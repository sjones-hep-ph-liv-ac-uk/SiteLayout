package com.basingwerk.sldb.mvc.dao;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.Cluster;
import com.basingwerk.sldb.mvc.model.ClusterSet;
import com.basingwerk.sldb.mvc.model.Node;
import com.basingwerk.sldb.mvc.model.NodeSet;
import com.basingwerk.sldb.mvc.model.NodeState;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.basingwerk.sldb.mvc.dao.ClusterSetImpl;

public class NodeImpl implements NodeDao  {
    final static Logger logger = Logger.getLogger(NodeImpl.class);
    
    @Override
    public   void updateNode(HttpServletRequest request) throws WTFException, RoutineException {

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
            NodeSetDao nodeSetDao = (NodeSetDao) request.getSession().getAttribute("nodeSetDao");
            NodeSet nodeSet = nodeSetDao .readOneNodeSet(hibSession, updatedNodeSet);
            if (nodeSet == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateNode, desired nodeSet  not found");
                throw new RoutineException("While using updateNode, desired nodeSet not found");
            }
            storedNode.setNodeSet(nodeSet);
            NodeStateDao nodeStateDao = (NodeStateDao) request.getSession().getAttribute("nodeStateDao");
            NodeState nodeState = nodeStateDao.readOneNodeState(hibSession, updatedNodeState);
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
    @Override
    public   List<Node> readNodeList(Session hibSession, String col, String order) {
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
    @Override
    public   Node readOneNode(Session hibSession, String nodeName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Node> q = cb.createQuery(Node.class);
        Root<Node> root = q.from(Node.class);
        q.select(root).where(cb.equal(root.get("nodeName"), nodeName));
        return hibSession.createQuery(q).getSingleResult();

    }
    @Override
    public   void addNode(HttpServletRequest request) throws WTFException, RoutineException {

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
            NodeSetDao nodeSetDao  = (NodeSetDao) request.getSession().getAttribute("nodeSetDao"); 
            NodeSet nodeSet = nodeSetDao.readOneNodeSet(hibSession, nodeSetName);
            

            if (nodeSet == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addNode, desired NodeSet not found");
                throw new RoutineException("While using addNode, desired NodeSet not found");
            }
            NodeStateDao nodeStateDao  = (NodeStateDao) request.getSession().getAttribute("nodeStateDao"); 
            NodeState nodeState = nodeStateDao.readOneNodeState(hibSession, nodeStateName);
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
    @Override
    public   void deleteNode(HttpServletRequest request, String nodeName) throws WTFException, RoutineException {

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
    @Override
    public   void loadIndexedNode(HttpServletRequest request, Integer nodeIndex) throws WTFException, RoutineException {

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

            NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao");
            storedNode = nodeDao.readOneNode(hibSession, cachedNode.getNodeName());

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
    @Override
    public   void loadNodes(HttpServletRequest request, String col, String order) throws RoutineException, WTFException {
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

            NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao");
            nodeList = nodeDao.readNodeList(hibSession, col, order);

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
    @Override
    public   void toggleCheckedNodes(HttpServletRequest request) throws RoutineException, WTFException {

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
                NodeDao nodeDao = (NodeDao) request.getSession().getAttribute("nodeDao");
                Node node = nodeDao.readOneNode(hibSession, nodeName);
                NodeStateDao nodeStateDao = (NodeStateDao) request.getSession().getAttribute("nodeStateDao");
                if (node.getNodeState().getState().equalsIgnoreCase("ONLINE"))
                    node.setNodeState(nodeStateDao.readOneNodeState(hibSession, "OFFLINE"));
                else
                    node.setNodeState(nodeStateDao.readOneNodeState(hibSession, "ONLINE"));
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
    @Override
    public   void toggleIndexedNode(HttpServletRequest request, Integer nodeIndex) throws RoutineException, WTFException {

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

            NodeStateDao nodeStateDao = (NodeStateDao) request.getSession().getAttribute("nodeStateDao");
            if (node.getNodeState().getState().equalsIgnoreCase("ONLINE"))
                node.setNodeState(nodeStateDao.readOneNodeState(hibSession, "OFFLINE"));
            else
                node.setNodeState(nodeStateDao.readOneNodeState(hibSession, "ONLINE"));
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

}
