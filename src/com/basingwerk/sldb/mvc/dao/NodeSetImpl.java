package com.basingwerk.sldb.mvc.dao;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.Cluster;
import com.basingwerk.sldb.mvc.model.ClusterSet;
import com.basingwerk.sldb.mvc.model.NodeSet;
import com.basingwerk.sldb.mvc.model.NodeType;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;

import javax.persistence.criteria.Root;
import com.basingwerk.sldb.mvc.dao.ClusterSetImpl;

public class NodeSetImpl implements NodeSetDao  {
    final static Logger logger = Logger.getLogger(NodeSetImpl.class);

    @Override
    public   void updateNodeSet(HttpServletRequest request) throws WTFException, RoutineException {

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
            ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
            Cluster cluster = clusterDao.readOneCluster(hibSession, updatedClusterName);
            if (cluster == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateNodeSet, desired Cluster  not found");
                throw new RoutineException("While using updateNodeSet, desired Cluster not found");
            }
            storedNodeSet.setCluster(cluster);

            NodeTypeDao nodeTypeDao = (NodeTypeDao) request.getSession().getAttribute("nodeTypeDao");
            NodeType nodeType = nodeTypeDao.readOneNodeType(hibSession, updatedNodeTypeName);

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
    @Override
    public   List<NodeSet> readNodeSetList(Session hibSession, String col, String order) {
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
    @Override
    public   NodeSet readOneNodeSet(Session hibSession, String nodeSetName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<NodeSet> q = cb.createQuery(NodeSet.class);
        Root<NodeSet> root = q.from(NodeSet.class);
        q.select(root).where(cb.equal(root.get("nodeSetName"), nodeSetName));
        return hibSession.createQuery(q).getSingleResult();
    }
    @Override
    public   void addNodeSet(HttpServletRequest request) throws WTFException, RoutineException {

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
            ClusterDao clusterDao = (ClusterDao) request.getSession().getAttribute("clusterDao");
            Cluster c = clusterDao.readOneCluster(hibSession, clusterName);

            if (c == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addNodeSet, desired Cluster not found");
                throw new RoutineException("While using addNodeSet, desired Cluster not found");
            }

            NodeTypeDao nodeTypeDao = (NodeTypeDao) request.getSession().getAttribute("nodeTypeDao");
            NodeType nodeType = nodeTypeDao.readOneNodeType(hibSession, nodeTypeName);

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
        } catch (PersistenceException e) {
            if (Util.isIdClash(e)) {
                hibSession.getTransaction().rollback();
                throw new RoutineException(
                        "While using addNodeSet, the nodeSetName conflicted with an existing nodeSet");
            } else {
                logger.error("WTF error using addNodeSet, ", e);
                throw new WTFException("WTF while using addNodeSet");
            }
        }
        finally {
            hibSession.close();
        }
    }
    @Override
    public   void deleteNodeSet(HttpServletRequest request, String nodeSetName) throws WTFException, RoutineException {

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
            Cluster c = nodeSet.getCluster();
            Map<String, NodeSet> m = c.getNodeSets();
            m.remove(nodeSet.getNodeSetName());
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
    @Override
    public   void    loadIndexedNodeSet(HttpServletRequest request, Integer nodeSetIndex)
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

            NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao");
            storedNodeSet = nodeSetDao.readOneNodeSet(hibSession, cachedNodeSet.getNodeSetName());

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
    @Override
    public   void  loadNodeSets(HttpServletRequest request, String col, String order)
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
            NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao");
            nodeSetList = nodeSetDao.readNodeSetList(hibSession, col, order);

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
}
