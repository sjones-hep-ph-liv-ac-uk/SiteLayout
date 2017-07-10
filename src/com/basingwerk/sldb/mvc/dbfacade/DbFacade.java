package com.basingwerk.sldb.mvc.dbfacade;

import org.hibernate.criterion.Order;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.basingwerk.sldb.mvc.exceptions.DbFacadeException;
import com.basingwerk.sldb.mvc.model.Cluster;
import com.basingwerk.sldb.mvc.model.ClusterSet;
import com.basingwerk.sldb.mvc.model.NodeSet;
import com.basingwerk.sldb.mvc.model.NodeSetNodeTypeJoin;
import com.basingwerk.sldb.mvc.model.NodeType;

public class DbFacade {
    final static Logger logger = Logger.getLogger(DbFacade.class);

    public DbFacade() {
    }

    public static void refreshClusterSets(HttpServletRequest request, String col, String order) throws HibernateException {

        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");
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
            throw ex;
        }
        request.setAttribute("clusterSetList", list);
    }

    public static void refreshNodeTypes(HttpServletRequest request, String col, String order) throws HibernateException {

        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

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
            throw ex;
        }
        request.setAttribute("nodeTypeList", list);
    }

    public static void refreshClusters(HttpServletRequest request, String col, String order) throws HibernateException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

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
            logger.error("Hibernate error using refreshListOfAllClusters, ", ex);
            throw ex;
        }
        request.setAttribute("clusterList", list);
    }

    public static void setBaselineNodeType(HttpServletRequest request) throws HibernateException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

        ArrayList<NodeType> list = null;
        try {

            hibSession.beginTransaction();
            list = (ArrayList<NodeType>) hibSession.createCriteria(NodeType.class).list();
            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error using setBaselineNodeType, ", ex);
            throw ex;
        }

        request.setAttribute("baseline", null);
        for (NodeType n : list) {
            if (n.getNodeTypeName().toUpperCase().startsWith("BASELINE")) {
                request.setAttribute("baseline", n);
            }
        }
    }

    public static ArrayList<String> getClustersOfClusterSet(HttpServletRequest request, String clusterSetName) throws HibernateException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

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
            throw ex;
        }
        return cl;
    }

    public static ClusterSet queryOneClusterSet(HttpServletRequest request, String clusterSetName)
            throws HibernateException  {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

        try {
            hibSession.beginTransaction();
            ClusterSet ns = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
                    .add(Restrictions.eq("clusterSetName", clusterSetName)).uniqueResult();
            hibSession.getTransaction().commit();
                return ns;

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using queryOneClusterSet, ", ex);
            throw ex;
        }
    }

    public static void updateClusterSet(HttpServletRequest request) throws HibernateException , DbFacadeException {

        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

        String clusterSetName = request.getParameter("clusterSetName");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");
        String admin = request.getParameter("admin");

        ClusterSet existing = null;

        try {
            hibSession.beginTransaction();

            List<ClusterSet> csl = hibSession.createCriteria(ClusterSet.class)
                    .add(Restrictions.eq("clusterSetName", clusterSetName)).list();
            if (csl.size() != 1) {
                hibSession.getTransaction().rollback();
                logger.error("Had a WTF error using updateClusterSet");
                throw new DbFacadeException("Had a WTF error using updateClusterSet");
            } else {
                existing = csl.get(0);
            }

            existing.setDescription(description);
            existing.setLocation(location);
            existing.setLongitude(Double.parseDouble(longitude));
            existing.setLatitude(Double.parseDouble(latitude));
            existing.setAdmin(admin);

            hibSession.update(existing);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using updateClusterSet, ", e);
            throw e;
        }
    }

    public static void addClusterSet(HttpServletRequest request) throws HibernateException , DbFacadeException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

        String clusterSetName = request.getParameter("clusterSetName");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");
        String admin = request.getParameter("admin");

        ClusterSet existing = new ClusterSet();

        try {
            hibSession.beginTransaction();

            existing.setClusterSetName(clusterSetName);
            existing.setDescription(description);
            existing.setLocation(location);
            existing.setLongitude(Double.parseDouble(longitude));
            existing.setLatitude(Double.parseDouble(latitude));
            existing.setAdmin(admin);

            hibSession.save(existing);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using addClusterSet, ", e);
            throw e;
        }

    }

    public static ArrayList<ClusterSet> queryClusterSetList(HttpServletRequest request) throws HibernateException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

        ArrayList<ClusterSet> csl = null;

        try {
            hibSession.beginTransaction();

            csl = (ArrayList<ClusterSet>) hibSession.createCriteria(ClusterSet.class).list();
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using queryClusterSetList, ", e);
            throw e;
        }
        return csl;
    }

    public static ArrayList<String> listAllClusterSets(HttpServletRequest request) throws HibernateException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

        ArrayList<ClusterSet> csl = null;

        try {
            hibSession.beginTransaction();

            csl = (ArrayList<ClusterSet>) hibSession.createCriteria(ClusterSet.class).list();
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using queryClusterSetList, ", e);
            throw e;
        }
        ArrayList<String> s = new ArrayList<String>();
        for (ClusterSet c : csl) {
            s.add(c.getClusterSetName());
        }
        return s;
    }

    public static void deleteClusterSet(HttpServletRequest request, String clusterSetName) throws HibernateException , DbFacadeException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");
        try {
            hibSession.beginTransaction();
            ClusterSet ns = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
                    .add(Restrictions.eq("clusterSetName", clusterSetName)).uniqueResult();
            hibSession.delete(ns);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using deleteClusterSet, ", ex);
            throw ex;
        }
    }

    public static void deleteCluster(HttpServletRequest request, String clusterName) throws HibernateException, DbFacadeException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");
        try {
            hibSession.beginTransaction();
            Cluster c = (Cluster) hibSession.createCriteria(Cluster.class)
                    .add(Restrictions.eq("clusterName", clusterName)).uniqueResult();
            hibSession.delete(c);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using deleteCluster, ", ex);
            throw ex;
        }
    }

    public static void addCluster(HttpServletRequest request) throws HibernateException , DbFacadeException {

        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

        String clusterName = request.getParameter("clusterName");
        String descr = request.getParameter("descr");
        String clusterSetName = request.getParameter("clusterSetList");

        Cluster newCluster = new Cluster();

        try {
            hibSession.beginTransaction();

            ClusterSet cs = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
                    .add(Restrictions.eq("clusterSetName", clusterSetName)).uniqueResult();

            if (cs == null) {
                hibSession.getTransaction().rollback();
                logger.error("Had a WTF error using addCluster");
                throw new DbFacadeException("Had a WTF error using addCluster");
            }

            newCluster.setClusterName(clusterName);
            newCluster.setDescr(descr);
            newCluster.setClusterSet(cs);

            hibSession.save(newCluster);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using addCluster, ", e);
            throw e;
        }

    }

    public static ArrayList<Cluster> queryCluster(HttpServletRequest request) throws HibernateException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");
        ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
        try {
            hibSession.beginTransaction();
            clusterList = (ArrayList<Cluster>) hibSession.createCriteria(Cluster.class).list();
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Error using queryCluster, ", ex);
            throw ex;
        }
        return clusterList;
    }

