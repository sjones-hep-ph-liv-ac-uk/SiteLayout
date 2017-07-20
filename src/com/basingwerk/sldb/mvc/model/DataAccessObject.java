package com.basingwerk.sldb.mvc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import com.basingwerk.sldb.mvc.exceptions.ConflictException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;

public class DataAccessObject {
    final static Logger logger = Logger.getLogger(DataAccessObject.class);

    private static DataAccessObject instance = null;

    private DataAccessObject() {
        clusterSetHash = new HashMap<String, ClusterSet> ();
        clusterHash = new HashMap<String, Cluster> ();
        nodeSetHash = new HashMap<String, NodeSet> ();
        nodeTypeHash = new HashMap<String, NodeType> ();
    }

    public static DataAccessObject getInstance() {
        if (instance == null) {
            instance = new DataAccessObject();
        }
        return instance;
    }

    private HashMap<String, ClusterSet> clusterSetHash;
    private HashMap<String, Cluster> clusterHash;
    private HashMap<String, NodeSet> nodeSetHash;
    private HashMap<String, NodeType> nodeTypeHash;

    // Helper functions
    public static ArrayList<NodeSetNodeTypeJoin> getJoinForCluster(HttpServletRequest request, String clusterName)
            throws WTFException {
        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();
        ArrayList<NodeSetNodeTypeJoin> nsntj = new ArrayList<NodeSetNodeTypeJoin>();
        try {
            hibSession.beginTransaction();
            List<NodeSet> ns = hibSession.createCriteria(NodeSet.class)
                    .add(Restrictions.eq("cluster.clusterName", clusterName)).list();
            for (NodeSet n : ns) {
                Float nodeSetHs06 = (float) (n.getNodeType().getHs06PerSlot() * n.getNodeCount());
                NodeSetNodeTypeJoin j = new NodeSetNodeTypeJoin(n.getNodeSetName(), n.getNodeType().getNodeTypeName(),
                        n.getNodeCount(), new Integer(n.getNodeType().getCpu()), new Integer(n.getNodeType().getSlot()),
                        new Float(n.getNodeType().getHs06PerSlot()), nodeSetHs06);
                nsntj.add(j);
            }
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using addNodeSet, ", e);
            throw new WTFException("WTF error using addNodeSet");
        } finally {
            hibSession.close();
        }
        return nsntj;
    }

    public static ArrayList<String> listClustersOfClusterSet(HttpServletRequest request, String clusterSetName)
            throws WTFException {
        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        ArrayList<String> cl = new ArrayList<String>();
        try {
            hibSession.beginTransaction();
            Query query = hibSession
                    .createQuery("select clusterName from Cluster where clusterSetName ='" + clusterSetName + "'");
            cl = (ArrayList) query.list();
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Error using getClustersOfClusterSet, ", ex);
            throw new WTFException("Error using getClustersOfClusterSet");
        } finally {
            hibSession.close();
        }

        return cl;
    }

    public static void setBaselineNodeType(HttpServletRequest request) throws WTFException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        ArrayList<NodeType> list = null;
        try {
            hibSession.beginTransaction();
            list = (ArrayList<NodeType>) hibSession.createCriteria(NodeType.class).list();
            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error using setBaselineNodeType, ", ex);
            throw new WTFException("Had an error using setBaselineNodeType");
        } finally {
            hibSession.close();
        }

