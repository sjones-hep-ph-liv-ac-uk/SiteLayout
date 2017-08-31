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
import com.basingwerk.sldb.mvc.model.NodeType;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.basingwerk.sldb.mvc.dao.ClusterSetImpl;


public class NodeTypeImpl implements NodeTypeDao {
    final static Logger logger = Logger.getLogger(NodeTypeImpl.class);
    private static NodeTypeDao instance = null;
    public static NodeTypeDao getInstance() {
        if (instance == null) {
            instance = new NodeTypeImpl();
        }
        return instance;
    }

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#updateNodeType(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public  void updateNodeType(HttpServletRequest request) throws WTFException, RoutineException {

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
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#readNodeTypeList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    @Override
    public  List<NodeType> readNodeTypeList(Session hibSession, String col, String order) {
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
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#readOneNodeType(org.hibernate.Session, java.lang.String)
     */
    @Override
    public  NodeType readOneNodeType(Session hibSession, String nodeTypeName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<NodeType> q = cb.createQuery(NodeType.class);
        Root<NodeType> root = q.from(NodeType.class);
        q.select(root).where(cb.equal(root.get("nodeTypeName"), nodeTypeName));
        return hibSession.createQuery(q).getSingleResult();
    }
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#addNodeType(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public  void addNodeType(HttpServletRequest request) throws WTFException, RoutineException {

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
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#deleteNodeType(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    @Override
    public  void deleteNodeType(HttpServletRequest request, String nodeTypeName) throws WTFException, RoutineException {

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
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#loadIndexedNodeType(javax.servlet.http.HttpServletRequest, java.lang.Integer)
     */
    @Override
    public  void loadIndexedNodeType(HttpServletRequest request, Integer nodeTypeIndex)
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

            NodeTypeDao nodeTypeDao = NodeTypeImpl.getInstance();
            storedNodeType = nodeTypeDao.readOneNodeType(hibSession, cachedNodeType.getNodeTypeName());

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
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#loadNodeTypes(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     */
    @Override
    public  void loadNodeTypes(HttpServletRequest request, String col, String order)
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
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeTypeDao#setBaselineNodeType(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public  void setBaselineNodeType(HttpServletRequest request) throws RoutineException, WTFException {

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

            NodeTypeDao nodeTypeDao = NodeTypeImpl.getInstance(); 
            nodeTypes = nodeTypeDao.readNodeTypeList(hibSession, "nodeTypeName", "ASC");
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
}