//    public static void updateSingleCluster(HttpServletRequest request, Cluster cluster) throws HibernateException {
//
//        HttpSession httpSession = request.getSession();
//        Session hibSession = (Session) httpSession.getAttribute("accessObject");
//
//        try {
//            hibSession.beginTransaction();
//            hibSession.update(cluster);
//            hibSession.getTransaction().commit();
//
//        } catch (HibernateException e) {
//            hibSession.getTransaction().rollback();
//            logger.error("Hibernate error using updateSingleCluster, ", e);
//            throw e;
//        }
//    }

    public static void setSingleCluster(HttpServletRequest request, String cluster) throws HibernateException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");
        Cluster c = null;
        try {
            hibSession.beginTransaction();
            c = (Cluster) hibSession.createCriteria(Cluster.class).add(Restrictions.eq("clusterName", cluster))
                    .uniqueResult();

            hibSession.getTransaction().commit();
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using setSingleCluster, ", e);
            throw e;
        }
        request.setAttribute("cluster", c);
        return;
    }

    public static ArrayList<String> listClusterNames(HttpServletRequest request) throws HibernateException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");
        Order ord = org.hibernate.criterion.Order.desc("clusterName");

        ArrayList<Cluster> clusters = null;
        try {

            hibSession.beginTransaction();
            clusters = (ArrayList<Cluster>) hibSession.createCriteria(Cluster.class).addOrder(ord).list();
            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using listAllClusterNames, ", ex);
            throw ex;
        }
        ArrayList<String> s = new ArrayList<String>();
        for (Cluster c : clusters) {
            s.add(c.getClusterName());
        }
        return s;
    }

    public static void updateCluster(HttpServletRequest request) throws HibernateException , DbFacadeException {

        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

        String clusterName = request.getParameter("clusterName");
        String descr = request.getParameter("descr");
        String clusterSetName = request.getParameter("clusterSetList");

        Cluster asSaved = null;

        try {
            hibSession.beginTransaction();

            asSaved = (Cluster) hibSession.createCriteria(Cluster.class)
                    .add(Restrictions.eq("clusterName", clusterName)).uniqueResult();
            if (asSaved == null) {
                hibSession.getTransaction().rollback();
                logger.error("Had a WTF error using addCluster");
                throw new DbFacadeException("Had a WTF error using updateCluster");
            }

            asSaved.setClusterName(clusterName);
            asSaved.setDescr(descr);
            if (!asSaved.getClusterSet().getClusterSetName().equals(clusterSetName)) {
                ClusterSet newClusterSet = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
                        .add(Restrictions.eq("clusterSetName", clusterSetName)).uniqueResult();
                if (newClusterSet == null) {
                    hibSession.getTransaction().rollback();
                    logger.error("Had a WTF error using updateNodeSet, involving newClusterSet");
                    throw new DbFacadeException("Had a WTF error using updateNodeSet, involving newClusterSet");
                }
                asSaved.setClusterSet(newClusterSet);
            }
            hibSession.update(asSaved);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using updateClusterSet, ", e);
            throw e;
        }
    }

    public static void addNodeType(HttpServletRequest request) throws HibernateException , DbFacadeException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

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

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using addNodeType, ", e);
            throw e;
        }
    }

    public static void deleteNodeType(HttpServletRequest request, String nodeTypeName) throws HibernateException , DbFacadeException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");
        try {
            hibSession.beginTransaction();
            NodeType ns = (NodeType) hibSession.createCriteria(NodeType.class)
                    .add(Restrictions.eq("nodeTypeName", nodeTypeName)).uniqueResult();
            hibSession.delete(ns);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using deleteNodeType, ", ex);
            throw ex;
        }
    }

    public static NodeType queryOneNodeType(HttpServletRequest request, String nodeTypeName) throws HibernateException  {

        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

        try {
            hibSession.beginTransaction();
            NodeType ns = (NodeType) hibSession.createCriteria(NodeType.class)
                    .add(Restrictions.eq("nodeTypeName", nodeTypeName)).uniqueResult();
            hibSession.getTransaction().commit();
            return ns;
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using queryOneNodeType, ", ex);
            throw ex;
        }
    }

    public static void updateNodeType(HttpServletRequest request) throws HibernateException , DbFacadeException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

        String nodeTypeName = request.getParameter("nodeTypeName");
        String cpu = request.getParameter("cpu");
        String slot = request.getParameter("slot");
        String hs06PerSlot = request.getParameter("hs06PerSlot");
        String memPerNode = request.getParameter("memPerNode");

        NodeType existing = null;

        try {
            hibSession.beginTransaction();

            List<NodeType> csl = hibSession.createCriteria(NodeType.class)
                    .add(Restrictions.eq("nodeTypeName", nodeTypeName)).list();
            if (csl.size() != 1) {
                hibSession.getTransaction().rollback();
                logger.error("Had a WTF error using updateNodeType");
                throw new DbFacadeException("Had a WTF error using updateNodeType");
            } else {
                existing = csl.get(0);
            }
            existing.setCpu(Integer.parseInt(cpu));
            existing.setSlot(Integer.parseInt(slot));
            existing.setHs06PerSlot(Double.parseDouble(hs06PerSlot));
            existing.setMemPerNode(Double.parseDouble(memPerNode));
            hibSession.update(existing);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using updateClusterSet, ", e);
            throw e;
        }
    }

    public static void refreshNodeSets(HttpServletRequest request, String col, String order) throws HibernateException {

        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

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
            throw ex;
        }
        request.setAttribute("nodeSetList", list);
    }

    public static ArrayList<String> listNodeTypeNames(HttpServletRequest request) throws HibernateException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");
        Order ord = org.hibernate.criterion.Order.desc("nodeTypeName");

        ArrayList<NodeType> nodeTypes = null;
        try {

            hibSession.beginTransaction();
            nodeTypes = (ArrayList<NodeType>) hibSession.createCriteria(NodeType.class).addOrder(ord).list();
            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using listNodeTypes, ", ex);
            throw ex;
        }
        ArrayList<String> s = new ArrayList<String>();
        for (NodeType n : nodeTypes) {
            s.add(n.getNodeTypeName());
        }
        return s;
    }

    public static void updateNodeSet(HttpServletRequest request) throws HibernateException , DbFacadeException {

        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

        String nodeSetName = request.getParameter("nodeSetName");
        String nodeCount = request.getParameter("nodeCount");
        String nodeTypeName = request.getParameter("nodeTypeList");
        String clusterName = request.getParameter("clusterList");

        NodeSet asSaved = null;

        try {
            hibSession.beginTransaction();

            NodeSet ns = (NodeSet) hibSession.createCriteria(NodeSet.class)
                    .add(Restrictions.eq("nodeSetName", nodeSetName)).uniqueResult();
            if (ns == null) {
                hibSession.getTransaction().rollback();
                logger.error("Had a WTF error using updateNodeSet");
                throw new DbFacadeException("Had a WTF error using updateNodeSet");
            } else {
                asSaved = ns;
            }
            asSaved.setNodeCount(Integer.parseInt(nodeCount));

            if (!asSaved.getNodeType().getNodeTypeName().equals(nodeTypeName)) {
                NodeType newNodeType = (NodeType) hibSession.createCriteria(NodeType.class)
                        .add(Restrictions.eq("nodeTypeName", nodeTypeName)).uniqueResult();
                if (newNodeType == null) {
                    hibSession.getTransaction().rollback();
                    logger.error("Had a WTF error using updateNodeSet, involving newNodeType");
                    throw new DbFacadeException("Had a WTF error using updateNodeSet, involving newNodeType");
                }
                asSaved.setNodeType(newNodeType);
            }
            if (!asSaved.getCluster().getClusterName().equals(clusterName)) {
                Cluster newCluster = (Cluster) hibSession.createCriteria(Cluster.class)
                        .add(Restrictions.eq("clusterName", clusterName)).uniqueResult();
                if (newCluster == null) {
                    hibSession.getTransaction().rollback();
                    logger.error("Had a WTF error using updateNodeSet, involving newCluster");
                    throw new DbFacadeException("Had a WTF error using updateNodeSet, involving newCluster");
                }
                asSaved.setCluster(newCluster);
            }

            hibSession.update(asSaved);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using updateNodeSet, ", e);
            throw e;
        }
    }

    public static void deleteNodeSet(HttpServletRequest request, String nodeSetName) throws HibernateException, DbFacadeException {
        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");
        try {
            hibSession.beginTransaction();
            NodeSet ns = (NodeSet) hibSession.createCriteria(NodeSet.class)
                    .add(Restrictions.eq("nodeSetName", nodeSetName)).uniqueResult();
            hibSession.delete(ns);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using deleteNodeSet, ", ex);
            throw ex;
        }
    }
    public static NodeSet queryOneNodeSet (HttpServletRequest request, String nodeSetName) throws HibernateException  {

        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

        try {
            hibSession.beginTransaction();
            NodeSet ns = (NodeSet) hibSession.createCriteria(NodeSet.class)
                    .add(Restrictions.eq("nodeSetName", nodeSetName)).uniqueResult();
                hibSession.getTransaction().commit();
                return ns;

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using queryOneNodeSet, ", ex);
            throw ex;
        }
    }
    public static void addNodeSet(HttpServletRequest request) throws HibernateException , DbFacadeException {

        HttpSession httpSession = request.getSession();
        Session hibSession = (Session) httpSession.getAttribute("accessObject");

        String nodeSetName = request.getParameter("nodeSetName");
        String nodeCount = request.getParameter("nodeCount");
        String nodeTypeName = request.getParameter("nodeTypeList");
        String clusterName = request.getParameter("clusterList");

        NodeSet newNodeSet = new NodeSet();

        try {
            hibSession.beginTransaction();

            Cluster c = (Cluster) hibSession.createCriteria(Cluster.class)
                    .add(Restrictions.eq("clusterName", clusterName)).uniqueResult();

            if (c == null) {
                hibSession.getTransaction().rollback();
                logger.error("Had a WTF error using addNodeSet");
                throw new DbFacadeException("Had a WTF error using addNodeSet");
            }

            NodeType n = (NodeType) hibSession.createCriteria(NodeType.class)
                    .add(Restrictions.eq("nodeTypeName", nodeTypeName)).uniqueResult();
            if (n == null) {
                hibSession.getTransaction().rollback();
                logger.error("Had a WTF error using addNodeSet, involving nodeType");
                throw new DbFacadeException("Had a WTF error using addNodeSet, involving nodeType");
            }
            newNodeSet.setCluster(c);
            newNodeSet.setNodeType(n);
            newNodeSet.setNodeCount(Integer.parseInt(nodeCount));
            newNodeSet.setNodeSetName(nodeSetName);

            hibSession.save(newNodeSet);
            hibSession.getTransaction().commit();
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("Hibernate error using addNodeSet, ", e);
            throw e;
        }

    }

    public static ArrayList<NodeSetNodeTypeJoin> getJoinForCluster(HttpServletRequest request, String clusterName)
            throws HibernateException {
        HttpSession session = request.getSession();
        Session s = (Session) session.getAttribute("accessObject");
        ArrayList<NodeSetNodeTypeJoin> nsntj = new ArrayList<NodeSetNodeTypeJoin>();

        List<NodeSet> ns = s.createCriteria(NodeSet.class).add(Restrictions.eq("cluster.clusterName", clusterName)).list();
        for (NodeSet n: ns) {
            Float nodeSetHs06 = (float) (n.getNodeType().getHs06PerSlot() * n.getNodeCount()); 
            NodeSetNodeTypeJoin j = new NodeSetNodeTypeJoin(
                    n.getNodeSetName(), 
                    n.getNodeType().getNodeTypeName(), 
                    n.getNodeCount(), 
                    new Integer(n.getNodeType().getCpu()), 
                    new Integer(n.getNodeType().getSlot()), 
                    new Float(n.getNodeType().getHs06PerSlot()),
                    nodeSetHs06 );
            nsntj.add(j);
        }
        return nsntj;
    }
    
    
}
