package com.basingwerk.sldb.mvc.model;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DataAccessObject {
    final static Logger logger = Logger.getLogger(DataAccessObject.class);

    private static DataAccessObject instance = null;

    protected DataAccessObject() {
        // Exists only to defeat instantiation.
    }

    public static DataAccessObject getInstance() {
        if (instance == null) {
            instance = new DataAccessObject();
        }
        return instance;
    }

    public void setBaselineNodeType(HttpServletRequest request) throws RoutineException, WTFException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        List<NodeType> nodeTypes = null;
        try {
            hibSession.beginTransaction();

            nodeTypes = this.readNodeTypeList(hibSession, "nodeTypeName", "ASC");
            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error using setBaselineNodeType, ", ex);
            throw new WTFException("Had an error using setBaselineNodeType");
        } finally {
            hibSession.close();
        }

        request.setAttribute("baseline", null);
        for (NodeType n : nodeTypes) {
            if (n.getNodeTypeName().toUpperCase().startsWith("BASELINE")) {
                request.setAttribute("baseline", n);
            }
        }
    }

    public void loadClusters(HttpServletRequest request, String col, String order)
            throws RoutineException, WTFException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        List<Cluster> clusterList = new ArrayList<Cluster>();
        try {

            hibSession.beginTransaction();

            clusterList = readClusterList(hibSession, col, order);

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadClusters, ", ex);
            throw new WTFException("WTF error using loadClusters");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("clusterList", clusterList);
    }

    public void loadHostSystems(HttpServletRequest request, String col, String order)
            throws RoutineException, WTFException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        List<HostSystem> hostSystemList = new ArrayList<HostSystem>();
        try {

            hibSession.beginTransaction();

            hostSystemList = readHostSystemList(hibSession, col, order);

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadHostSystems, ", ex);
            throw new WTFException("WTF error using loadHostSystems");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("hostSystemList", hostSystemList);
    }

    public void loadNodeStates(HttpServletRequest request, String col, String order)
            throws RoutineException, WTFException {

        List<NodeState> nodeStateList = new ArrayList<NodeState>();

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        nodeStateList = null;
        try {

            hibSession.beginTransaction();
            nodeStateList = this.readNodeStateList(hibSession, col, order);

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadNodeStates, ", ex);
            throw new WTFException("WTF error using loadNodeStates");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("nodeStateList", nodeStateList);
    }

    public void loadClusterSets(HttpServletRequest request, String col, String order)
            throws RoutineException, WTFException {

        List<ClusterSet> clusterSetList = new ArrayList<ClusterSet>();

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        clusterSetList = null;

        try {

            hibSession.beginTransaction();
            clusterSetList = readClusterSetList(hibSession, col, order);
            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had a problem when using loadClusterSets, ", ex);
            throw new WTFException("Had a problem when using loadClusterSets");
        } finally {
            hibSession.close();
        }

        httpSession.setAttribute("clusterSetList", clusterSetList);
    }

    public void loadNodeSets(HttpServletRequest request, String col, String order)
            throws RoutineException, WTFException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        List<NodeSet> nodeSetList = new ArrayList<NodeSet>();
        try {

            hibSession.beginTransaction();
            nodeSetList = this.readNodeSetList(hibSession, col, order);

            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error using loadNodeSets", ex);
            throw new WTFException("Had an error using loadNodeSets");
        } finally {
            hibSession.close();
        }

        httpSession.setAttribute("nodeSetList", nodeSetList);
    }

    public void loadInstallations(HttpServletRequest request, String col, String order)
            throws RoutineException, WTFException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        List<Installation> installationList = new ArrayList<Installation>();
        try {

            hibSession.beginTransaction();

            installationList = this.readInstallationList(hibSession, col, order);

            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error using loadInstallations", ex);
            throw new WTFException("Had an error using loadInstallations");
        } finally {
            hibSession.close();
        }

        httpSession.setAttribute("installationList", installationList);
    }

    public void loadNodes(HttpServletRequest request, String col, String order) throws RoutineException, WTFException {
        List<Node> nodeList = new ArrayList<Node>();

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        nodeList = null;
        try {

            hibSession.beginTransaction();

            nodeList = this.readNodeList(hibSession, col, order);

            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error using loadNodes, ", ex);
            throw new WTFException("Had an error using loadNodes");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("nodeList", nodeList);
    }

    public void loadServiceNodes(HttpServletRequest request, String col, String order)
            throws RoutineException, WTFException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        List<ServiceNode> serviceNodeList = new ArrayList<ServiceNode>();
        try {

            hibSession.beginTransaction();

            serviceNodeList = this.readServiceNodeList(hibSession, col, order);

            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error using loadServiceNodes, ", ex);
            throw new WTFException("Had an error using loadServiceNodes");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("serviceNodeList", serviceNodeList);
    }

    public void loadServices(HttpServletRequest request, String col, String order)
            throws RoutineException, WTFException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        List<Service> serviceList = new ArrayList<Service>();
        try {

            hibSession.beginTransaction();
            serviceList = this.readServiceList(hibSession, col, order);

            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error using loadServices, ", ex);
            throw new WTFException("Had an error using loadServices");
        } finally {
            hibSession.close();
        }

        httpSession.setAttribute("serviceList", serviceList);
    }

    public void loadNodeTypes(HttpServletRequest request, String col, String order)
            throws RoutineException, WTFException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        List<NodeType> nodeTypeList = null;
        try {

            hibSession.beginTransaction();

            nodeTypeList = readNodeTypeList(hibSession, col, order);

            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error using loadNodeTypes, ", ex);
            throw new WTFException("Had an error using loadNodeTypes");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("nodeTypeList", nodeTypeList);
    }

    public void loadIndexedCluster(HttpServletRequest request, Integer clusterIndex)
            throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        Cluster cachedCluster = null;
        Cluster storedCluster = null;

        try {
            hibSession.beginTransaction();

            cachedCluster = ((ArrayList<Cluster>) httpSession.getAttribute("clusterList")).get(clusterIndex);
            if (cachedCluster == null) {
                logger.error("While using loadIndexedCluster, desired Cluster not found");
                throw new RoutineException("While using loadIndexedCluster, desired Cluster not found");
            }
            Long cachedVersion = cachedCluster.getVersion();

            storedCluster = this.readOneCluster(hibSession, cachedCluster.getClusterName());

            // Possibly deleted during long conversation
            if (storedCluster == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedCluster, desired Cluster not found");
                throw new RoutineException("While using loadIndexedCluster, desired Cluster not found");
            }
            Long storedVersion = storedCluster.getVersion();

            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedCluster, desired Cluster was altered by another user ");
                throw new RoutineException(
                        "While using loadIndexedCluster, desired Cluster was altered by another user ");
            }

            hibSession.getTransaction().commit();
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadIndexedCluster, ", e);
            throw new WTFException("WTF error using loadIndexedCluster");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("cluster", storedCluster);
    }

    public void loadIndexedClusterSet(HttpServletRequest request, Integer clusterSetIndex)
            throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        ClusterSet cachedClusterSet = null;

        ClusterSet storedClusterSet = null;
        try {
            hibSession.beginTransaction();
            cachedClusterSet = ((ArrayList<ClusterSet>) httpSession.getAttribute("clusterSetList"))
                    .get(clusterSetIndex);

            if (cachedClusterSet == null) {
                logger.error("While using loadIndexedClusterSet, desired ClusterSet not found");
                throw new RoutineException("While using loadIndexedClusterSet, desired ClusterSet not found");
            }
            Long cachedVersion = cachedClusterSet.getVersion();

            storedClusterSet = this.readOneClusterSet(hibSession, cachedClusterSet.getClusterSetName());

            if (storedClusterSet == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedClusterSet, desired ClusterSet not found");
                throw new RoutineException("While using loadIndexedClusterSet, desired ClusterSet not found");
            }
            // Possibly altered during long conversation
            Long storedVersion = storedClusterSet.getVersion();
            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedClusterSet, desired ClusterSet was altered by another user ");
                throw new RoutineException(
                        "While using loadIndexedClusterSet, desired ClusterSet was altered by another user ");
            }

            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadIndexedClusterSet, ", ex);
            throw new WTFException("WTF error using loadIndexedClusterSet");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("clusterSet", storedClusterSet);
    }

    public void loadIndexedNodeSet(HttpServletRequest request, Integer nodeSetIndex)
            throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        NodeSet cachedNodeSet = null;
        NodeSet storedNodeSet = null;
        try {
            hibSession.beginTransaction();

            cachedNodeSet = ((ArrayList<NodeSet>) httpSession.getAttribute("nodeSetList")).get(nodeSetIndex);

            if (cachedNodeSet == null) {
                logger.error("While using loadIndexedNodeSet, desired NodeSet not found");
                throw new RoutineException("While using loadIndexedNodeSet, desired NodeSet not found");
            }

            Long cachedVersion = cachedNodeSet.getVersion();

            storedNodeSet = this.readOneNodeSet(hibSession, cachedNodeSet.getNodeSetName());

            // Possibly deleted during long conversation
            if (storedNodeSet == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedNodeSet, desired NodeSet not found");
                throw new RoutineException("While using loadIndexedNodeSet, desired NodeSet not found");
            }

            Long storedVersion = storedNodeSet.getVersion();
            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedNodeSet, desired NodeSet was altered by another user ");
                throw new RoutineException(
                        "While using loadIndexedNodeSet, desired NodeSet was altered by another user ");
            }

            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadIndexedNodeSet, ", ex);
            throw new WTFException("WTF error using loadIndexedNodeSet");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("nodeSet", storedNodeSet);
    }

    public void loadIndexedNodeType(HttpServletRequest request, Integer nodeTypeIndex)
            throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        NodeType cachedNodeType = null;
        NodeType storedNodeType = null;
        try {
            hibSession.beginTransaction();
            cachedNodeType = ((ArrayList<NodeType>) httpSession.getAttribute("nodeTypeList")).get(nodeTypeIndex);
            if (cachedNodeType == null) {
                logger.error("While using loadIndexedNodeType, desired NodeType not found");
                throw new RoutineException("While using loadIndexedNodeType, desired NodeType not found");
            }

            Long cachedVersion = cachedNodeType.getVersion();

            storedNodeType = this.readOneNodeType(hibSession, cachedNodeType.getNodeTypeName());

            // Possibly deleted during long conversation
            if (storedNodeType == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedNodeType, desired NodeType not found");
                throw new RoutineException("While using loadIndexedNodeType, desired NodeType not found");
            }

            Long storedVersion = storedNodeType.getVersion();
            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedNodeType, desired NodeType was altered by another user ");
                throw new RoutineException(
                        "While using loadIndexedNodeType, desired NodeType was altered by another user ");
            }

            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadIndexedNodeType, ", ex);
            throw new WTFException("WTF error using loadIndexedNodeType");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("nodeType", storedNodeType);
    }

    public void loadIndexedNode(HttpServletRequest request, Integer nodeIndex) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        Node cachedNode = null;
        Node storedNode = null;
        try {
            hibSession.beginTransaction();

            cachedNode = ((ArrayList<Node>) httpSession.getAttribute("nodeList")).get(nodeIndex);

            if (cachedNode == null) {
                logger.error("While using loadIndexedNode, desired Node not found");
                throw new RoutineException("While using loadIndexedNode, desired Node not found");
            }

            Long cachedVersion = cachedNode.getVersion();

            storedNode = this.readOneNode(hibSession, cachedNode.getNodeName());

            // Possibly deleted during long conversation
            if (storedNode == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedNode, desired Node not found");
                throw new RoutineException("While using loadIndexedNode, desired Node not found");
            }

            Long storedVersion = storedNode.getVersion();
            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedNode, desired Node was altered by another user ");
                throw new RoutineException("While using loadIndexedNode, desired Node was altered by another user ");
            }

            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadIndexedNode, ", ex);
            throw new WTFException("WTF error using loadIndexedNode");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("node", storedNode);
    }

    public void loadIndexedServiceNode(HttpServletRequest request, Integer serviceNodeIndex)
            throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        ServiceNode cachedServiceNode = null;
        ServiceNode storedServiceNode = null;
        try {
            hibSession.beginTransaction();

            cachedServiceNode = ((ArrayList<ServiceNode>) httpSession.getAttribute("serviceNodeList"))
                    .get(serviceNodeIndex);

            if (cachedServiceNode == null) {
                logger.error("While using loadIndexedServiceNode, desired ServiceNode not found");
                throw new RoutineException("While using loadIndexedServiceNode, desired ServiceNode not found");
            }

            Long cachedVersion = cachedServiceNode.getVersion();

            storedServiceNode = this.readOneServiceNode(hibSession, cachedServiceNode.getHostname());

            // Possibly deleted during long conversation
            if (storedServiceNode == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedServiceNode, desired ServiceNode not found");
                throw new RoutineException("While using loadIndexedServiceNode, desired ServiceNode not found");
            }

            Long storedVersion = storedServiceNode.getVersion();
            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedServiceNode, desired ServiceNode was altered by another user ");
                throw new RoutineException(
                        "While using loadIndexedServiceNode, desired ServiceNode was altered by another user ");
            }

            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadIndexedServiceNode, ", ex);
            throw new WTFException("WTF error using loadIndexedServiceNode");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("serviceNode", storedServiceNode);
    }

    public void toggleCheckedNodes(HttpServletRequest request) throws RoutineException, WTFException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        try {
            hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();
            String[] choices = request.getParameterValues("choices");
            if (choices == null) {
                return;
            }
            hibSession.beginTransaction();
            for (String c : choices) {
                String nodeName = c.substring(4, c.length());
                Node node = this.readOneNode(hibSession, nodeName);
                if (node.getNodeState().getState().equalsIgnoreCase("ONLINE"))
                    node.setNodeState(this.readOneNodeState(hibSession, "OFFLINE"));
                else
                    node.setNodeState(this.readOneNodeState(hibSession, "ONLINE"));
                hibSession.save(node);
            }
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.warn("Some minor error occured toggling a node state", ex);
        } finally {
            hibSession.close();
        }
    }

    public void toggleIndexedNode(HttpServletRequest request, Integer nodeIndex) throws RoutineException, WTFException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        try {
            hibSession.beginTransaction();
            Node node = ((ArrayList<Node>) httpSession.getAttribute("node")).get(nodeIndex);

            if (node.getNodeState().getState().equalsIgnoreCase("ONLINE"))
                node.setNodeState(this.readOneNodeState(hibSession, "OFFLINE"));
            else
                node.setNodeState(this.readOneNodeState(hibSession, "ONLINE"));
            //
            hibSession.save(node);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.warn("Some minor error occured toggling a node state", ex);
            // throw new WTFException("WTF error using toggleIndexedNode");
        } finally {
            hibSession.close();
        }
    }

    public void addCluster(HttpServletRequest request) throws RoutineException, WTFException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        String clusterName = request.getParameter("clusterName");
        String descr = request.getParameter("descr");
        String clusterSetName = request.getParameter("clusterSetList");

        Cluster cluster = new Cluster();

        try {
            hibSession.beginTransaction();

            ClusterSet cs = this.readOneClusterSet(hibSession, clusterSetName);

            if (cs == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addCluster, desired ClusterSet not found");
                throw new RoutineException("While using addCluster, desired ClusterSet not found");
            }
            cluster.setClusterName(clusterName);
            cluster.setDescr(descr);
            cluster.setClusterSet(cs);

            hibSession.save(cluster);
            hibSession.getTransaction().commit();

        } catch (ConstraintViolationException e) {
            hibSession.getTransaction().rollback();
            logger.error("While using addCluster, the clusterName conflicted with an existing cluster");
            throw new RoutineException("While using addCluster, the clusterName conflicted with an existing cluster");
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF while using addCluster", e);
            throw new WTFException("WTF while using addCluster");
        } finally {
            hibSession.close();
        }
    }

    public void addClusterSet(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        String clusterSetName = request.getParameter("clusterSetName");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");
        String admin = request.getParameter("admin");

        ClusterSet clusterSet = new ClusterSet();

        try {
            hibSession.beginTransaction();
            clusterSet.setClusterSetName(clusterSetName);
            clusterSet.setDescription(description);
            clusterSet.setLocation(location);
            clusterSet.setLongitude(Double.parseDouble(longitude));
            clusterSet.setLatitude(Double.parseDouble(latitude));
            clusterSet.setAdmin(admin);

            hibSession.save(clusterSet);
            hibSession.getTransaction().commit();

        } catch (ConstraintViolationException e) {
            hibSession.getTransaction().rollback();
            logger.error("While using addClusterSet, the clusterSetName conflicted with an existing clusterSet");
            throw new RoutineException(
                    "While using addClusterSet, the clusterSetName conflicted with an existing clusterSet");
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF while using addClusterSet, ", e);
            throw new WTFException("WTF while using addClusterSet");
        } finally {
            hibSession.close();
        }
    }

    public void addNodeType(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        String nodeTypeName = request.getParameter("nodeTypeName");
        String cpu = request.getParameter("cpu");
        String slot = request.getParameter("slot");
        String hs06PerSlot = request.getParameter("hs06PerSlot");
        String memPerNode = request.getParameter("memPerNode");

        NodeType nt = new NodeType();

        try {
            hibSession.beginTransaction();
            nt.setNodeTypeName(nodeTypeName);
            nt.setCpu(Integer.parseInt(cpu));
            nt.setHs06PerSlot(Integer.parseInt(hs06PerSlot));
            nt.setSlot(Integer.parseInt(slot));
            nt.setMemPerNode(Integer.parseInt(memPerNode));

            hibSession.save(nt);
            hibSession.getTransaction().commit();

        } catch (ConstraintViolationException e) {
            hibSession.getTransaction().rollback();
            logger.error("While using addNodeType, the nodeTypeName conflicted with an existing nodeType");
            throw new RoutineException(
                    "While using addNodeType, the nodeTypeName conflicted with an existing nodeType");
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using addNodeType, ", e);
            throw new WTFException("WTF error using addNodeType");
        } finally {
            hibSession.close();
        }
    }

    public void addNodeSet(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        String nodeSetName = request.getParameter("nodeSetName");
        String nodeCount = request.getParameter("nodeCount");
        String nodeTypeName = request.getParameter("nodeTypeList");
        String clusterName = request.getParameter("clusterList");

        NodeSet nodeSet = new NodeSet();

        try {
            hibSession.beginTransaction();
            Cluster c = this.readOneCluster(hibSession, clusterName);

            if (c == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addNodeSet, desired Cluster not found");
                throw new RoutineException("While using addNodeSet, desired Cluster not found");
            }

            NodeType nodeType = this.readOneNodeType(hibSession, nodeTypeName);

            if (nodeType == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addNodeSet, desired NodeType not found");
                throw new RoutineException("While using addNodeSet, desired NodeType not found");
            }
            nodeSet.setCluster(c);
            nodeSet.setNodeType(nodeType);
            nodeSet.setNodeCount(Integer.parseInt(nodeCount));
            nodeSet.setNodeSetName(nodeSetName);
            hibSession.save(nodeSet);
            hibSession.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            hibSession.getTransaction().rollback();
            logger.error("While using addNodeSet, the nodeSetName conflicted with an existing nodeSet");
            throw new RoutineException("While using addNodeSet, the nodeSetName conflicted with an existing nodeSet");
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using addNodeSet, ", e);
            throw new WTFException("WTF error using addNodeSet");
        } finally {
            hibSession.close();
        }
    }

    public void addNode(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        String nodeName = request.getParameter("nodeName");
        String nodeDescription = request.getParameter("nodeDescription");
        String nodeSetName = request.getParameter("nodeSetList");
        String nodeStateName = request.getParameter("nodeStateList");

        Node n = new Node();

        try {
            hibSession.beginTransaction();

            NodeSet nodeSet = this.readOneNodeSet(hibSession, nodeSetName);

            if (nodeSet == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addNode, desired NodeSet not found");
                throw new RoutineException("While using addNode, desired NodeSet not found");
            }

            NodeState nodeState = readOneNodeState(hibSession, nodeStateName);
            if (nodeState == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addNode, desired nodeState not found");
                throw new RoutineException("While using addNode, desired nodeState not found");
            }

            n.setNodeName(nodeName);
            n.setDescription(nodeDescription);
            n.setNodeSet(nodeSet);
            n.setNodeState(nodeState);

            hibSession.save(n);
            hibSession.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            hibSession.getTransaction().rollback();
            logger.error("While using addNode, the nodeName conflicted with an existing node");
            throw new RoutineException("While using addNode, the nodeName conflicted with an existing node");
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using addNode, ", e);
            throw new WTFException("WTF error using addNode");
        } finally {
            hibSession.close();
        }
    }

    public void addServiceNode(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        String hostname = request.getParameter("hostname");
        String cluster = request.getParameter("clusterList");
        String hostSystem = request.getParameter("hostSystemList");
        String cpu = request.getParameter("cpu");
        String mem = request.getParameter("mem");
        String os = request.getParameter("os");
        String kernel = request.getParameter("kernel");
        String service = request.getParameter("service");
        String comment = request.getParameter("comment");

        ServiceNode sn = new ServiceNode();

        try {
            hibSession.beginTransaction();

            Cluster cl = readOneCluster(hibSession, cluster);
            if (cl == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addServiceNode, desired Cluster not found");
                throw new RoutineException("While using addServiceNode, desired Cluster not found");
            }
            HostSystem hs = this.readOneHostSystem(hibSession, hostSystem);

            if (hs == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addServiceNode, desired HostSystem not found");
                throw new RoutineException("While using addServiceNode, desired HostSystem not found");
            }

            sn.setHostname(hostname);
            sn.setCluster(cl);
            sn.setHostSystem(hs);
            sn.setCpu(Integer.parseInt(cpu));
            sn.setMem(Integer.parseInt(mem));
            sn.setOs(os);
            sn.setKernel(kernel);
            sn.setService(service);
            sn.setComment(comment);

            hibSession.save(sn);
            hibSession.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            hibSession.getTransaction().rollback();
            logger.error("While using addServiceNode, the hostname conflicted with an existing service node");
            throw new RoutineException(
                    "While using addServiceNode, the hostname conflicted with an existing service node");
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using addServiceNode, ", e);
            throw new WTFException("WTF error using addServiceNode");
        } finally {
            hibSession.close();
        }
    }

    public void addInstallation(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        String hostname = request.getParameter("serviceNodeList");
        String serviceName = request.getParameter("serviceList");
        String softwareVersion = request.getParameter("softwareVersion");

        Installation installation = new Installation();

        try {
            hibSession.beginTransaction();

            ServiceNode sn = readOneServiceNode(hibSession, hostname);
            if (sn == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addInstallation, desired ServiceNode not found");
                throw new RoutineException("While using addInstallation, desired ServiceNode not found");
            }
            Service s = readOneService(hibSession, serviceName);
            if (s == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addInstallation, desired Service not found");
                throw new RoutineException("While using addInstallation, desired Service not found");
            }

            installation.setService(s);
            installation.setServiceNode(sn);
            installation.setSoftwareVersion(softwareVersion);
            hibSession.save(installation);
            hibSession.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            hibSession.getTransaction().rollback();
            logger.error("While using addInstallation, the new object conflicted with an existing installation");
            throw new RoutineException(
                    "While using addInstallation, the new object conflicted with an existing installation");
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using addInstallation, ", e);
            throw new WTFException("WTF error using addInstallation");
        } finally {
            hibSession.close();
        }
    }

    public void deleteCluster(HttpServletRequest request, String clusterName) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        try {
            Cluster cluster = readOneCluster(hibSession, clusterName);
            hibSession.beginTransaction();
            hibSession.delete(cluster);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteCluster, undesired Cluster not found");
            throw new RoutineException("While using deleteCluster, undesired Cluster not found");
        } finally {
            hibSession.close();
        }
    }

    public void deleteClusterSet(HttpServletRequest request, String clusterSetName)
            throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        ClusterSet clusterSet = readOneClusterSet(hibSession, clusterSetName);
        if (clusterSet == null) {
            logger.error("While using deleteCluster, undesired Cluster not found");
            throw new RoutineException("While using deleteCluster, undesired Cluster not found");
        }
        try {
            hibSession.beginTransaction();
            hibSession.delete(clusterSet);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteClusterSet, undesired ClusterSet not found");
            throw new RoutineException("While using deleteClusterSet, undesired ClusterSet not found");
        } finally {
            hibSession.close();
        }
    }

    public void deleteNodeSet(HttpServletRequest request, String nodeSetName) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        NodeSet nodeSet = readOneNodeSet(hibSession, nodeSetName);
        if (nodeSet == null) {
            logger.error("While using deleteNodeSet, undesired NodeSet not found");
            throw new RoutineException("While using deleteNodeSet, undesired NodeSet not found");
        }
        try {
            hibSession.beginTransaction();
            hibSession.delete(nodeSet);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteNodeSet, undesired NodeSet not found");
            throw new RoutineException("While using deleteNodeSet, undesired NodeSet not found");
        } finally {
            hibSession.close();
        }
    }


    public void deleteNode(HttpServletRequest request, String nodeName) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        Node node = readOneNode(hibSession, nodeName);

        if (node == null) {
            logger.error("While using deleteNode, desired Node not found");
            throw new RoutineException("While using deleteNode, desired Nodenot found");
        }
        try {
            hibSession.beginTransaction();
            hibSession.delete(node);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteNode, desired Node not found");
            throw new RoutineException("While using deleteNode , desired Nodenot found");
        } finally {
            hibSession.close();
        }
    }

    public void deleteServiceNode(HttpServletRequest request, String hostname) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        ServiceNode serviceNode = readOneServiceNode(hibSession, hostname);

        if (serviceNode == null) {
            logger.error("While using deleteServiceNode, desired ServiceNode not found");
            throw new RoutineException("While using deleteServiceNode, desired ServiceNodenot found");
        }
        try {
            hibSession.beginTransaction();
            hibSession.delete(serviceNode);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteServiceNode, desired ServiceNode not found");
            throw new RoutineException("While using deleteServiceNode , desired ServiceNodenot found");
        } finally {
            hibSession.close();
        }
    }

    public void deleteNodeType(HttpServletRequest request, String nodeTypeName) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        NodeType nodeType = readOneNodeType(hibSession, nodeTypeName);
        if (nodeType == null) {
            logger.error("While using deleteNodeType, desired NodeType not found");
            throw new RoutineException("While using deleteNodeType, desired NodeType not found");
        }
        try {
            hibSession.beginTransaction();
            hibSession.delete(nodeType);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteNodeType, desired NodeType not found");
            throw new RoutineException("While using deleteNodeType, desired NodeType not found");
        } finally {
            hibSession.close();
        }
    }

    public void updateCluster(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        try {
            hibSession.beginTransaction();

            // We assume it is updated, whether it is or is not
            String updatedClusterName = request.getParameter("clusterName");
            String updatedDescr = request.getParameter("descr");
            String updatedClusterSetName = request.getParameter("clusterSetList");

            Cluster storedCluster = null;
            Cluster cachedCluster = (Cluster) httpSession.getAttribute("cluster");
            if (cachedCluster == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateCluster, desired Cluster not found");
                throw new RoutineException("While using updateCluster, desired Cluster not found");
            }
            Long cachedClusterVersion = cachedCluster.getVersion();

            storedCluster = readOneCluster(hibSession, updatedClusterName);

            // Possibly deleted during long conversation
            if (storedCluster == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateCluster, desired Cluster  not found");
                throw new RoutineException("While using updateCluster, desired Cluster  not found");
            }
            // Possibly altered during long conversation

            Long storedClusterVersion = storedCluster.getVersion();

            if (!storedClusterVersion.equals(cachedClusterVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateCluster, desired Cluster was altered by another user ");
                throw new RoutineException("While using updateCluster, desired Cluster was altered by another user ");
            }

            // Both the same. Safe to update

            storedCluster.setClusterName(updatedClusterName);
            storedCluster.setDescr(updatedDescr);
            ClusterSet newClusterSet = readOneClusterSet(hibSession, updatedClusterSetName);

            if (newClusterSet == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateCluster, desired ClusterSet  not found");
                throw new RoutineException("While using updateCluster, desired ClusterSet not found");
            }
            storedCluster.setClusterSet(newClusterSet);
            hibSession.update(storedCluster);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using updateClusterSet, ", e);
            throw new WTFException("WTF error using updateClusterSet");
        } finally {
            hibSession.close();
        }
    }

    public void updateNodeSet(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        try {
            hibSession.beginTransaction();

            // We assume it is updated, whether it is or is not
            String updatedNodeSetName = request.getParameter("nodeSetName");
            String updatedNodeCount = request.getParameter("nodeCount");
            String updatedNodeTypeName = request.getParameter("nodeTypeList");
            String updatedClusterName = request.getParameter("clusterList");

            NodeSet storedNodeSet = null;
            NodeSet cachedNodeSet = (NodeSet) httpSession.getAttribute("nodeSet");

            if (cachedNodeSet == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeSet, desired NodeSet not found");
                throw new RoutineException("While using updateNodeSet, desired NodeSet not found");
            }
            Long cachedNodeSetVersion = cachedNodeSet.getVersion();

            storedNodeSet = readOneNodeSet(hibSession, updatedNodeSetName);

            // Possibly deleted during long conversation
            if (storedNodeSet == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeSet, desired NodeSet  not found");
                throw new RoutineException("While using updateNodeSet, desired NodeSet  not found");
            }
            // Possibly altered during long conversation

            Long storedNodeSetVersion = storedNodeSet.getVersion();

            if (!storedNodeSetVersion.equals(cachedNodeSetVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeSet, desired NodeSet was altered by another user ");
                throw new RoutineException("While using updateNodeSet, desired NodeSet was altered by another user ");
            }

            // Both the same. Safe to update

            storedNodeSet.setNodeSetName(updatedNodeSetName);
            storedNodeSet.setNodeCount(Integer.parseInt(updatedNodeCount));

            Cluster cluster = readOneCluster(hibSession, updatedClusterName);
            if (cluster == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeSet, desired Cluster  not found");
                throw new RoutineException("While using updateNodeSet, desired Cluster not found");
            }
            storedNodeSet.setCluster(cluster);

            NodeType nodeType = readOneNodeType(hibSession, updatedNodeTypeName);

            if (nodeType == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeSet, desired NodeType  not found");
                throw new RoutineException("While using updateNodeSet, desired NodeType not found");
            }
            storedNodeSet.setNodeType(nodeType);

            hibSession.update(storedNodeSet);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using updateNodeSet, ", e);
            throw new WTFException("WTF error using updateNodeSet");
        } finally {
            hibSession.close();
        }
    }

    public void updateNode(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        try {
            hibSession.beginTransaction();

            // We assume it is updated, whether it is or is not
            String updatedNodeName = request.getParameter("nodeName");
            String updateDescription = request.getParameter("nodeDescription");
            String updatedNodeSet = request.getParameter("nodeSetList");
            String updatedNodeState = request.getParameter("nodeStateList");

            Node storedNode = null;
            Node cachedNode = (Node) httpSession.getAttribute("node");

            if (cachedNode == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNode, desired Node not found");
                throw new RoutineException("While using updateNode, desired Node not found");
            }
            Long cachedNodeVersion = cachedNode.getVersion();

            storedNode = readOneNode(hibSession, updatedNodeName);

            // Possibly deleted during long conversation
            if (storedNode == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNode, desired Node  not found");
                throw new RoutineException("While using updateNode, desired Node  not found");
            }
            // Possibly altered during long conversation
            Long storedNodeVersion = storedNode.getVersion();

            if (!storedNodeVersion.equals(cachedNodeVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNode, desired Node was altered by another user ");
                throw new RoutineException("While using updateNode, desired Node was altered by another user ");
            }

            // Both the same. Safe to update
            storedNode.setNodeName(updatedNodeName);
            storedNode.setDescription(updateDescription);

            NodeSet nodeSet = readOneNodeSet(hibSession, updatedNodeSet);
            if (nodeSet == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateNode, desired nodeSet  not found");
                throw new RoutineException("While using updateNode, desired nodeSet not found");
            }
            storedNode.setNodeSet(nodeSet);

            NodeState nodeState = readOneNodeState(hibSession, updatedNodeState);
            if (nodeState == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateNode, desired nodeState not found");
                throw new RoutineException("While using updateNode, desired nodeState not found");
            }
            storedNode.setNodeState(nodeState);

            hibSession.update(storedNode);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using updateNode, ", e);
            throw new WTFException("WTF error using updateNode");
        } finally {
            hibSession.close();
        }
    }

    public void updateNodeType(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        try {
            hibSession.beginTransaction();
            String updatedNodeTypeName = request.getParameter("nodeTypeName");
            String updatedCpu = request.getParameter("cpu");
            String updatedSlot = request.getParameter("slot");
            String updatedHs06PerSlot = request.getParameter("hs06PerSlot");
            String updatedMemPerNode = request.getParameter("memPerNode");

            NodeType storedNodeType = null;

            NodeType cachedNodeType = (NodeType) httpSession.getAttribute("nodeType");

            if (cachedNodeType == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeType, desired NodeType not found");
                throw new RoutineException("While using updateNodeType, desired NodeType not found");
            }
            Long cachedNodeTypeVersion = cachedNodeType.getVersion();

            storedNodeType = readOneNodeType(hibSession, updatedNodeTypeName);
            // Possibly deleted during long conversation
            if (storedNodeType == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeType, desired NodeType  not found");
                throw new RoutineException("While using updateNodeType, desired NodeType  not found");
            }
            // Possibly altered during long conversation

            Long storedNodeTypeVersion = storedNodeType.getVersion();

            if (!storedNodeTypeVersion.equals(cachedNodeTypeVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeType, desired NodeType was altered by another user ");
                throw new RoutineException("While using updateNodeType, desired NodeType was altered by another user ");
            }

            // Both the same. Safe to update

            storedNodeType.setCpu(Integer.parseInt(updatedCpu));
            storedNodeType.setSlot(Integer.parseInt(updatedSlot));
            storedNodeType.setHs06PerSlot(Double.parseDouble(updatedHs06PerSlot));
            storedNodeType.setMemPerNode(Double.parseDouble(updatedMemPerNode));

            hibSession.update(storedNodeType);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using updateNodeType, ", e);
            throw new WTFException("WTF error using updateNodeType");
        } finally {
            hibSession.close();
        }
    }

    public void updateClusterSet(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        try {
            hibSession.beginTransaction();

            String updatedClusterSetName = request.getParameter("clusterSetName");
            String updatedDescription = request.getParameter("description");
            String updatedLocation = request.getParameter("location");
            String updatedLongitude = request.getParameter("longitude");
            String updatedLatitude = request.getParameter("latitude");
            String updatedAdmin = request.getParameter("admin");

            ClusterSet storedClusterSet = null;
            ClusterSet cachedClusterSet = (ClusterSet) httpSession.getAttribute("clusterSet");

            if (cachedClusterSet == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateClusterSet, desired ClusterSet not found");
                throw new RoutineException("While using updateClusterSet, desired ClusterSet not found");
            }
            Long cachedClusterSetVersion = cachedClusterSet.getVersion();

            storedClusterSet = readOneClusterSet(hibSession, updatedClusterSetName);

            // Possibly deleted during long conversation
            if (storedClusterSet == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateClusterSet, desired ClusterSet  not found");
                throw new RoutineException("While using updateClusterSet, desired ClusterSet  not found");
            }
            // Possibly altered during long conversation

            Long storedClusterSetVersion = storedClusterSet.getVersion();

            if (!storedClusterSetVersion.equals(cachedClusterSetVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateClusterSet, desired ClusterSet was altered by another user ");
                throw new RoutineException(
                        "While using updateClusterSet, desired ClusterSet was altered by another user ");
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
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using updateClusterSet, ", e);
            throw new WTFException("WTF error using updateClusterSet");
        } finally {
            hibSession.close();
        }
    }

    public void updateServiceNode(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        try {
            hibSession.beginTransaction();

            // We assume it is updated, whether it is or is not

            String updatedHostname = request.getParameter("hostname");
            String updatedCluster = request.getParameter("clusterList");
            String updatedHostSystem = request.getParameter("hostSystemList");
            String updatedCpu = request.getParameter("cpu");
            String updatedMem = request.getParameter("mem");
            String updatedOs = request.getParameter("os");
            String updatedKernel = request.getParameter("kernel");
            String updatedService = request.getParameter("service");
            String updatedComment = request.getParameter("comment");

            ServiceNode storedServiceNode = null;
            ServiceNode cachedServiceNode = (ServiceNode) httpSession.getAttribute("serviceNode");

            if (cachedServiceNode == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateServiceNode, desired ServiceNode not found");
                throw new RoutineException("While using updateServiceNode, desired ServiceNode not found");
            }
            Long cachedServiceNodeVersion = cachedServiceNode.getVersion();

            storedServiceNode = readOneServiceNode(hibSession, updatedHostname);

            // Possibly deleted during long conversation
            if (storedServiceNode == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateServiceNode, desired ServiceNode  not found");
                throw new RoutineException("While using updateServiceNode, desired ServiceNode  not found");
            }
            // Possibly altered during long conversation
            Long storedServiceNodeVersion = storedServiceNode.getVersion();

            if (!storedServiceNodeVersion.equals(cachedServiceNodeVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateServiceNode, desired ServiceNode was altered by another user ");
                throw new RoutineException(
                        "While using updateServiceNode, desired ServiceNode was altered by another user ");
            }

            // Both the same. Safe to update
            storedServiceNode.setHostname(updatedHostname);
            storedServiceNode.setCpu(Integer.parseInt(updatedCpu));
            storedServiceNode.setMem(Integer.parseInt(updatedMem));
            storedServiceNode.setOs(updatedOs);
            storedServiceNode.setKernel(updatedKernel);
            storedServiceNode.setService(updatedService);
            storedServiceNode.setComment(updatedComment);

            Cluster cluster = readOneCluster(hibSession, updatedCluster);
            if (cluster == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateServiceNode, desired Cluster  not found");
                throw new RoutineException("While using updateServiceNode, desired Cluster not found");
            }
            storedServiceNode.setCluster(cluster);

            HostSystem hostSystem = readOneHostSystem(hibSession, updatedHostSystem);
            if (hostSystem == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateServiceNode, desired HostSystem not found");
                throw new RoutineException("While using updateServiceNode, desired HostSystem not found");
            }
            storedServiceNode.setHostSystem(hostSystem);

            hibSession.update(storedServiceNode);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using updateServiceNode, ", e);
            throw new WTFException("WTF error using updateServiceNode");
        } finally {
            hibSession.close();
        }
    }

    public void deleteIndexedInstallation(HttpServletRequest request, Integer installationIndex)
            throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        Installation cachedInstallation = null;
        Installation storedInstallation = null;
        try {
            hibSession.beginTransaction();

            cachedInstallation = ((ArrayList<Installation>) httpSession.getAttribute("installationList"))
                    .get(installationIndex);

            if (cachedInstallation == null) {
                logger.error("While using loadIndexedInstallation, undesired Installation not found");
                throw new RoutineException("While using loadIndexedInstallation, undesired Installation not found");
            }

            Long cachedVersion = cachedInstallation.getVersion();
            String serviceName = cachedInstallation.getService().getServiceName();
            String hostname = cachedInstallation.getServiceNode().getHostname();

            storedInstallation = readOneInstallation(hibSession, serviceName, hostname);

            // Possibly deleted during long conversation
            if (storedInstallation == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using deleteIndexedInstallation, undesired Installation not found");
                throw new RoutineException("While using deleteIndexedInstallation, undesired Installation not found");
            }

            Long storedVersion = storedInstallation.getVersion();
            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error(
                        "While using deleteIndexedInstallation, undesired Installation was altered by another user ");
                throw new RoutineException(
                        "While using deleteIndexedInstallation, undesired Installation was altered by another user ");
            }

            hibSession.delete(storedInstallation);
            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using deleteIndexedInstallation, ", ex);
            throw new WTFException("WTF error using deleteIndexedInstallation");
        } finally {
            hibSession.close();
        }
        return;
    }

    public void loadIndexedInstallation(HttpServletRequest request, Integer installationIndex)
            throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        Installation cachedInstallation = null;
        Installation storedInstallation = null;

        try {
            hibSession.beginTransaction();

            cachedInstallation = ((ArrayList<Installation>) httpSession.getAttribute("installationList"))
                    .get(installationIndex);
            if (cachedInstallation == null) {
                logger.error("While using loadIndexedInstallation, desired Installation not found");
                throw new RoutineException("While using loadIndexedInstallation, desired Installation not found");
            }
            Long cachedVersion = cachedInstallation.getVersion();
            String serviceName = cachedInstallation.getService().getServiceName();
            String hostname = cachedInstallation.getServiceNode().getHostname();

            storedInstallation = readOneInstallation(hibSession, serviceName, hostname);

            // Possibly deleted during long conversation
            if (storedInstallation == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedInstallation, desired Installation not found");
                throw new RoutineException("While using loadIndexedInstallation, desired Installation not found");
            }
            Long storedVersion = storedInstallation.getVersion();

            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedInstallation, desired Installation was altered by another user ");
                throw new RoutineException(
                        "While using loadIndexedInstallation, desired Installation was altered by another user ");
            }

            hibSession.getTransaction().commit();
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadIndexedInstallation, ", e);
            throw new WTFException("WTF error using loadIndexedInstallation");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("installation", storedInstallation);
    }

    public void updateInstallation(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        try {
            hibSession.beginTransaction();

            // We assume it is updated, whether it is or is not
            String updatedService = request.getParameter("service");
            String updatedServiceNode = request.getParameter("serviceNode");
            String updatedSoftwareVersion = request.getParameter("softwareVersion");

            Installation storedInstallation = null;
            Installation cachedInstallation = (Installation) httpSession.getAttribute("installation");

            if (cachedInstallation == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateInstallation, desired Installation not found");
                throw new RoutineException("While using updateInstallation, desired Installation not found");
            }
            Long cachedInstallationVersion = cachedInstallation.getVersion();
            String serviceName = cachedInstallation.getService().getServiceName();
            String hostname = cachedInstallation.getServiceNode().getHostname();

            storedInstallation = readOneInstallation(hibSession, serviceName, hostname);

            // Possibly deleted during long conversation
            if (storedInstallation == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateInstallation, desired Installation  not found");
                throw new RoutineException("While using updateInstallation, desired Installation  not found");
            }
            // Possibly altered during long conversation

            Long storedInstallationVersion = storedInstallation.getVersion();

            if (!storedInstallationVersion.equals(cachedInstallationVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateInstallation, desired Installation was altered by another user ");
                throw new RoutineException(
                        "While using updateInstallation, desired Installation was altered by another user ");
            }

            // Both the same. Safe to update

            storedInstallation.setSoftwareVersion(updatedSoftwareVersion);

            hibSession.update(storedInstallation);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using updateInstallation, ", e);
            throw new WTFException("WTF error using updateInstallation");
        } finally {
            hibSession.close();
        }
    }

    public void loadClustersOfClusterSet(HttpServletRequest request, String clusterSetName, String col, String order)
            throws RoutineException, WTFException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        List<Cluster> clusterList = null;

        try {
            hibSession.beginTransaction();

            clusterList = readClustersOfClusterSet(hibSession, clusterSetName, col, order);

            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadClustersOfClusterSet, ", ex);
            throw new WTFException("WTF error using loadClustersOfClusterSet");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("clusterList", clusterList);
    }

    public Cluster readOneCluster(Session hibSession, String clusterName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Cluster> q = cb.createQuery(Cluster.class);
        Root<Cluster> root = q.from(Cluster.class);
        q.select(root).where(cb.equal(root.get("clusterName"), clusterName));
        return hibSession.createQuery(q).getSingleResult();
    }

    public ClusterSet readOneClusterSet(Session hibSession, String clusterSetName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<ClusterSet> q = cb.createQuery(ClusterSet.class);
        Root<ClusterSet> root = q.from(ClusterSet.class);
        q.select(root).where(cb.equal(root.get("clusterSetName"), clusterSetName));
        return hibSession.createQuery(q).getSingleResult();
    }

    public ServiceNode readOneServiceNode(Session hibSession, String hostname) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<ServiceNode> q = cb.createQuery(ServiceNode.class);
        Root<ServiceNode> root = q.from(ServiceNode.class);
        q.select(root).where(cb.equal(root.get("hostname"), hostname));
        return hibSession.createQuery(q).getSingleResult();
    }

    public HostSystem readOneHostSystem(Session hibSession, String hostname) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<HostSystem> q = cb.createQuery(HostSystem.class);
        Root<HostSystem> root = q.from(HostSystem.class);
        q.select(root).where(cb.equal(root.get("hostname"), hostname));
        return hibSession.createQuery(q).getSingleResult();
    }

    public Service readOneService(Session hibSession, String serviceName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Service> q = cb.createQuery(Service.class);
        Root<Service> root = q.from(Service.class);
        q.select(root).where(cb.equal(root.get("serviceName"), serviceName));
        return hibSession.createQuery(q).getSingleResult();
    }

    public NodeState readOneNodeState(Session hibSession, String nodeStateName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<NodeState> q = cb.createQuery(NodeState.class);
        Root<NodeState> root = q.from(NodeState.class);
        q.select(root).where(cb.equal(root.get("nodeStateName"), nodeStateName));
        return hibSession.createQuery(q).getSingleResult();
    }

    public NodeType readOneNodeType(Session hibSession, String nodeTypeName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<NodeType> q = cb.createQuery(NodeType.class);
        Root<NodeType> root = q.from(NodeType.class);
        q.select(root).where(cb.equal(root.get("nodeTypeName"), nodeTypeName));
        return hibSession.createQuery(q).getSingleResult();
    }

    public NodeSet readOneNodeSet(Session hibSession, String nodeSetName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<NodeSet> q = cb.createQuery(NodeSet.class);
        Root<NodeSet> root = q.from(NodeSet.class);
        q.select(root).where(cb.equal(root.get("nodeSetName"), nodeSetName));
        return hibSession.createQuery(q).getSingleResult();
    }

    public Node readOneNode(Session hibSession, String nodeName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Node> q = cb.createQuery(Node.class);
        Root<Node> root = q.from(Node.class);
        q.select(root).where(cb.equal(root.get("nodeName"), nodeName));
        return hibSession.createQuery(q).getSingleResult();

    }

    public Installation readOneInstallation(Session hibSession, String serviceName, String hostname) {

        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Installation> query = cb.createQuery(Installation.class);

        Root<Installation> installatione = query.from(Installation.class);
        Join<Installation, ServiceNode> serviceNodeJoin = installatione.join("serviceNode");
        Join<Installation, Service> serviceJoin = installatione.join("service");

        List<Predicate> conditions = new ArrayList<Predicate>();
        conditions.add(cb.equal(serviceNodeJoin.get("hostname"), hostname));
        conditions.add(cb.equal(serviceJoin.get("serviceName"), serviceName));

        TypedQuery<Installation> typedQuery = hibSession
                .createQuery(query.select(installatione).where(conditions.toArray(new Predicate[conditions.size()])));

        Installation i = typedQuery.getSingleResult();
        return i;
    }

    public List<Cluster> readClusterList(Session hibSession, String col, String order) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Cluster> cq = cb.createQuery(Cluster.class);
        cq.distinct(true);
        Root<Cluster> c = cq.from(Cluster.class);
        if (order.equalsIgnoreCase("ASC"))
            cq.orderBy(cb.asc(c.get(col)));
        else
            cq.orderBy(cb.desc(c.get(col)));
        cq.select(c);
        TypedQuery<Cluster> q = hibSession.createQuery(cq);
        List<Cluster> clusterList = q.getResultList();
        return clusterList;
    }

    public List<HostSystem> readHostSystemList(Session hibSession, String col, String order) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<HostSystem> cq = cb.createQuery(HostSystem.class);
        cq.distinct(true);
        Root<HostSystem> c = cq.from(HostSystem.class);
        if (order.equalsIgnoreCase("ASC"))
            cq.orderBy(cb.asc(c.get(col)));
        else
            cq.orderBy(cb.desc(c.get(col)));
        cq.select(c);
        TypedQuery<HostSystem> q = hibSession.createQuery(cq);
        List<HostSystem> clusterList = q.getResultList();
        return clusterList;
    }

    public List<NodeState> readNodeStateList(Session hibSession, String col, String order) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<NodeState> cq = cb.createQuery(NodeState.class);
        cq.distinct(true);
        Root<NodeState> c = cq.from(NodeState.class);
        if (order.equalsIgnoreCase("ASC"))
            cq.orderBy(cb.asc(c.get(col)));
        else
            cq.orderBy(cb.desc(c.get(col)));
        cq.select(c);
        TypedQuery<NodeState> q = hibSession.createQuery(cq);
        List<NodeState> theList = q.getResultList();
        return theList;
    }

    public List<Node> readNodeList(Session hibSession, String col, String order) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Node> cq = cb.createQuery(Node.class);
        cq.distinct(true);
        Root<Node> c = cq.from(Node.class);
        if (order.equalsIgnoreCase("ASC"))
            cq.orderBy(cb.asc(c.get(col)));
        else
            cq.orderBy(cb.desc(c.get(col)));
        cq.select(c);
        TypedQuery<Node> q = hibSession.createQuery(cq);
        List<Node> theList = q.getResultList();
        return theList;
    }

    public List<NodeType> readNodeTypeList(Session hibSession, String col, String order) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<NodeType> cq = cb.createQuery(NodeType.class);
        cq.distinct(true);
        Root<NodeType> c = cq.from(NodeType.class);
        if (order.equalsIgnoreCase("ASC"))
            cq.orderBy(cb.asc(c.get(col)));
        else
            cq.orderBy(cb.desc(c.get(col)));
        cq.select(c);
        TypedQuery<NodeType> q = hibSession.createQuery(cq);
        List<NodeType> theList = q.getResultList();
        return theList;
    }

    public List<NodeSet> readNodeSetList(Session hibSession, String col, String order) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<NodeSet> cq = cb.createQuery(NodeSet.class);
        cq.distinct(true);
        Root<NodeSet> c = cq.from(NodeSet.class);
        if (order.equalsIgnoreCase("ASC"))
            cq.orderBy(cb.asc(c.get(col)));
        else
            cq.orderBy(cb.desc(c.get(col)));
        cq.select(c);
        TypedQuery<NodeSet> q = hibSession.createQuery(cq);
        List<NodeSet> theList = q.getResultList();
        return theList;
    }

    public List<ClusterSet> readClusterSetList(Session hibSession, String col, String order) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<ClusterSet> cq = cb.createQuery(ClusterSet.class);
        cq.distinct(true);
        Root<ClusterSet> c = cq.from(ClusterSet.class);
        if (order.equalsIgnoreCase("ASC"))
            cq.orderBy(cb.asc(c.get(col)));
        else
            cq.orderBy(cb.desc(c.get(col)));
        cq.select(c);
        TypedQuery<ClusterSet> q = hibSession.createQuery(cq);
        List<ClusterSet> theList = q.getResultList();
        return theList;
    }

    public List<Installation> readInstallationList(Session hibSession, String col, String order) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Installation> cq = cb.createQuery(Installation.class);
        cq.distinct(true);
        Root<Installation> c = cq.from(Installation.class);
        if (order.equalsIgnoreCase("ASC"))
            cq.orderBy(cb.asc(c.get(col)));
        else
            cq.orderBy(cb.desc(c.get(col)));
        cq.select(c);
        TypedQuery<Installation> q = hibSession.createQuery(cq);
        List<Installation> theList = q.getResultList();
        return theList;
    }

    public List<ServiceNode> readServiceNodeList(Session hibSession, String col, String order) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<ServiceNode> cq = cb.createQuery(ServiceNode.class);
        cq.distinct(true);
        Root<ServiceNode> c = cq.from(ServiceNode.class);
        if (order.equalsIgnoreCase("ASC"))
            cq.orderBy(cb.asc(c.get(col)));
        else
            cq.orderBy(cb.desc(c.get(col)));
        cq.select(c);
        TypedQuery<ServiceNode> q = hibSession.createQuery(cq);
        List<ServiceNode> theList = q.getResultList();
        return theList;
    }

    public List<Service> readServiceList(Session hibSession, String col, String order) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Service> cq = cb.createQuery(Service.class);
        cq.distinct(true);
        Root<Service> c = cq.from(Service.class);
        if (order.equalsIgnoreCase("ASC"))
            cq.orderBy(cb.asc(c.get(col)));
        else
            cq.orderBy(cb.desc(c.get(col)));
        cq.select(c);
        TypedQuery<Service> q = hibSession.createQuery(cq);
        List<Service> theList = q.getResultList();
        return theList;
    }
    
    public List<Cluster> readClustersOfClusterSet(Session hibSession, String clusterSetName, String col, String order) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Cluster> q = cb.createQuery(Cluster.class);
        Root<Cluster> cRoot = q.from(Cluster.class);
        Root<ClusterSet> csRoot = q.from(ClusterSet.class);
        Join<ClusterSet, Cluster> clusters = csRoot.join("clusters");
        q.distinct(true);
        if (order.equalsIgnoreCase("ASC"))
            q.orderBy(cb.asc(cRoot.get(col)));
        else
            q.orderBy(cb.desc(cRoot.get(col)));
        q.select(clusters).where(cb.equal(csRoot.get("clusterSetName"), clusterSetName));
        List<Cluster> clusterList = hibSession.createQuery(q).getResultList();
        return clusterList ;
    }

    public void logout(HttpServletRequest request) {
        HttpSession httpSession = null;

        httpSession = request.getSession();
        httpSession.invalidate();
        return;
    }
}


