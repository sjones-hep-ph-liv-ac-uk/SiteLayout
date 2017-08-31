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
import com.basingwerk.sldb.mvc.model.ServiceNode;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.basingwerk.sldb.mvc.dao.ClusterSetImpl;

public class ServiceNodeImpl implements ServiceNodeDao {
    final static Logger logger = Logger.getLogger(ServiceNodeImpl.class);
    private static ServiceNodeDao instance = null;
    public static ServiceNodeDao getInstance() {
        if (instance == null) {
            instance = new ServiceNodeImpl();
        }
        return instance;
    }
    
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#updateServiceNode(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public  void updateServiceNode(HttpServletRequest request) throws WTFException, RoutineException {

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

            String updatedHostname = request.getParameter("hostname");
            String updatedCluster = request.getParameter("clusterList");
            String updatedHostSystem = request.getParameter("hostSystemList");
            String updatedCpu = request.getParameter("cpu");
            String updatedMem = request.getParameter("mem");
            String updatedOs = request.getParameter("os");
            String updatedKernel = request.getParameter("kernel");
            String updatedService = request.getParameter("service");
            String updatedComment = request.getParameter("comment");

            ServiceNode storedServiceNode = null;
            ServiceNode cachedServiceNode = (ServiceNode) httpSession.getAttribute("serviceNode");

            if (cachedServiceNode == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateServiceNode, desired ServiceNode not found");
                throw new RoutineException("While using updateServiceNode, desired ServiceNode not found");
            }
            Long cachedServiceNodeVersion = cachedServiceNode.getVersion();

            storedServiceNode = readOneServiceNode(hibSession, updatedHostname);

            // Possibly deleted during long conversation
            if (storedServiceNode == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateServiceNode, desired ServiceNode  not found");
                throw new RoutineException("While using updateServiceNode, desired ServiceNode  not found");
            }
            // Possibly altered during long conversation
            Long storedServiceNodeVersion = storedServiceNode.getVersion();

