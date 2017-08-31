package com.basingwerk.sldb.mvc.dao;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Root;
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


public class ClusterSetImpl implements  ClusterSetDao {
    final static Logger logger = Logger.getLogger(ClusterSetImpl.class);

    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#updateClusterSet(javax.servlet.http.HttpServletRequest)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#updateClusterSet(javax.servlet.http.HttpServletRequest)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#updateClusterSet(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public  void updateClusterSet(HttpServletRequest request) throws WTFException, RoutineException {

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
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#readClusterSetList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#readClusterSetList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#readClusterSetList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    @Override
    public  List<ClusterSet> readClusterSetList(Session hibSession, String col, String order) {
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
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#readOneClusterSet(org.hibernate.Session, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#readOneClusterSet(org.hibernate.Session, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#readOneClusterSet(org.hibernate.Session, java.lang.String)
     */
    @Override
    public  ClusterSet readOneClusterSet(Session hibSession, String clusterSetName) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<ClusterSet> q = cb.createQuery(ClusterSet.class);
        Root<ClusterSet> root = q.from(ClusterSet.class);
        q.select(root).where(cb.equal(root.get("clusterSetName"), clusterSetName));
        return hibSession.createQuery(q).getSingleResult();
    }
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#addClusterSet(javax.servlet.http.HttpServletRequest)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#addClusterSet(javax.servlet.http.HttpServletRequest)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#addClusterSet(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public  void addClusterSet(HttpServletRequest request) throws WTFException, RoutineException {

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
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#deleteClusterSet(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#deleteClusterSet(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#deleteClusterSet(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    @Override
    public  void deleteClusterSet(HttpServletRequest request, String clusterSetName)
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
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#loadClusterSets(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#loadClusterSets(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#loadClusterSets(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     */
    @Override
    public  void loadClusterSets(HttpServletRequest request, String col, String order)
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

        List<ClusterSet> clusterSetList = null;

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
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ClusterSetDao#loadIndexedClusterSet(javax.servlet.http.HttpServletRequest, java.lang.Integer)
     */
    @Override
    public  void       loadIndexedClusterSet(HttpServletRequest request, Integer clusterSetIndex)
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

            storedClusterSet = readOneClusterSet(hibSession, cachedClusterSet.getClusterSetName());

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
}
