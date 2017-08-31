package com.basingwerk.sldb.mvc.dao;

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

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.basingwerk.sldb.mvc.dao.ClusterSetImpl;

public class ClusterImpl implements ClusterDao  {
    final static Logger logger = Logger.getLogger(ClusterImpl.class);


    @Override
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
            ClusterSetDao clusterSetDao = (ClusterSetDao) request.getSession().getAttribute("clusterSetDao");
            ClusterSet newClusterSet = clusterSetDao.readOneClusterSet(hibSession, updatedClusterSetName);

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

    @Override
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

    @Override
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
        return clusterList;
    }

    @Override
    public Cluster readOneCluster(Session hibSession, String clusterName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Cluster> q = cb.createQuery(Cluster.class);
        Root<Cluster> root = q.from(Cluster.class);
        q.select(root).where(cb.equal(root.get("clusterName"), clusterName));
        return hibSession.createQuery(q).getSingleResult();
    }

    @Override
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
            ClusterSetDao clusterSetDao = (ClusterSetDao) request.getSession().getAttribute("clusterSetDao");
            ClusterSet cs = clusterSetDao.readOneClusterSet(hibSession, clusterSetName);

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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void  loadIndexedCluster(HttpServletRequest request, Integer clusterIndex)
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

            storedCluster = readOneCluster(hibSession, cachedCluster.getClusterName());

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

}