            if (!storedServiceNodeVersion.equals(cachedServiceNodeVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateServiceNode, desired ServiceNode was altered by another user ");
                throw new RoutineException(
                        "While using updateServiceNode, desired ServiceNode was altered by another user ");
            }

            // Both the same. Safe to update
            storedServiceNode.setHostname(updatedHostname);
            storedServiceNode.setCpu(Integer.parseInt(updatedCpu));
            storedServiceNode.setMem(Integer.parseInt(updatedMem));
            storedServiceNode.setOs(updatedOs);
            storedServiceNode.setKernel(updatedKernel);
            storedServiceNode.setService(updatedService);
            storedServiceNode.setComment(updatedComment);

            ClusterDao clusterDao = ClusterImpl.getInstance();
            Cluster cluster = clusterDao.readOneCluster(hibSession, updatedCluster);
            if (cluster == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateServiceNode, desired Cluster  not found");
                throw new RoutineException("While using updateServiceNode, desired Cluster not found");
            }
            storedServiceNode.setCluster(cluster);

            HostSystemDao hostSystemDao = HostSystemImpl.getInstance();
            HostSystem hostSystem = hostSystemDao.readOneHostSystem(hibSession, updatedHostSystem);
            if (hostSystem == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using updateServiceNode, desired HostSystem not found");
                throw new RoutineException("While using updateServiceNode, desired HostSystem not found");
            }
            storedServiceNode.setHostSystem(hostSystem);

            hibSession.update(storedServiceNode);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using updateServiceNode, ", e);
            throw new WTFException("WTF error using updateServiceNode");
        } finally {
            hibSession.close();
        }
    }
    
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#readOneServiceNode(org.hibernate.Session, java.lang.String)
     */
    @Override
    public  ServiceNode readOneServiceNode(Session hibSession, String hostname) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<ServiceNode> q = cb.createQuery(ServiceNode.class);
        Root<ServiceNode> root = q.from(ServiceNode.class);
        q.select(root).where(cb.equal(root.get("hostname"), hostname));
        return hibSession.createQuery(q).getSingleResult();
    }
    
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#readServiceNodeList(org.hibernate.Session, java.lang.String, java.lang.String)
     */
    @Override
    public  List<ServiceNode> readServiceNodeList(Session hibSession, String col, String order) {
        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<ServiceNode> cq = cb.createQuery(ServiceNode.class);
        cq.distinct(true);
        Root<ServiceNode> c = cq.from(ServiceNode.class);
        if (order.equalsIgnoreCase("ASC"))
            cq.orderBy(cb.asc(c.get(col)));
        else
            cq.orderBy(cb.desc(c.get(col)));
        cq.select(c);
        TypedQuery<ServiceNode> q = hibSession.createQuery(cq);
        List<ServiceNode> theList = q.getResultList();
        return theList;
    }
    
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#addServiceNode(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public  void addServiceNode(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        String hostname = request.getParameter("hostname");
        String cluster = request.getParameter("clusterList");
        String hostSystem = request.getParameter("hostSystemList");
        String cpu = request.getParameter("cpu");
        String mem = request.getParameter("mem");
        String os = request.getParameter("os");
        String kernel = request.getParameter("kernel");
        String service = request.getParameter("service");
        String comment = request.getParameter("comment");

        ServiceNode sn = new ServiceNode();

        try {
            hibSession.beginTransaction();

            ClusterDao clusterDao = ClusterImpl.getInstance();
            Cluster cl = clusterDao.readOneCluster(hibSession, cluster);
            if (cl == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addServiceNode, desired Cluster not found");
                throw new RoutineException("While using addServiceNode, desired Cluster not found");
            }
            HostSystemDao hostSystemDao = HostSystemImpl.getInstance();
            HostSystem hs = hostSystemDao.readOneHostSystem(hibSession, hostSystem);

            if (hs == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addServiceNode, desired HostSystem not found");
                throw new RoutineException("While using addServiceNode, desired HostSystem not found");
            }

            sn.setHostname(hostname);
            sn.setCluster(cl);
            sn.setHostSystem(hs);
            sn.setCpu(Integer.parseInt(cpu));
            sn.setMem(Integer.parseInt(mem));
            sn.setOs(os);
            sn.setKernel(kernel);
            sn.setService(service);
            sn.setComment(comment);

            hibSession.save(sn);
            hibSession.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            hibSession.getTransaction().rollback();
            logger.error("While using addServiceNode, the hostname conflicted with an existing service node");
            throw new RoutineException(
                    "While using addServiceNode, the hostname conflicted with an existing service node");
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using addServiceNode, ", e);
            throw new WTFException("WTF error using addServiceNode");
        } finally {
            hibSession.close();
        }
    }
    
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#deleteServiceNode(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    @Override
    public  void deleteServiceNode(HttpServletRequest request, String hostname) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        ServiceNode serviceNode = readOneServiceNode(hibSession, hostname);

        if (serviceNode == null) {
            logger.error("While using deleteServiceNode, desired ServiceNode not found");
            throw new RoutineException("While using deleteServiceNode, desired ServiceNodenot found");
        }
        try {
            hibSession.beginTransaction();
            hibSession.delete(serviceNode);
            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            // Possibly deleted during long conversation
            hibSession.getTransaction().rollback();
            logger.error("While using deleteServiceNode, desired ServiceNode not found");
            throw new RoutineException("While using deleteServiceNode , desired ServiceNodenot found");
        } finally {
            hibSession.close();
        }
    }
    
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#loadIndexedServiceNode(javax.servlet.http.HttpServletRequest, java.lang.Integer)
     */
    @Override
    public  void loadIndexedServiceNode(HttpServletRequest request, Integer serviceNodeIndex)
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

        ServiceNode cachedServiceNode = null;
        ServiceNode storedServiceNode = null;
        try {
            hibSession.beginTransaction();

            cachedServiceNode = ((ArrayList<ServiceNode>) httpSession.getAttribute("serviceNodeList"))
                    .get(serviceNodeIndex);

            if (cachedServiceNode == null) {
                logger.error("While using loadIndexedServiceNode, desired ServiceNode not found");
                throw new RoutineException("While using loadIndexedServiceNode, desired ServiceNode not found");
            }

            Long cachedVersion = cachedServiceNode.getVersion();
            ServiceNodeDao serviceNodeDao  = ServiceNodeImpl.getInstance();
            storedServiceNode = serviceNodeDao.readOneServiceNode(hibSession, cachedServiceNode.getHostname());

            // Possibly deleted during long conversation
            if (storedServiceNode == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedServiceNode, desired ServiceNode not found");
                throw new RoutineException("While using loadIndexedServiceNode, desired ServiceNode not found");
            }

            Long storedVersion = storedServiceNode.getVersion();
            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedServiceNode, desired ServiceNode was altered by another user ");
                throw new RoutineException(
                        "While using loadIndexedServiceNode, desired ServiceNode was altered by another user ");
            }

            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadIndexedServiceNode, ", ex);
            throw new WTFException("WTF error using loadIndexedServiceNode");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("serviceNode", storedServiceNode);
    }
    
    /* (non-Javadoc)
     * @see com.basingwerk.sldb.mvc.dao.ServiceNodeDao#loadServiceNodes(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     */
    @Override
    public  void loadServiceNodes(HttpServletRequest request, String col, String order)
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

        List<ServiceNode> serviceNodeList = new ArrayList<ServiceNode>();
        try {

            hibSession.beginTransaction();

            ServiceNodeDao serviceNodeDao  = ServiceNodeImpl.getInstance();

            serviceNodeList = serviceNodeDao.readServiceNodeList(hibSession, col, order);

            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error using loadServiceNodes, ", ex);
            throw new WTFException("Had an error using loadServiceNodes");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("serviceNodeList", serviceNodeList);
    }


}