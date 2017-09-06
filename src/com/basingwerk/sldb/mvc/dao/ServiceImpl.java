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


import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.Cluster;
import com.basingwerk.sldb.mvc.model.ClusterSet;
import com.basingwerk.sldb.mvc.model.Service;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;

import javax.persistence.criteria.Root;
import com.basingwerk.sldb.mvc.dao.ClusterSetImpl;

public class ServiceImpl implements ServiceDao  {
    final static Logger logger = Logger.getLogger(ServiceImpl.class);
    
    @Override
    public   Service readOneService(Session hibSession, String serviceName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Service> q = cb.createQuery(Service.class);
        Root<Service> root = q.from(Service.class);
        q.select(root).where(cb.equal(root.get("serviceName"), serviceName));
        return hibSession.createQuery(q).getSingleResult();
    }
    @Override
    public   List<Service> readServiceList(Session hibSession, String col, String order) {
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
    public   void loadServices(HttpServletRequest request, String col, String order)
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
            serviceList = readServiceList(hibSession, col, order);

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

}
