package com.basingwerk.sldb.mvc.dbfacade;

import org.hibernate.criterion.Order;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.exceptions.ConflictException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.Cluster;
import com.basingwerk.sldb.mvc.model.ClusterSet;
import com.basingwerk.sldb.mvc.model.NodeSet;
import com.basingwerk.sldb.mvc.model.NodeSetNodeTypeJoin;
import com.basingwerk.sldb.mvc.model.NodeType;

public class DbFacade {
    final static Logger logger = Logger.getLogger(DbFacade.class);

    public DbFacade() {
    }

    public static void addCluster(HttpServletRequest request) throws ConflictException, WTFException {
        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        String clusterName = request.getParameter("clusterName");
        String descr = request.getParameter("descr");
        String clusterSetName = request.getParameter("clusterSetList");
        
        Cluster cluster = new Cluster();
        ClusterSet chosenClusterSet = null;

        try {
            hibSession.beginTransaction();

            chosenClusterSet = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
                    .add(Restrictions.eq("clusterSetName", clusterSetName)).uniqueResult();

            if (chosenClusterSet == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addCluster, desired ClusterSet not found");
                throw new ConflictException("While using addCluster, desired ClusterSet not found");
            }

            cluster.setClusterName(clusterName);
            cluster.setDescr(descr);
            cluster.setClusterSet(chosenClusterSet);

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

    public static void addClusterSet(HttpServletRequest request) throws WTFException, ConflictException {

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

    public static void addNodeSet(HttpServletRequest request) throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        String nodeSetName = request.getParameter("nodeSetName");
        String nodeCount = request.getParameter("nodeCount");
        String nodeTypeName = request.getParameter("nodeTypeList");
        String clusterName = request.getParameter("clusterList");

        NodeSet nodeSet = new NodeSet();

        try {
            hibSession.beginTransaction();

            Cluster c = (Cluster) hibSession.createCriteria(Cluster.class)
                    .add(Restrictions.eq("clusterName", clusterName)).uniqueResult();

            if (c == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addNodeSet, desired Cluster not found");
                throw new ConflictException("While using addNodeSet, desired Cluster not found");
            }

            NodeType n = (NodeType) hibSession.createCriteria(NodeType.class)
                    .add(Restrictions.eq("nodeTypeName", nodeTypeName)).uniqueResult();
            if (n == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addNodeSet, desired NodeType not found");
                throw new ConflictException("While using addNodeSet, desired NodeType not found");
            }
            nodeSet.setCluster(c);
            nodeSet.setNodeType(n);
            nodeSet.setNodeCount(Integer.parseInt(nodeCount));
            nodeSet.setNodeSetName(nodeSetName);

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

    public static void addNodeType(HttpServletRequest request) throws WTFException, ConflictException {

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

    public static void deleteCluster(HttpServletRequest request, String clusterName)
            throws WTFException, ConflictException {
        
        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();
        
        try {
            hibSession.beginTransaction();
            Cluster c = (Cluster) hibSession.createCriteria(Cluster.class)
                    .add(Restrictions.eq("clusterName", clusterName)).uniqueResult();
            hibSession.delete(c);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteCluster, desired Cluster not found");
            throw new ConflictException("While using deleteCluster, desired Cluster not found");
        } finally {
            hibSession.close();
        }
    }

    public static void deleteClusterSet(HttpServletRequest request, String clusterSetName)
            throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();
        
        try {
            hibSession.beginTransaction();
            ClusterSet cs = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
                    .add(Restrictions.eq("clusterSetName", clusterSetName)).uniqueResult();
            hibSession.delete(cs);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteClusterSet, desired ClusterSet not found");
            throw new ConflictException("While using deleteClusterSet, desired ClusterSet not found");
        } finally {
            hibSession.close();
        }
    }

    public static void deleteNodeSet(HttpServletRequest request, String nodeSetName)
            throws WTFException, ConflictException {
        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();
        try {
            hibSession.beginTransaction();
            NodeSet ns = (NodeSet) hibSession.createCriteria(NodeSet.class)
                    .add(Restrictions.eq("nodeSetName", nodeSetName)).uniqueResult();
            hibSession.delete(ns);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteClusterSet, desired NodeSet not found");
            throw new ConflictException("While using deleteClusterSet, desired NodeSet not found");
        } finally {
            hibSession.close();
        }
    }

    public static void deleteNodeType(HttpServletRequest request, String nodeTypeName)
            throws WTFException, ConflictException {
        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();
        try {
            hibSession.beginTransaction();
            NodeType nt = (NodeType) hibSession.createCriteria(NodeType.class)
                    .add(Restrictions.eq("nodeTypeName", nodeTypeName)).uniqueResult();
            hibSession.delete(nt);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteNodeType, desired NodeType not found");
            throw new ConflictException("While using deleteNodeType, desired NodeType not found");
        } finally {
            hibSession.close();
        }
    }

    public static void loadClusters(HttpServletRequest request, String col, String order) throws WTFException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        Order ord = org.hibernate.criterion.Order.desc(col);
        if (order.equalsIgnoreCase("ASC")) {
            ord = org.hibernate.criterion.Order.asc(col);
        }

        ArrayList<Cluster> list = null;
        try {

            hibSession.beginTransaction();
            list = (ArrayList<Cluster>) hibSession.createCriteria(Cluster.class).addOrder(ord).list();
            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WHT error using refreshListOfAllClusters, ", ex);
            throw new WTFException("WHT error using refreshListOfAllClusters");
        } finally {
            hibSession.close();
        }

        //httpSession.setAttribute("clusterList", list);
        request.setAttribute("clusterList", list);
    }

    public static void loadClusterSets(HttpServletRequest request, String col, String order) throws WTFException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        Order ord = org.hibernate.criterion.Order.desc(col);
        if (order.equalsIgnoreCase("ASC")) {
            ord = org.hibernate.criterion.Order.asc(col);
        }
        ArrayList<ClusterSet> list = null;
        try {

            hibSession.beginTransaction();
            list = (ArrayList<ClusterSet>) hibSession.createCriteria(ClusterSet.class).addOrder(ord).list();
            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had a problem when using refreshClusterSets, ", ex);
            throw new WTFException("Had a problem when using refreshClusterSets");
        } finally {
            hibSession.close();
        }
        //httpSession.setAttribute("clusterSetList", list);
        request.setAttribute("clusterSetList", list);
    }

    public static void loadNodeSets(HttpServletRequest request, String col, String order) throws WTFException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        Order ord = org.hibernate.criterion.Order.desc(col);
        if (order.equalsIgnoreCase("ASC")) {
            ord = org.hibernate.criterion.Order.asc(col);
        }

        ArrayList<NodeSet> list = null;
        try {

            hibSession.beginTransaction();
            list = (ArrayList<NodeSet>) hibSession.createCriteria(NodeSet.class).addOrder(ord).list();
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error when trying to refresh node sets, ", ex);
            throw new WTFException("Had an error when trying to refresh node sets");
        } finally {
            hibSession.close();
        }
        //httpSession.setAttribute("nodeSetList", list);
        request.setAttribute("nodeSetList", list);
    }

    public static void loadNodeTypes(HttpServletRequest request, String col, String order) throws WTFException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        Order ord = org.hibernate.criterion.Order.desc(col);
        if (order.equalsIgnoreCase("ASC")) {
            ord = org.hibernate.criterion.Order.asc(col);
        }

        ArrayList<NodeType> list = null;
        try {

            hibSession.beginTransaction();
            list = (ArrayList<NodeType>) hibSession.createCriteria(NodeType.class).addOrder(ord).list();
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error when using refreshNodeTypes, ", ex);
            throw new WTFException("Had an error when using refreshNodeTypes");
        } finally {
            hibSession.close();
        }

        //httpSession.setAttribute("nodeTypeList", list);
        request.setAttribute("nodeTypeList", list);
    }