        request.setAttribute("baseline", null);
        for (NodeType n : list) {
            if (n.getNodeTypeName().toUpperCase().startsWith("BASELINE")) {
                request.setAttribute("baseline", n);
            }
        }
    }

    // Member functions

    public void loadClusters(HttpServletRequest request, String col, String order) throws WTFException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        Order ord = org.hibernate.criterion.Order.desc(col);
        if (order.equalsIgnoreCase("ASC")) {
            ord = org.hibernate.criterion.Order.asc(col);
        }

        ArrayList<Cluster> clusterList = null;
        try {

            hibSession.beginTransaction();
            clusterList = (ArrayList<Cluster>) hibSession.createCriteria(Cluster.class).addOrder(ord).list();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using refreshListOfAllClusters, ", ex);
            throw new WTFException("WTF error using refreshListOfAllClusters");
        } finally {
            hibSession.close();
        }

        request.setAttribute("clusterList", clusterList);
        
        clusterHash = new HashMap<String, Cluster>();
        for (Cluster c : clusterList) {
            String clusterName = c.getClusterName();
            clusterHash.put(clusterName, c);
        }
    }

    public void loadClusterSets(HttpServletRequest request, String col, String order) throws WTFException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        Order ord = org.hibernate.criterion.Order.desc(col);
        if (order.equalsIgnoreCase("ASC")) {
            ord = org.hibernate.criterion.Order.asc(col);
        }
        ArrayList<ClusterSet> clusterSetList = null;
        try {

            hibSession.beginTransaction();
            clusterSetList = (ArrayList<ClusterSet>) hibSession.createCriteria(ClusterSet.class).addOrder(ord).list();
            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had a problem when using refreshClusterSets, ", ex);
            throw new WTFException("Had a problem when using refreshClusterSets");
        } finally {
            hibSession.close();
        }

        request.setAttribute("clusterSetList", clusterSetList);

        clusterSetHash = new HashMap<String, ClusterSet>();

        for (ClusterSet cs : clusterSetList) {
            String clusterSetName = cs.getClusterSetName();
            clusterSetHash.put(clusterSetName, cs);
        }
    }

    public void loadNodeSets(HttpServletRequest request, String col, String order) throws WTFException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        Order ord = org.hibernate.criterion.Order.desc(col);
        if (order.equalsIgnoreCase("ASC")) {
            ord = org.hibernate.criterion.Order.asc(col);
        }

        ArrayList<NodeSet> nodeSetList = null;
        try {

            hibSession.beginTransaction();
            nodeSetList = (ArrayList<NodeSet>) hibSession.createCriteria(NodeSet.class).addOrder(ord).list();
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error when trying to refresh node sets, ", ex);
            throw new WTFException("Had an error when trying to refresh node sets");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("nodeSetList", nodeSetList);

        nodeSetHash = new HashMap<String, NodeSet>();
        for (NodeSet cs : nodeSetList) {
            String nodeSetName = cs.getNodeSetName();
            nodeSetHash.put(nodeSetName, cs);
        }

    }

    public void loadNodeTypes(HttpServletRequest request, String col, String order) throws WTFException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        Order ord = org.hibernate.criterion.Order.desc(col);
        if (order.equalsIgnoreCase("ASC")) {
            ord = org.hibernate.criterion.Order.asc(col);
        }

        ArrayList<NodeType> nodeTypeList = null;
        try {

            hibSession.beginTransaction();
            nodeTypeList = (ArrayList<NodeType>) hibSession.createCriteria(NodeType.class).addOrder(ord).list();
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error when using refreshNodeTypes, ", ex);
            throw new WTFException("Had an error when using refreshNodeTypes");
        } finally {
            hibSession.close();
        }

        httpSession.setAttribute("nodeTypeList", nodeTypeList);
        request.setAttribute("nodeTypeList", nodeTypeList);

        nodeTypeHash = new HashMap<String, NodeType>();
        for (NodeType cs : nodeTypeList) {
            String nodeTypeName = cs.getNodeTypeName();
            nodeTypeHash.put(nodeTypeName, cs);
        }

    }

    public void loadNamedCluster(HttpServletRequest request, String clusterName)
            throws WTFException, ConflictException {
        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        Cluster storedCluster = null;
        Cluster cachedCluster = null;

        try {
            hibSession.beginTransaction();

            cachedCluster = clusterHash.get(clusterName);
            if (cachedCluster == null) {
                logger.error("While using loadNamedCluster, desired Cluster not found");
                throw new ConflictException("While using loadNamedCluster, desired Cluster not found");
            }
            Long cachedVersion = cachedCluster.getVersion();

            storedCluster = (Cluster) hibSession.createCriteria(Cluster.class)
                    .add(Restrictions.eq("clusterName", clusterName)).uniqueResult();
            // Possibly deleted during long conversation
            if (storedCluster == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadNamedCluster, desired Cluster not found");
                clusterHash.remove(clusterName);
                throw new ConflictException("While using loadNamedCluster, desired Cluster not found");
            }
            Long storedVersion = storedCluster.getVersion();

            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadNamedCluster, desired Cluster was altered by another user ");
                throw new ConflictException(
                        "While using loadNamedCluster, desired Cluster was altered by another user ");
            }

            hibSession.getTransaction().commit();
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using setSingleCluster, ", e);
            throw new WTFException("WTF error using setSingleCluster");
        } finally {
            hibSession.close();
        }

        httpSession.setAttribute("clusterVersion", storedCluster.getVersion());
        request.setAttribute("cluster", storedCluster);
        return;
    }

    public void loadNamedClusterSet(HttpServletRequest request, String clusterSetName)
            throws WTFException, ConflictException {
        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        ClusterSet cachedClusterSet = null;

        ClusterSet storedClusterSet = null;
        try {
            hibSession.beginTransaction();
            cachedClusterSet = clusterSetHash.get(clusterSetName);
            if (cachedClusterSet == null) {
                logger.error("While using loadNamedClusterSet, desired ClusterSet not found");
                throw new ConflictException("While using loadNamedClusterSet, desired ClusterSet not found");
            }
            Long cachedVersion = cachedClusterSet.getVersion();

            storedClusterSet = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
                    .add(Restrictions.eq("clusterSetName", clusterSetName)).uniqueResult();
            if (storedClusterSet == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using loadNamedClusterSet, desired ClusterSet not found");
                throw new ConflictException("While using loadNamedClusterSet, desired ClusterSet not found");
            }
            // Possibly altered during long conversation
            Long storedVersion = storedClusterSet.getVersion();
            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadNamedClusterSet, desired ClusterSet was altered by another user ");
                throw new ConflictException(
                        "While using loadNamedClusterSet, desired ClusterSet was altered by another user ");
            }

            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadNamedClusterSet, ", ex);
            throw new WTFException("WTF error using loadNamedClusterSet");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("clusterSetVersion", storedClusterSet.getVersion());
        request.setAttribute("clusterSet", storedClusterSet);

        return;
    }

    public void loadNamedNodeSet(HttpServletRequest request, String nodeSetName)
            throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        NodeSet cachedNodeSet = null;
        NodeSet storedNodeSet = null;
        try {
            hibSession.beginTransaction();

            cachedNodeSet = nodeSetHash.get(nodeSetName);
            if (cachedNodeSet == null) {
                logger.error("While using loadNamedNodeSet, desired NodeSet not found");
                throw new ConflictException("While using loadNamedNodeSet, desired NodeSet not found");
            }

            Long cachedVersion = cachedNodeSet.getVersion();
            storedNodeSet = (NodeSet) hibSession.createCriteria(NodeSet.class)
                    .add(Restrictions.eq("nodeSetName", nodeSetName)).uniqueResult();
            // Possibly deleted during long conversation
            if (storedNodeSet == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadNamedNodeSet, desired NodeSet not found");
                throw new ConflictException("While using loadNamedNodeSet, desired NodeSet not found");
            }

            Long storedVersion = storedNodeSet.getVersion();
            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadNamedNodeSet, desired NodeSet was altered by another user ");
                throw new ConflictException(
                        "While using loadNamedNodeSet, desired NodeSet was altered by another user ");
            }

            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadNamedNodeSet, ", ex);
            throw new WTFException("WTF error using loadNamedNodeSet");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("nodeSetVersion", storedNodeSet.getVersion());
        request.setAttribute("nodeSet", storedNodeSet);
        return;
    }

    public void loadNamedNodeType(HttpServletRequest request, String nodeTypeName)
            throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        NodeType cachedNodeType = null;
        NodeType storedNodeType = null;
        try {
            hibSession.beginTransaction();
            cachedNodeType = nodeTypeHash.get(nodeTypeName);
            if (cachedNodeType == null) {
                logger.error("While using loadNamedNodeType, desired NodeType not found");
                throw new ConflictException("While using loadNamedNodeType, desired NodeType not found");
            }

            Long cachedVersion = cachedNodeType.getVersion();

            storedNodeType = (NodeType) hibSession.createCriteria(NodeType.class)
                    .add(Restrictions.eq("nodeTypeName", nodeTypeName)).uniqueResult();
            // Possibly deleted during long conversation
            if (storedNodeType == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadNamedNodeType, desired NodeType not found");
                throw new ConflictException("While using loadNamedNodeType, desired NodeType not found");
            }

            Long storedVersion = storedNodeType.getVersion();
            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadNamedNodeType, desired NodeType was altered by another user ");
                throw new ConflictException(
                        "While using loadNamedNodeType, desired NodeType was altered by another user ");
            }

            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadNamedNodeType, ", ex);
            throw new WTFException("WTF error using loadNamedNodeType");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("nodeTypeVersion", storedNodeType.getVersion());
        request.setAttribute("nodeType", storedNodeType);
        return;
    }

    public void addCluster(HttpServletRequest request) throws ConflictException, WTFException {
        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        String clusterName = request.getParameter("clusterName");
        String descr = request.getParameter("descr");
        String clusterSetName = request.getParameter("clusterSetList");

        Cluster cluster = new Cluster();

        try {
            hibSession.beginTransaction();

            Cluster c = clusterHash.get(clusterName);
            if (c != null) {
                hibSession.getTransaction().rollback();
                logger.error("While using addCluster, desired ClusterSet already there");
                throw new ConflictException("While using addCluster, desired ClusterSet already there");
            }

            ClusterSet cs = clusterSetHash.get(clusterSetName);
            if (cs == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addCluster, desired ClusterSet not found");
                throw new ConflictException("While using addCluster, desired ClusterSet not found");
            }
            cluster.setClusterName(clusterName);
            cluster.setDescr(descr);
            cluster.setClusterSet(cs);
            clusterHash.put(clusterName, cluster);

            hibSession.save(cluster);
            hibSession.getTransaction().commit();

        } catch (ConstraintViolationException e) {
            hibSession.getTransaction().rollback();
            logger.error("While using addCluster, the clusterName conflicted with an existing cluster");
            throw new ConflictException("While using addCluster, the clusterName conflicted with an existing cluster");
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF while using addCluster", e);
            throw new WTFException("WTF while using addCluster");
        } finally {
            hibSession.close();
        }
    }

    public void addClusterSet(HttpServletRequest request) throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        String clusterSetName = request.getParameter("clusterSetName");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");
        String admin = request.getParameter("admin");

        ClusterSet clusterSet = new ClusterSet();

        try {
            hibSession.beginTransaction();

            if (clusterSetHash.get(clusterSetName) != null) {
                logger.error("While using addClusterSet, desired clusterSet already there");
                throw new ConflictException("While using addClusterSet, desired clusterSet already there");
            }

            clusterSet.setClusterSetName(clusterSetName);
            clusterSet.setDescription(description);
            clusterSet.setLocation(location);
            clusterSet.setLongitude(Double.parseDouble(longitude));
            clusterSet.setLatitude(Double.parseDouble(latitude));
            clusterSet.setAdmin(admin);
            clusterSetHash.put(clusterSetName, clusterSet);

            hibSession.save(clusterSet);
            hibSession.getTransaction().commit();

        } catch (ConstraintViolationException e) {
            hibSession.getTransaction().rollback();
            logger.error("While using addClusterSet, the clusterSetName conflicted with an existing clusterSet");
            throw new ConflictException(
                    "While using addClusterSet, the clusterSetName conflicted with an existing clusterSet");
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF while using addClusterSet, ", e);
            throw new WTFException("WTF while using addClusterSet");
        } finally {
            hibSession.close();
        }
    }

    public void addNodeType(HttpServletRequest request) throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        String nodeTypeName = request.getParameter("nodeTypeName");
        String cpu = request.getParameter("cpu");
        String slot = request.getParameter("slot");
        String hs06PerSlot = request.getParameter("hs06PerSlot");
        String memPerNode = request.getParameter("memPerNode");

        NodeType nt = new NodeType();

        try {
            hibSession.beginTransaction();

            if (nodeTypeHash.get(nodeTypeName) != null) {
                logger.error("While using addNodeType, desired NodeType already there");
                throw new ConflictException("While using addNodeType, desired NodeType already there");
            }
            nt.setNodeTypeName(nodeTypeName);
            nt.setCpu(Integer.parseInt(cpu));
            nt.setHs06PerSlot(Integer.parseInt(hs06PerSlot));
            nt.setSlot(Integer.parseInt(slot));
            nt.setMemPerNode(Integer.parseInt(memPerNode));
            nodeTypeHash.put(nodeTypeName, nt);

            hibSession.save(nt);
            hibSession.getTransaction().commit();

        } catch (ConstraintViolationException e) {
            hibSession.getTransaction().rollback();
            logger.error("While using addNodeType, the nodeTypeName conflicted with an existing nodeType");
            throw new ConflictException(
                    "While using addNodeType, the nodeTypeName conflicted with an existing nodeType");
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using addNodeType, ", e);
            throw new WTFException("WTF error using addNodeType");
        } finally {
            hibSession.close();
        }
    }

    public void addNodeSet(HttpServletRequest request) throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        String nodeSetName = request.getParameter("nodeSetName");
        String nodeCount = request.getParameter("nodeCount");
        String nodeTypeName = request.getParameter("nodeTypeList");
        String clusterName = request.getParameter("clusterList");

        NodeSet nodeSet = new NodeSet();

        try {
            hibSession.beginTransaction();

            Cluster c = clusterHash.get(clusterName);

            if (c == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addNodeSet, desired Cluster not found");
                throw new ConflictException("While using addNodeSet, desired Cluster not found");
            }

            NodeType nodeType = this.nodeTypeHash.get(nodeTypeName);
            if (nodeType == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addNodeSet, desired NodeType not found");
                throw new ConflictException("While using addNodeSet, desired NodeType not found");
            }
            nodeSet.setCluster(c);
            nodeSet.setNodeType(nodeType);
            nodeSet.setNodeCount(Integer.parseInt(nodeCount));
            nodeSet.setNodeSetName(nodeSetName);
            nodeSetHash.put(nodeSetName, nodeSet);
            hibSession.save(nodeSet);
            hibSession.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            hibSession.getTransaction().rollback();
            logger.error("While using addNodeSet, the nodeSetName conflicted with an existing nodeSet");
            throw new ConflictException("While using addNodeSet, the nodeSetName conflicted with an existing nodeSet");
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using addNodeSet, ", e);
            throw new WTFException("WTF error using addNodeSet");
        } finally {
            hibSession.close();
        }
    }

    public void deleteCluster(HttpServletRequest request, String clusterName) throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        Cluster cluster = clusterHash.get(clusterName);
        if (cluster == null) {
            logger.error("While using deleteCluster, desired Cluster not found");
            throw new ConflictException("While using deleteCluster, desired Cluster not found");
        }
        try {
            hibSession.beginTransaction();
            hibSession.delete(cluster);
            hibSession.getTransaction().commit();
            clusterHash.remove(clusterName);
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteCluster, desired Cluster not found");
            throw new ConflictException("While using deleteCluster, desired Cluster not found");
        } finally {
            hibSession.close();
        }
    }

    public void deleteClusterSet(HttpServletRequest request, String clusterSetName)
            throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        ClusterSet clusterSet = clusterSetHash.get(clusterSetName);
        if (clusterSet == null) {
            logger.error("While using deleteCluster, desired Cluster not found");
            throw new ConflictException("While using deleteCluster, desired Cluster not found");
        }
        try {
            hibSession.beginTransaction();
            hibSession.delete(clusterSet);
            hibSession.getTransaction().commit();
            clusterSetHash.remove(clusterSetName);
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteClusterSet, desired ClusterSet not found");
            throw new ConflictException("While using deleteClusterSet, desired ClusterSet not found");
        } finally {
            hibSession.close();
        }
    }

    public void deleteNodeSet(HttpServletRequest request, String nodeSetName) throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        NodeSet nodeSet = nodeSetHash.get(nodeSetName);
        if (nodeSet == null) {
            logger.error("While using deleteNodeSet, desired NodeSet not found");
            throw new ConflictException("While using deleteNodeSet, desired NodeSet not found");
        }
        try {
            hibSession.beginTransaction();
            hibSession.delete(nodeSet);
            hibSession.getTransaction().commit();
            clusterSetHash.remove(nodeSetName);
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteNodeSet, desired NodeSet not found");
            throw new ConflictException("While using deleteNodeSet, desired NodeSet not found");
        } finally {
            hibSession.close();
        }
    }

    public void deleteNodeType(HttpServletRequest request, String nodeTypeName) throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        NodeType nodeType = nodeTypeHash.get(nodeTypeName);
        if (nodeType == null) {
            logger.error("While using deleteNodeType, desired NodeType not found");
            throw new ConflictException("While using deleteNodeType, desired NodeType not found");
        }
        try {
            hibSession.beginTransaction();
            hibSession.delete(nodeType);
            hibSession.getTransaction().commit();
            clusterSetHash.remove(nodeTypeName);
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteNodeType, desired NodeType not found");
            throw new ConflictException("While using deleteNodeType, desired NodeType not found");
        } finally {
            hibSession.close();
        }
    }

    public void updateCluster(HttpServletRequest request) throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        try {
            hibSession.beginTransaction();

            // We assume it is updated, whether it is or is not
            String updatedClusterName = request.getParameter("clusterName");
            String updatedDescr = request.getParameter("descr");
            String updatedClusterSetName = request.getParameter("clusterSetList");

            Cluster cachedCluster = null;
            Cluster storedCluster = null;

            cachedCluster = clusterHash.get(updatedClusterName);
            if (cachedCluster == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateCluster, desired Cluster not found");
                throw new ConflictException("While using updateCluster, desired Cluster not found");
            }
            Long cachedClusterVersion = cachedCluster.getVersion();

            storedCluster = (Cluster) hibSession.createCriteria(Cluster.class)
                    .add(Restrictions.eq("clusterName", updatedClusterName)).uniqueResult();
            // Possibly deleted during long conversation
            if (storedCluster == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateCluster, desired Cluster  not found");
                throw new ConflictException("While using updateCluster, desired Cluster  not found");
            }
            // Possibly altered during long conversation

            Long storedClusterVersion = storedCluster.getVersion();

            if (!storedClusterVersion.equals(cachedClusterVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateCluster, desired Cluster was altered by another user ");
                throw new ConflictException("While using updateCluster, desired Cluster was altered by another user ");
            }

            // Both the same. Safe to update

            storedCluster.setClusterName(updatedClusterName);
            storedCluster.setDescr(updatedDescr);
            ClusterSet newClusterSet = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
                    .add(Restrictions.eq("clusterSetName", updatedClusterSetName)).uniqueResult();
            if (newClusterSet == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateCluster, desired ClusterSet  not found");
                throw new ConflictException("While using updateCluster, desired ClusterSet not found");
            }
            storedCluster.setClusterSet(newClusterSet);
            hibSession.update(storedCluster);
            hibSession.getTransaction().commit();
            clusterHash.remove(updatedClusterName);
            clusterHash.put(updatedClusterName,storedCluster);
            
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using updateClusterSet, ", e);
            throw new WTFException("WTF error using updateClusterSet");
        } finally {
            hibSession.close();
        }
    }
    
    
    public void updateNodeSet(HttpServletRequest request) throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        try {
            hibSession.beginTransaction();

            // We assume it is updated, whether it is or is not
            String updatedNodeSetName = request.getParameter("nodeSetName");
            String updatedNodeCount = request.getParameter("nodeCount");
            String updatedNodeTypeName = request.getParameter("nodeTypeList");
            String updatedClusterName = request.getParameter("clusterList");

            NodeSet cachedNodeSet = null;
            NodeSet storedNodeSet = null;

            cachedNodeSet = nodeSetHash.get(updatedNodeSetName);
            if (cachedNodeSet == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeSet, desired NodeSet not found");
                throw new ConflictException("While using updateNodeSet, desired NodeSet not found");
            }
            Long cachedNodeSetVersion = cachedNodeSet.getVersion();

            storedNodeSet = (NodeSet) hibSession.createCriteria(NodeSet.class)
                    .add(Restrictions.eq("nodeSetName", updatedNodeSetName)).uniqueResult();
            // Possibly deleted during long conversation
            if (storedNodeSet == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeSet, desired NodeSet  not found");
                throw new ConflictException("While using updateNodeSet, desired NodeSet  not found");
            }
            // Possibly altered during long conversation

            Long storedNodeSetVersion = storedNodeSet.getVersion();

            if (!storedNodeSetVersion.equals(cachedNodeSetVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeSet, desired NodeSet was altered by another user ");
                throw new ConflictException("While using updateNodeSet, desired NodeSet was altered by another user ");
            }

            // Both the same. Safe to update

            storedNodeSet.setNodeSetName(updatedNodeSetName);
            storedNodeSet.setNodeCount(Integer.parseInt(updatedNodeCount));

            Cluster cluster = (Cluster) hibSession.createCriteria(Cluster.class)
                    .add(Restrictions.eq("clusterName", updatedClusterName)).uniqueResult();
            if (cluster  == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeSet, desired Cluster  not found");
                throw new ConflictException("While using updateNodeSet, desired Cluster not found");
            }
            storedNodeSet.setCluster(cluster);
            

            NodeType nodeType = (NodeType) hibSession.createCriteria(NodeType.class)
                    .add(Restrictions.eq("nodeTypeName", updatedNodeTypeName)).uniqueResult();
            if (nodeType  == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeSet, desired NodeType  not found");
                throw new ConflictException("While using updateNodeSet, desired NodeType not found");
            }
            storedNodeSet.setNodeType(nodeType);
            
            hibSession.update(storedNodeSet);
            hibSession.getTransaction().commit();
            nodeSetHash.remove(updatedNodeSetName);
            nodeSetHash.put(updatedNodeSetName,storedNodeSet);
            
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using updateNodeSet, ", e);
            throw new WTFException("WTF error using updateNodeSet");
        } finally {
            hibSession.close();
        }
    }

    public void updateNodeType(HttpServletRequest request) throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        try {
            hibSession.beginTransaction();
            String updatedNodeTypeName = request.getParameter("nodeTypeName");
            String updatedCpu = request.getParameter("cpu");
            String updatedSlot = request.getParameter("slot");
            String updatedHs06PerSlot = request.getParameter("hs06PerSlot");
            String updatedMemPerNode = request.getParameter("memPerNode");

            NodeType cachedNodeType = null;
            NodeType storedNodeType = null;

            cachedNodeType = nodeTypeHash.get(updatedNodeTypeName);
            if (cachedNodeType == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeType, desired NodeType not found");
                throw new ConflictException("While using updateNodeType, desired NodeType not found");
            }
            Long cachedNodeTypeVersion = cachedNodeType.getVersion();

            storedNodeType = (NodeType) hibSession.createCriteria(NodeType.class)
                    .add(Restrictions.eq("nodeTypeName", updatedNodeTypeName)).uniqueResult();
            // Possibly deleted during long conversation
            if (storedNodeType == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeType, desired NodeType  not found");
                throw new ConflictException("While using updateNodeType, desired NodeType  not found");
            }
            // Possibly altered during long conversation

            Long storedNodeTypeVersion = storedNodeType.getVersion();

            if (!storedNodeTypeVersion.equals(cachedNodeTypeVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeType, desired NodeType was altered by another user ");
                throw new ConflictException("While using updateNodeType, desired NodeType was altered by another user ");
            }

            // Both the same. Safe to update

            storedNodeType.setCpu(Integer.parseInt(updatedCpu));
            storedNodeType.setSlot(Integer.parseInt(updatedSlot));
            storedNodeType.setHs06PerSlot(Double.parseDouble(updatedHs06PerSlot));
            storedNodeType.setMemPerNode(Double.parseDouble(updatedMemPerNode));
            
            hibSession.update(storedNodeType);
            hibSession.getTransaction().commit();
            nodeTypeHash.remove(updatedNodeTypeName);
            nodeTypeHash.put(updatedNodeTypeName,storedNodeType);
            
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using updateNodeType, ", e);
            throw new WTFException("WTF error using updateNodeType");
        } finally {
            hibSession.close();
        }
    }

    public void updateClusterSet(HttpServletRequest request) throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        try {
            hibSession.beginTransaction();
            
            String updatedClusterSetName = request.getParameter("clusterSetName");
            String updatedDescription = request.getParameter("description");
            String updatedLocation = request.getParameter("location");
            String updatedLongitude = request.getParameter("longitude");
            String updatedLatitude = request.getParameter("latitude");
            String updatedAdmin = request.getParameter("admin");
            
            ClusterSet cachedClusterSet = null;
            ClusterSet storedClusterSet = null;

            cachedClusterSet = clusterSetHash.get(updatedClusterSetName);
            if (cachedClusterSet == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateClusterSet, desired ClusterSet not found");
                throw new ConflictException("While using updateClusterSet, desired ClusterSet not found");
            }
            Long cachedClusterSetVersion = cachedClusterSet.getVersion();

            storedClusterSet = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
                    .add(Restrictions.eq("clusterSetName", updatedClusterSetName)).uniqueResult();
            // Possibly deleted during long conversation
            if (storedClusterSet == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateClusterSet, desired ClusterSet  not found");
                throw new ConflictException("While using updateClusterSet, desired ClusterSet  not found");
            }
            // Possibly altered during long conversation

            Long storedClusterSetVersion = storedClusterSet.getVersion();

            if (!storedClusterSetVersion.equals(cachedClusterSetVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateClusterSet, desired ClusterSet was altered by another user ");
                throw new ConflictException("While using updateClusterSet, desired ClusterSet was altered by another user ");
            }

            // Both the same. Safe to update

            storedClusterSet.setClusterSetName(updatedClusterSetName);
            storedClusterSet.setDescription(updatedDescription);
            storedClusterSet.setLocation(updatedLocation);
            storedClusterSet.setLongitude(Double.parseDouble(updatedLongitude));
            storedClusterSet.setLatitude(Double.parseDouble(updatedLatitude));
            storedClusterSet.setAdmin(updatedAdmin);
            
            hibSession.update(storedClusterSet);
            hibSession.getTransaction().commit();
            clusterSetHash.remove(updatedClusterSetName);
            clusterSetHash.put(updatedClusterSetName,storedClusterSet);
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using updateClusterSet, ", e);
            throw new WTFException("WTF error using updateClusterSet");
        } finally {
            hibSession.close();
        }
    }
}
