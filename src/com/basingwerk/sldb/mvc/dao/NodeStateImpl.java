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
import com.basingwerk.sldb.mvc.model.NodeState;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.basingwerk.sldb.mvc.dao.ClusterSetImpl;

public class NodeStateImpl implements NodeStateDao {
    final static Logger logger = Logger.getLogger(NodeStateImpl.class);
    
    private static NodeStateDao instance = null;
    public static NodeStateDao getInstance() {
        if (instance == null) {
            instance = new NodeStateImpl();
        }
        return instance;
    }
    
    
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeStateDao#readNodeStateList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    @Override
    public   List<NodeState> readNodeStateList(Session hibSession, String col, String order) {
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
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeStateDao#readOneNodeState(org.hibernate.Session, java.lang.String)
     */
    @Override
    public   NodeState readOneNodeState(Session hibSession, String nodeStateName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<NodeState> q = cb.createQuery(NodeState.class);
        Root<NodeState> root = q.from(NodeState.class);
        q.select(root).where(cb.equal(root.get("nodeStateName"), nodeStateName));
        return hibSession.createQuery(q).getSingleResult();
    }
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.NodeStateDao#loadNodeStates(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     */
    @Override
    public   void loadNodeStates(HttpServletRequest request, String col, String order)
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
            NodeStateDao nodeStateDao = NodeStateImpl.getInstance();
            nodeStateList = nodeStateDao.readNodeStateList(hibSession, col, order);

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadNodeStates, ", ex);
            throw new WTFException("WTF error using loadNodeStates");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("nodeStateList", nodeStateList);
    }

}