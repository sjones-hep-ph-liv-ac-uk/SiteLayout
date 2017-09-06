package com.basingwerk.sldb.mvc.dao;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.Installation;
import com.basingwerk.sldb.mvc.model.Service;
import com.basingwerk.sldb.mvc.model.ServiceNode;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class InstallationImpl implements InstallationDao {
    final static Logger logger = Logger.getLogger(InstallationImpl.class);

    @Override
    public void updateInstallation(HttpServletRequest request) throws WTFException, RoutineException {

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
            // String updatedService = request.getParameter("service");
            // String updatedServiceNode = request.getParameter("serviceNode");
            String updatedSoftwareVersion = request.getParameter("softwareVersion");

            Installation cachedInstallation = (Installation) httpSession.getAttribute("installation");

            if (cachedInstallation == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateInstallation, desired Installation not found");
                throw new RoutineException("While using updateInstallation, desired Installation not found");
            }
            Long cachedInstallationVersion = cachedInstallation.getVersion();
            String serviceName = cachedInstallation.getService().getServiceName();
            String hostname = cachedInstallation.getServiceNode().getHostname();

            Installation storedInstallation = readOneInstallation(hibSession, serviceName, hostname);

            // Possibly deleted during long conversation
            if (storedInstallation == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateInstallation, desired Installation  not found");
                throw new RoutineException("While using updateInstallation, desired Installation  not found");
            }
            // Possibly altered during long conversation
            Long storedInstallationVersion = storedInstallation.getVersion();

            if (!storedInstallationVersion.equals(cachedInstallationVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using updateInstallation, desired Installation was altered by another user ");
                throw new RoutineException(
                        "While using updateInstallation, desired Installation was altered by another user ");
            }

            // Both the same. Safe to update
            storedInstallation.setSoftwareVersion(updatedSoftwareVersion);

            hibSession.update(storedInstallation);
            hibSession.getTransaction().commit();

        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using updateInstallation, ", e);
            throw new WTFException("WTF error using updateInstallation");
        } finally {
            hibSession.close();
        }
    }

    @Override
    public List<Installation> readInstallationList(Session hibSession, String col, String order) {

        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Installation> query = cb.createQuery(Installation.class);

        Root<Installation> inst = query.from(Installation.class);
        Join<Installation, ServiceNode> serviceNodeJoin = inst.join("serviceNode");
        Join<Installation, Service> serviceJoin = inst.join("service");

        Order o = null;
        Path<?> p = null;
        if (col.equalsIgnoreCase("softwareVersion"))
            p = inst.get("softwareVersion");
        else if (col.equalsIgnoreCase("hostname"))
            p = serviceNodeJoin.get("hostname");
        else
            p = serviceJoin.get("serviceName");

        o = cb.desc(p);
        if (order.equalsIgnoreCase("ASC"))
            o = cb.asc(p);

        TypedQuery<Installation> typedQuery = hibSession.createQuery(query.select(inst).orderBy(o));
        return typedQuery.getResultList();
    }

    @Override
    public Installation readOneInstallation(Session hibSession, String serviceName, String hostname) {

        CriteriaBuilder cb = hibSession.getCriteriaBuilder();
        CriteriaQuery<Installation> query = cb.createQuery(Installation.class);

        Root<Installation> installatione = query.from(Installation.class);
        Join<Installation, ServiceNode> serviceNodeJoin = installatione.join("serviceNode");
        Join<Installation, Service> serviceJoin = installatione.join("service");

        List<Predicate> conditions = new ArrayList<Predicate>();
        conditions.add(cb.equal(serviceNodeJoin.get("hostname"), hostname));
        conditions.add(cb.equal(serviceJoin.get("serviceName"), serviceName));

        TypedQuery<Installation> typedQuery = hibSession
                .createQuery(query.select(installatione).where(conditions.toArray(new Predicate[conditions.size()])));

        Installation i = typedQuery.getSingleResult();
        return i;
    }

    @Override
    public void addInstallation(HttpServletRequest request) throws WTFException, RoutineException {

        HttpSession httpSession = null;
        SessionFactory fac = null;
        Session hibSession = null;

        httpSession = request.getSession();
        fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
        if (fac != null)
            hibSession = fac.openSession();
        if (hibSession == null)
            throw new RoutineException("Hibernate session unavailable");

        String hostname = request.getParameter("serviceNodeList");
        String serviceName = request.getParameter("serviceList");
        String softwareVersion = request.getParameter("softwareVersion");

        Installation installation = new Installation();

        try {
            hibSession.beginTransaction();
            ServiceNodeDao serviceNodeDao = (ServiceNodeDao) request.getSession().getAttribute("serviceNodeDao");
            ServiceNode sn = serviceNodeDao.readOneServiceNode(hibSession, hostname);
            if (sn == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addInstallation, desired ServiceNode not found");
                throw new RoutineException("While using addInstallation, desired ServiceNode not found");
            }
            ServiceDao serviceDao = (ServiceDao) request.getSession().getAttribute("serviceDao");
            Service s = serviceDao.readOneService(hibSession, serviceName);
            if (s == null) {
                // Possibly deleted during long conversation
                hibSession.getTransaction().rollback();
                logger.error("While using addInstallation, desired Service not found");
                throw new RoutineException("While using addInstallation, desired Service not found");
            }

            installation.setService(s);
            installation.setServiceNode(sn);
            installation.setSoftwareVersion(softwareVersion);
            hibSession.save(installation);
            hibSession.getTransaction().commit();
        } catch (PersistenceException e) {
            if (Util.isIdClash(e)) {
                hibSession.getTransaction().rollback();
                throw new RoutineException(
                        "While using addInstallation, the installationName conflicted with an existing installation");
            } else {
                logger.error("WTF error using addInstallation, ", e);
                throw new WTFException("WTF while using addInstallation");
            }
        }
        finally {
            hibSession.close();
        }

    }

    @Override
    public void deleteIndexedInstallation(HttpServletRequest request, Integer installationIndex)
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

        Installation cachedInstallation = null;
        Installation storedInstallation = null;
        try {
            hibSession.beginTransaction();

            cachedInstallation = ((ArrayList<Installation>) httpSession.getAttribute("installationList"))
                    .get(installationIndex);

            if (cachedInstallation == null) {
                logger.error("While using loadIndexedInstallation, undesired Installation not found");
                throw new RoutineException("While using loadIndexedInstallation, undesired Installation not found");
            }

            Long cachedVersion = cachedInstallation.getVersion();
            String serviceName = cachedInstallation.getService().getServiceName();
            String hostname = cachedInstallation.getServiceNode().getHostname();

            storedInstallation = readOneInstallation(hibSession, serviceName, hostname);

            // Possibly deleted during long conversation
            if (storedInstallation == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using deleteIndexedInstallation, undesired Installation not found");
                throw new RoutineException("While using deleteIndexedInstallation, undesired Installation not found");
            }

            Long storedVersion = storedInstallation.getVersion();
            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error(
                        "While using deleteIndexedInstallation, undesired Installation was altered by another user ");
                throw new RoutineException(
                        "While using deleteIndexedInstallation, undesired Installation was altered by another user ");
            }

            hibSession.delete(storedInstallation);
            hibSession.getTransaction().commit();

        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using deleteIndexedInstallation, ", ex);
            throw new WTFException("WTF error using deleteIndexedInstallation");
        } finally {
            hibSession.close();
        }
        return;
    }

    @Override
    public void loadIndexedInstallation(HttpServletRequest request, Integer installationIndex)
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

        Installation cachedInstallation = null;
        Installation storedInstallation = null;

        try {
            hibSession.beginTransaction();

            cachedInstallation = ((ArrayList<Installation>) httpSession.getAttribute("installationList"))
                    .get(installationIndex);
            if (cachedInstallation == null) {
                logger.error("While using loadIndexedInstallation, desired Installation not found");
                throw new RoutineException("While using loadIndexedInstallation, desired Installation not found");
            }
            Long cachedVersion = cachedInstallation.getVersion();
            String serviceName = cachedInstallation.getService().getServiceName();
            String hostname = cachedInstallation.getServiceNode().getHostname();

            storedInstallation = readOneInstallation(hibSession, serviceName, hostname);

            // Possibly deleted during long conversation
            if (storedInstallation == null) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedInstallation, desired Installation not found");
                throw new RoutineException("While using loadIndexedInstallation, desired Installation not found");
            }
            Long storedVersion = storedInstallation.getVersion();

            if (!storedVersion.equals(cachedVersion)) {
                hibSession.getTransaction().rollback();
                logger.error("While using loadIndexedInstallation, desired Installation was altered by another user ");
                throw new RoutineException(
                        "While using loadIndexedInstallation, desired Installation was altered by another user ");
            }

            hibSession.getTransaction().commit();
        } catch (HibernateException e) {
            hibSession.getTransaction().rollback();
            logger.error("WTF error using loadIndexedInstallation, ", e);
            throw new WTFException("WTF error using loadIndexedInstallation");
        } finally {
            hibSession.close();
        }
        httpSession.setAttribute("installation", storedInstallation);
    }

    @Override
    public void loadInstallations(HttpServletRequest request, String col, String order)
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

        List<Installation> installationList = new ArrayList<Installation>();
        try {

            hibSession.beginTransaction();
            InstallationDao installationDao = (InstallationDao) request.getSession().getAttribute("installationDao");
            installationList = installationDao.readInstallationList(hibSession, col, order);

            hibSession.getTransaction().commit();
        } catch (HibernateException ex) {
            hibSession.getTransaction().rollback();
            logger.error("Had an error using loadInstallations", ex);
            throw new WTFException("Had an error using loadInstallations");
        } finally {
            hibSession.close();
        }

        httpSession.setAttribute("installationList", installationList);
    }
}