    public static void loadNamedCluster(HttpServletRequest request, String cluster)
            throws WTFException, ConflictException {
        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();
        Cluster c = null;
        try {
            hibSession.beginTransaction();
            c = (Cluster) hibSession.createCriteria(Cluster.class).add(Restrictions.eq("clusterName", cluster))
                    .uniqueResult();
            if (c == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using loadNamedCluster, desired Cluster not found");
                throw new ConflictException("While using loadNamedCluster, desired Cluster not found");
            }
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WHT error using setSingleCluster, ", e);
            throw new WTFException("WHT error using setSingleCluster");
        } finally {
            hibSession.close();
        }

        //httpSession.setAttribute("cluster", c);
        request.setAttribute("cluster", c);
        return;
    }

    public static void loadNamedClusterSet(HttpServletRequest request, String clusterSetName)
            throws WTFException, ConflictException {
        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();
        ClusterSet cs = null;
        try {
            hibSession.beginTransaction();
            cs = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
                    .add(Restrictions.eq("clusterSetName", clusterSetName)).uniqueResult();
            if (cs == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using loadNamedClusterSet, desired ClusterSet not found");
                throw new ConflictException("While using loadNamedClusterSet, desired ClusterSet not found");
            }
            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WHT error using loadNamedClusterSet, ", ex);
            throw new WTFException("WHT error using loadNamedClusterSet");
        } finally {
            hibSession.close();
        }
        //httpSession.setAttribute("clusterSet", cs);
        request.setAttribute("clusterSet", cs);

        return;

    }

