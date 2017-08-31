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
import com.basingwerk.sldb.mvc.model.HostSystem;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.basingwerk.sldb.mvc.dao.ClusterSetImpl;

public class HostSystemImpl implements HostSystemDao {
    final static Logger logger = Logger.getLogger(HostSystemImpl.class);

    
    @Override
    public  List<HostSystem> readHostSystemList(Session hibSession, String col, String order) {
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
    @Override
    public  HostSystem readOneHostSystem(Session hibSession, String hostname) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<HostSystem> q = cb.createQuery(HostSystem.class);
        Root<HostSystem> root = q.from(HostSystem.class);
        q.select(root).where(cb.equal(root.get("hostname"), hostname));
        return hibSession.createQuery(q).getSingleResult();
    }
    @Override
    public  void loadHostSystems(HttpServletRequest request, String col, String order)
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
}