    public static void loadNamedNodeSet(HttpServletRequest request, String nodeSetName)
            throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();
        NodeSet ns = null;
        try {
            hibSession.beginTransaction();
            ns = (NodeSet) hibSession.createCriteria(NodeSet.class).add(Restrictions.eq("nodeSetName", nodeSetName))
                    .uniqueResult();
            if (ns == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using loadNamedNodeSet, desired NodeSet not found");
                throw new ConflictException("While using loadNamedNodeSet, desired NodeSet not found");
            }
            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WHT error using loadNamedNodeSet, ", ex);
            throw new WTFException("WHT error using loadNamedNodeSet");
        } finally {
            hibSession.close();
        }
        //httpSession.setAttribute("nodeSet", ns);
        request.setAttribute("nodeSet", ns);
        return;

    }

    public static void loadNamedNodeType(HttpServletRequest request, String nodeTypeName)
            throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();
        NodeType nt = null;
        try {
            hibSession.beginTransaction();
            nt = (NodeType) hibSession.createCriteria(NodeType.class).add(Restrictions.eq("nodeTypeName", nodeTypeName))
                    .uniqueResult();
            if (nt == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using loadNamedNodeType, desired NodeType not found");
                throw new ConflictException("While using loadNamedNodeType, desired NodeType not found");
            }

            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WHT error using loadNamedNodeType, ", ex);
            throw new WTFException("WHT error using loadNamedNodeType");
        } finally {
            hibSession.close();
        }
        //httpSession.setAttribute("nodeType", nt);
        request.setAttribute("nodeType", nt);
        return;
    }

  public static void updateCluster(HttpServletRequest request) throws WTFException, ConflictException {

      HttpSession httpSession = request.getSession();
      Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

      String clusterName = request.getParameter("clusterName");
      String descr = request.getParameter("descr");
      String clusterSetName = request.getParameter("clusterSetList");

      Cluster cluster = null;

      try {
          hibSession.beginTransaction();

          cluster = (Cluster) hibSession.createCriteria(Cluster.class)
                  .add(Restrictions.eq("clusterName", clusterName)).uniqueResult();
          if (cluster == null) {
              // Possibly deleted during long conversation
              hibSession.getTransaction().rollback();
              logger.error("While using updateCluster, desired Cluster  not found");
              throw new ConflictException("While using updateCluster, desired Cluster  not found");
          }

          cluster.setClusterName(clusterName);
          cluster.setDescr(descr);
          if (!cluster.getClusterSet().getClusterSetName().equals(clusterSetName)) {
              ClusterSet desiredClusterSet = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
                      .add(Restrictions.eq("clusterSetName", clusterSetName)).uniqueResult();
              if (desiredClusterSet == null) {
                  // Possibly deleted during long conversation
                  hibSession.getTransaction().rollback();
                  logger.error("While using updateCluster, desired ClusterSet  not found");
                  throw new ConflictException("While using updateCluster, desired ClusterSet not found");
              }
              cluster.setClusterSet(desiredClusterSet);
          }
          hibSession.update(cluster);
          hibSession.getTransaction().commit();
      } catch (HibernateException e) {
          hibSession.getTransaction().rollback();
          logger.error("WTF error using updateClusterSet, ", e);
          throw new WTFException("WTH error using updateClusterSet");
      } finally {
          hibSession.close();
      }

  }
    

    public static void updateClusterSet(HttpServletRequest request) throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        String clusterSetName = request.getParameter("clusterSetName");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");
        String admin = request.getParameter("admin");

        ClusterSet clusterSet = null;

        try {
            hibSession.beginTransaction();

            clusterSet = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
                    .add(Restrictions.eq("clusterSetName", clusterSetName)).uniqueResult();
            if (clusterSet == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateClusterSet, desired ClusterSet  not found");
                throw new ConflictException("While using updateClusterSet, desired ClusterSet  not found");
            }

            clusterSet.setDescription(description);
            clusterSet.setLocation(location);
            clusterSet.setLongitude(Double.parseDouble(longitude));
            clusterSet.setLatitude(Double.parseDouble(latitude));
            clusterSet.setAdmin(admin);

            hibSession.update(clusterSet);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WHT error using updateClusterSet, ", e);
            throw new WTFException("WHT error using updateClusterSet");
        } finally {
            hibSession.close();
        }

    }

    public static void updateNodeSet(HttpServletRequest request) throws WTFException, ConflictException {

        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        String nodeSetName = request.getParameter("nodeSetName");
        String nodeCount = request.getParameter("nodeCount");
        String nodeTypeName = request.getParameter("nodeTypeList");
        String clusterName = request.getParameter("clusterList");

        NodeSet ns = null;

        try {
            hibSession.beginTransaction();

            ns = (NodeSet) hibSession.createCriteria(NodeSet.class)
                    .add(Restrictions.eq("nodeSetName", nodeSetName)).uniqueResult();
            if (ns == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeSet, desired NodeSet  not found");
                throw new ConflictException("While using updateNodeSet, desired NodeSet  not found");
            } 
            ns.setNodeCount(Integer.parseInt(nodeCount));

            if (!ns.getNodeType().getNodeTypeName().equals(nodeTypeName)) {
                NodeType newNodeType = (NodeType) hibSession.createCriteria(NodeType.class)
                        .add(Restrictions.eq("nodeTypeName", nodeTypeName)).uniqueResult();
                if (newNodeType == null) {
                    hibSession.getTransaction().rollback();
                    logger.error("While using updateNodeSet, desired NodeType  not found");
                    throw new ConflictException("While using updateNodeSet, desired NodeType  not found");
                }
                ns.setNodeType(newNodeType);
            }
            if (!ns.getCluster().getClusterName().equals(clusterName)) {
                Cluster newCluster = (Cluster) hibSession.createCriteria(Cluster.class)
                        .add(Restrictions.eq("clusterName", clusterName)).uniqueResult();
                if (newCluster == null) {
                    hibSession.getTransaction().rollback();
                    logger.error("While using updateNodeSet, desired Cluster  not found");
                    throw new ConflictException("While using updateNodeSet, desired Cluster  not found");
                }
                ns.setCluster(newCluster);
            }

            hibSession.update(ns);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WHT error using updateNodeSet, ", e);
            throw new WTFException("WHT error using updateNodeSet");
        } finally {
            hibSession.close();
        }
    }

    public static void updateNodeType(HttpServletRequest request) throws WTFException, ConflictException {
        HttpSession httpSession = request.getSession();
        Session hibSession = ((SessionFactory) httpSession.getAttribute("sessionFactory")).openSession();

        String nodeTypeName = request.getParameter("nodeTypeName");
        String cpu = request.getParameter("cpu");
        String slot = request.getParameter("slot");
        String hs06PerSlot = request.getParameter("hs06PerSlot");
        String memPerNode = request.getParameter("memPerNode");

        NodeType nodeType = null;

        try {
            hibSession.beginTransaction();
            nodeType = (NodeType) hibSession.createCriteria(NodeType.class)
                    .add(Restrictions.eq("nodeTypeName", nodeTypeName)).uniqueResult();
            if (nodeType == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeType, desired NodeType  not found");
                throw new ConflictException("While using updateNodeType, desired NodeType  not found");
            } 

            nodeType.setCpu(Integer.parseInt(cpu));
            nodeType.setSlot(Integer.parseInt(slot));
            nodeType.setHs06PerSlot(Double.parseDouble(hs06PerSlot));
            nodeType.setMemPerNode(Double.parseDouble(memPerNode));

            hibSession.update(nodeType);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WHT error using updateNodeType, ", e);
            throw new WTFException("WHT error using updateNodeType");
        } finally {
            hibSession.close();
        }
    }

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
            logger.error("WHT error using addNodeSet, ", e);
            throw new WTFException("WHT error using addNodeSet");
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
}
