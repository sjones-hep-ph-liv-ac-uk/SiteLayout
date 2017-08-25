package com.basingwerk.sldb.mvc.model;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DataAccessObject {
	final static Logger logger = Logger.getLogger(DataAccessObject.class);

	private static DataAccessObject instance = null;

	protected DataAccessObject() {
		// Exists only to defeat instantiation.
	}

	public static DataAccessObject getInstance() {
		if (instance == null) {
			instance = new DataAccessObject();
		}
		return instance;
	}

	public void setBaselineNodeType(HttpServletRequest request) throws RoutineException, WTFException {

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

			// Full Java style query! This is what it takes, now, to run a bit
			// of SQL...
			CriteriaBuilder cb = hibSession.getCriteriaBuilder();
			CriteriaQuery<NodeType> q = cb.createQuery(NodeType.class);
			Root<NodeType> nt = q.from(NodeType.class);
			q.select(nt);
			q.distinct(true);
			TypedQuery<NodeType> ntq = hibSession.createQuery(q);
			nodeTypes = ntq.getResultList();
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

	public void loadClusters(HttpServletRequest request, String col, String order)
			throws RoutineException, WTFException {

		HttpSession httpSession = null;
		SessionFactory fac = null;
		Session hibSession = null;

		List<Cluster> clusterList = new ArrayList<Cluster>();

		httpSession = request.getSession();
		fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
		if (fac != null)
			hibSession = fac.openSession();
		if (hibSession == null)
			throw new RoutineException("Hibernate session unavailable");

		clusterList = null;
		try {

			hibSession.beginTransaction();

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
			clusterList = q.getResultList();

		} catch (HibernateException ex) {
			hibSession.getTransaction().rollback();
			logger.error("WTF error using loadClusters, ", ex);
			throw new WTFException("WTF error using loadClusters");
		} finally {
			hibSession.close();
		}
		httpSession.setAttribute("clusterList", clusterList);
	}

	public void loadHostSystems(HttpServletRequest request, String col, String order)
			throws RoutineException, WTFException {

		List<HostSystem> hostSystemList = new ArrayList<HostSystem>();

		HttpSession httpSession = null;
		SessionFactory fac = null;
		Session hibSession = null;

		httpSession = request.getSession();
		fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
		if (fac != null)
			hibSession = fac.openSession();
		if (hibSession == null)
			throw new RoutineException("Hibernate session unavailable");

		Order ord = org.hibernate.criterion.Order.desc(col);
		if (order.equalsIgnoreCase("ASC")) {
			ord = org.hibernate.criterion.Order.asc(col);
		}

		hostSystemList = null;
		try {

			hibSession.beginTransaction();

			// Full Java-style query
			CriteriaBuilder cb = hibSession.getCriteriaBuilder();
			CriteriaQuery<HostSystem> q = cb.createQuery(HostSystem.class);
			Root<HostSystem> hs = q.from(HostSystem.class);
			q.select(hs);
			q.distinct(true);
			if (order.equalsIgnoreCase("ASC"))
				q.orderBy(cb.asc(hs.get(col)));
			else
				q.orderBy(cb.desc(hs.get(col)));
			TypedQuery<HostSystem> ntq = hibSession.createQuery(q);
			hostSystemList = ntq.getResultList();

		} catch (HibernateException ex) {
			hibSession.getTransaction().rollback();
			logger.error("WTF error using loadHostSystems, ", ex);
			throw new WTFException("WTF error using loadHostSystems");
		} finally {
			hibSession.close();
		}
		httpSession.setAttribute("hostSystemList", hostSystemList);
	}

	public void loadNodeStates(HttpServletRequest request, String col, String order)
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

		Order ord = org.hibernate.criterion.Order.desc(col);
		if (order.equalsIgnoreCase("ASC")) {
			ord = org.hibernate.criterion.Order.asc(col);
		}

		nodeStateList = null;
		try {

			hibSession.beginTransaction();
			nodeStateList = (ArrayList<NodeState>) hibSession.createCriteria(NodeState.class).addOrder(ord)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

			// Full Java-style query
			CriteriaBuilder cb = hibSession.getCriteriaBuilder();
			CriteriaQuery<NodeState> q = cb.createQuery(NodeState.class);
			Root<NodeState> ns = q.from(NodeState.class);
			q.select(ns);
			q.distinct(true);
			if (order.equalsIgnoreCase("ASC"))
				q.orderBy(cb.asc(ns.get(col)));
			else
				q.orderBy(cb.desc(ns.get(col)));
			TypedQuery<NodeState> nsq = hibSession.createQuery(q);
			nodeStateList = nsq.getResultList();

		} catch (HibernateException ex) {
			hibSession.getTransaction().rollback();
			logger.error("WTF error using loadNodeStates, ", ex);
			throw new WTFException("WTF error using loadNodeStates");
		} finally {
			hibSession.close();
		}
		httpSession.setAttribute("nodeStateList", nodeStateList);
	}

	public void loadClusterSets(HttpServletRequest request, String col, String order)
			throws RoutineException, WTFException {

		List<ClusterSet> clusterSetList = new ArrayList<ClusterSet>();

		HttpSession httpSession = null;
		SessionFactory fac = null;
		Session hibSession = null;

		httpSession = request.getSession();
		fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
		if (fac != null)
			hibSession = fac.openSession();
		if (hibSession == null)
			throw new RoutineException("Hibernate session unavailable");
		
		clusterSetList = null;
		
		try {

			hibSession.beginTransaction();
			// Full Java-style query
			CriteriaBuilder cb = hibSession.getCriteriaBuilder();
			CriteriaQuery<ClusterSet> q = cb.createQuery(ClusterSet.class);
			Root<ClusterSet> cs = q.from(ClusterSet.class);
			q.select(cs);
			q.distinct(true);
			if (order.equalsIgnoreCase("ASC"))
				q.orderBy(cb.asc(cs.get(col)));
			else
				q.orderBy(cb.desc(cs.get(col)));
			TypedQuery<ClusterSet> csq = hibSession.createQuery(q);
			clusterSetList = csq.getResultList();

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

	public void loadNodeSets(HttpServletRequest request, String col, String order)
			throws RoutineException, WTFException {

		List<NodeSet> nodeSetList = new ArrayList<NodeSet>();

		HttpSession httpSession = null;
		SessionFactory fac = null;
		Session hibSession = null;

		httpSession = request.getSession();
		fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
		if (fac != null)
			hibSession = fac.openSession();
		if (hibSession == null)
			throw new RoutineException("Hibernate session unavailable");

		nodeSetList = null;
		try {

			hibSession.beginTransaction();

			// Full Java-style query
			CriteriaBuilder cb = hibSession.getCriteriaBuilder();
			CriteriaQuery<NodeSet> q = cb.createQuery(NodeSet.class);
			Root<NodeSet> ns = q.from(NodeSet.class);
			q.select(ns);
			q.distinct(true);
			if (order.equalsIgnoreCase("ASC"))
				q.orderBy(cb.asc(ns.get(col)));
			else
				q.orderBy(cb.desc(ns.get(col)));
			TypedQuery<NodeSet> nsq = hibSession.createQuery(q);
			nodeSetList = nsq.getResultList();

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

	public void loadInstallations(HttpServletRequest request, String col, String order)
			throws RoutineException, WTFException {

		List<Installation> installationList = new ArrayList<Installation>();

		HttpSession httpSession = null;
		SessionFactory fac = null;
		Session hibSession = null;

		httpSession = request.getSession();
		fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
		if (fac != null)
			hibSession = fac.openSession();
		if (hibSession == null)
			throw new RoutineException("Hibernate session unavailable");

		installationList = null;
		try {

			hibSession.beginTransaction();
			// Full Java-style query
			CriteriaBuilder cb = hibSession.getCriteriaBuilder();
			CriteriaQuery<Installation> q = cb.createQuery(Installation.class);
			Root<Installation> i = q.from(Installation.class);
			q.select(i);
			q.distinct(true);
			if (order.equalsIgnoreCase("ASC"))
				q.orderBy(cb.asc(i.get(col)));
			else
				q.orderBy(cb.desc(i.get(col)));
			TypedQuery<Installation> iq = hibSession.createQuery(q);
			installationList = iq.getResultList();

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

	public void loadNodes(HttpServletRequest request, String col, String order) throws RoutineException, WTFException {
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

			// Full Java-style query
			CriteriaBuilder cb = hibSession.getCriteriaBuilder();
			CriteriaQuery<Node> q = cb.createQuery(Node.class);
			Root<Node> n = q.from(Node.class);
			q.select(n);
			q.distinct(true);
			if (order.equalsIgnoreCase("ASC"))
				q.orderBy(cb.asc(n.get(col)));
			else
				q.orderBy(cb.desc(n.get(col)));
			TypedQuery<Node> nq = hibSession.createQuery(q);
			nodeList = nq.getResultList();

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

	public void loadServiceNodes(HttpServletRequest request, String col, String order)
			throws RoutineException, WTFException {

		List<ServiceNode> serviceNodeList = new ArrayList<ServiceNode>();

		HttpSession httpSession = null;
		SessionFactory fac = null;
		Session hibSession = null;

		httpSession = request.getSession();
		fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
		if (fac != null)
			hibSession = fac.openSession();
		if (hibSession == null)
			throw new RoutineException("Hibernate session unavailable");

		serviceNodeList = null;
		try {

			hibSession.beginTransaction();

			// Full Java-style query
			CriteriaBuilder cb = hibSession.getCriteriaBuilder();
			CriteriaQuery<ServiceNode> q = cb.createQuery(ServiceNode.class);
			Root<ServiceNode> sn = q.from(ServiceNode.class);
			q.select(sn);
			q.distinct(true);
			if (order.equalsIgnoreCase("ASC"))
				q.orderBy(cb.asc(sn.get(col)));
			else
				q.orderBy(cb.desc(sn.get(col)));
			TypedQuery<ServiceNode> snq = hibSession.createQuery(q);
			serviceNodeList = snq.getResultList();

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

	public void loadServices(HttpServletRequest request, String col, String order)
			throws RoutineException, WTFException {

		List<Service> serviceList = new ArrayList<Service>();

		HttpSession httpSession = null;
		SessionFactory fac = null;
		Session hibSession = null;

		httpSession = request.getSession();
		fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
		if (fac != null)
			hibSession = fac.openSession();
		if (hibSession == null)
			throw new RoutineException("Hibernate session unavailable");

		serviceList = null;
		try {

			hibSession.beginTransaction();

			// Full Java-style query
			CriteriaBuilder cb = hibSession.getCriteriaBuilder();
			CriteriaQuery<Service> q = cb.createQuery(Service.class);
			Root<Service> s = q.from(Service.class);
			q.select(s);
			q.distinct(true);
			if (order.equalsIgnoreCase("ASC"))
				q.orderBy(cb.asc(s.get(col)));
			else
				q.orderBy(cb.desc(s.get(col)));
			TypedQuery<Service> sq = hibSession.createQuery(q);
			serviceList = sq.getResultList();

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

	public void loadNodeTypes(HttpServletRequest request, String col, String order)
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

			// Full Java-style query
			CriteriaBuilder cb = hibSession.getCriteriaBuilder();
			CriteriaQuery<NodeType> q = cb.createQuery(NodeType.class);
			Root<NodeType> nt = q.from(NodeType.class);
			q.select(nt);
			q.distinct(true);
			if (order.equalsIgnoreCase("ASC"))
				q.orderBy(cb.asc(nt.get(col)));
			else
				q.orderBy(cb.desc(nt.get(col)));
			TypedQuery<NodeType> ntq = hibSession.createQuery(q);
			nodeTypeList = ntq.getResultList();

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

	public void loadIndexedCluster(HttpServletRequest request, Integer clusterIndex)
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

			storedCluster = (Cluster) hibSession.createCriteria(Cluster.class)
					.add(Restrictions.eq("clusterName", cachedCluster.getClusterName())).uniqueResult();
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

	public void loadIndexedClusterSet(HttpServletRequest request, Integer clusterSetIndex)
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

			storedClusterSet = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
					.add(Restrictions.eq("clusterSetName", cachedClusterSet.getClusterSetName())).uniqueResult();
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

	public void loadIndexedNodeSet(HttpServletRequest request, Integer nodeSetIndex)
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
			storedNodeSet = (NodeSet) hibSession.createCriteria(NodeSet.class)
					.add(Restrictions.eq("nodeSetName", cachedNodeSet.getNodeSetName())).uniqueResult();
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

	public void loadIndexedNodeType(HttpServletRequest request, Integer nodeTypeIndex)
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

			storedNodeType = (NodeType) hibSession.createCriteria(NodeType.class)
					.add(Restrictions.eq("nodeTypeName", cachedNodeType.getNodeTypeName())).uniqueResult();
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

	public void loadIndexedNode(HttpServletRequest request, Integer nodeIndex) throws WTFException, RoutineException {

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
			storedNode = (Node) hibSession.createCriteria(Node.class)
					.add(Restrictions.eq("nodeName", cachedNode.getNodeName())).uniqueResult();
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

	public void loadIndexedServiceNode(HttpServletRequest request, Integer serviceNodeIndex)
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
			logger.error("SJDEBUG: serviceNodeIndex -- " + serviceNodeIndex);
			logger.error("SJDEBUG: serviceNode -- " + httpSession.getAttribute("serviceNodeList"));

			cachedServiceNode = ((ArrayList<ServiceNode>) httpSession.getAttribute("serviceNodeList"))
					.get(serviceNodeIndex);

			if (cachedServiceNode == null) {
				logger.error("While using loadIndexedServiceNode, desired ServiceNode not found");
				throw new RoutineException("While using loadIndexedServiceNode, desired ServiceNode not found");
			}

			Long cachedVersion = cachedServiceNode.getVersion();
			storedServiceNode = (ServiceNode) hibSession.createCriteria(ServiceNode.class)
					.add(Restrictions.eq("hostname", cachedServiceNode.getHostname())).uniqueResult();
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

	public void toggleCheckedNodes(HttpServletRequest request) throws RoutineException, WTFException {

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
				Node node = (Node) hibSession.createCriteria(Node.class).add(Restrictions.eq("nodeName", nodeName))
						.uniqueResult();
				if (node.getNodeState().getState().equalsIgnoreCase("ONLINE")) {
					NodeState s = (NodeState) hibSession.createCriteria(NodeState.class)
							.add(Restrictions.eq("state", "OFFLINE")).uniqueResult();
					node.setNodeState(s);
				} else {
					NodeState s = (NodeState) hibSession.createCriteria(NodeState.class)
							.add(Restrictions.eq("state", "ONLINE")).uniqueResult();
					node.setNodeState(s);
				}
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

	public void toggleIndexedNode(HttpServletRequest request, Integer nodeIndex) throws RoutineException, WTFException {

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
			Node cn = ((ArrayList<Node>) httpSession.getAttribute("node")).get(nodeIndex);

			if (cn.getNodeState().getState().equalsIgnoreCase("ONLINE")) {
				NodeState s = (NodeState) hibSession.createCriteria(NodeState.class)
						.add(Restrictions.eq("state", "OFFLINE")).uniqueResult();
				cn.setNodeState(s);
			} else {
				NodeState s = (NodeState) hibSession.createCriteria(NodeState.class)
						.add(Restrictions.eq("state", "ONLINE")).uniqueResult();
				cn.setNodeState(s);
			}

			hibSession.save(cn);
			hibSession.getTransaction().commit();
		} catch (HibernateException ex) {
			hibSession.getTransaction().rollback();
			logger.warn("Some minor error occured toggling a node state", ex);
			// throw new WTFException("WTF error using toggleIndexedNode");
		} finally {
			hibSession.close();
		}
	}

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

			ClusterSet cs = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
					.add(Restrictions.eq("clusterSetName", clusterSetName)).uniqueResult();

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

	public void addClusterSet(HttpServletRequest request) throws WTFException, RoutineException {

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

	public void addNodeType(HttpServletRequest request) throws WTFException, RoutineException {

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

	public void addNodeSet(HttpServletRequest request) throws WTFException, RoutineException {

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
			Cluster c = (Cluster) hibSession.createCriteria(Cluster.class)
					.add(Restrictions.eq("clusterName", clusterName)).uniqueResult();
			if (c == null) {
				// Possibly deleted during long conversation
				hibSession.getTransaction().rollback();
				logger.error("While using addNodeSet, desired Cluster not found");
				throw new RoutineException("While using addNodeSet, desired Cluster not found");
			}

			NodeType nodeType = (NodeType) hibSession.createCriteria(NodeType.class)
					.add(Restrictions.eq("nodeTypeName", nodeTypeName)).uniqueResult();
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
		} catch (ConstraintViolationException e) {
			hibSession.getTransaction().rollback();
			logger.error("While using addNodeSet, the nodeSetName conflicted with an existing nodeSet");
			throw new RoutineException("While using addNodeSet, the nodeSetName conflicted with an existing nodeSet");
		} catch (HibernateException e) {
			hibSession.getTransaction().rollback();
			logger.error("WTF error using addNodeSet, ", e);
			throw new WTFException("WTF error using addNodeSet");
		} finally {
			hibSession.close();
		}
	}

	public void addNode(HttpServletRequest request) throws WTFException, RoutineException {

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

			NodeSet nodeSet = (NodeSet) hibSession.createCriteria(NodeSet.class)
					.add(Restrictions.eq("nodeSetName", nodeSetName)).uniqueResult();
			if (nodeSet == null) {
				// Possibly deleted during long conversation
				hibSession.getTransaction().rollback();
				logger.error("While using addNode, desired NodeSet not found");
				throw new RoutineException("While using addNode, desired NodeSet not found");
			}

			NodeState nodeState = (NodeState) hibSession.createCriteria(NodeState.class)
					.add(Restrictions.eq("state", nodeStateName)).uniqueResult();
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

	public void addServiceNode(HttpServletRequest request) throws WTFException, RoutineException {

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

			Cluster cl = (Cluster) hibSession.createCriteria(Cluster.class).add(Restrictions.eq("clusterName", cluster))
					.uniqueResult();
			if (cl == null) {
				// Possibly deleted during long conversation
				hibSession.getTransaction().rollback();
				logger.error("While using addServiceNode, desired Cluster not found");
				throw new RoutineException("While using addServiceNode, desired Cluster not found");
			}
			HostSystem hs = (HostSystem) hibSession.createCriteria(HostSystem.class)
					.add(Restrictions.eq("hostname", hostSystem)).uniqueResult();
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

			ServiceNode sn = (ServiceNode) hibSession.createCriteria(ServiceNode.class)
					.add(Restrictions.eq("hostname", hostname)).uniqueResult();
			if (sn == null) {
				// Possibly deleted during long conversation
				hibSession.getTransaction().rollback();
				logger.error("While using addInstallation, desired ServiceNode not found");
				throw new RoutineException("While using addInstallation, desired ServiceNode not found");
			}
			Service s = (Service) hibSession.createCriteria(Service.class)
					.add(Restrictions.eq("serviceName", serviceName)).uniqueResult();
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
		} catch (ConstraintViolationException e) {
			hibSession.getTransaction().rollback();
			logger.error("While using addInstallation, the new object conflicted with an existing installation");
			throw new RoutineException(
					"While using addInstallation, the new object conflicted with an existing installation");
		} catch (HibernateException e) {
			hibSession.getTransaction().rollback();
			logger.error("WTF error using addInstallation, ", e);
			throw new WTFException("WTF error using addInstallation");
		} finally {
			hibSession.close();
		}
	}

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
			Cluster cluster = (Cluster) hibSession.createCriteria(Cluster.class)
					.add(Restrictions.eq("clusterName", clusterName)).uniqueResult();
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

	public void deleteClusterSet(HttpServletRequest request, String clusterSetName)
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

		ClusterSet clusterSet = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
				.add(Restrictions.eq("clusterSetName", clusterSetName)).uniqueResult();
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

	public void deleteNodeSet(HttpServletRequest request, String nodeSetName) throws WTFException, RoutineException {

		HttpSession httpSession = null;
		SessionFactory fac = null;
		Session hibSession = null;

		httpSession = request.getSession();
		fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
		if (fac != null)
			hibSession = fac.openSession();
		if (hibSession == null)
			throw new RoutineException("Hibernate session unavailable");

		NodeSet nodeSet = (NodeSet) hibSession.createCriteria(NodeSet.class)
				.add(Restrictions.eq("nodeSetName", nodeSetName)).uniqueResult();

		if (nodeSet == null) {
			logger.error("While using deleteNodeSet, undesired NodeSet not found");
			throw new RoutineException("While using deleteNodeSet, undesired NodeSet not found");
		}
		try {
			hibSession.beginTransaction();
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

	public void deleteInstallation(HttpServletRequest request, String serviceName, String hostname)
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

		Installation installation = (Installation) hibSession.createCriteria(Installation.class)
				.add(Restrictions.eq("serviceName", serviceName)).add(Restrictions.eq("hostname", hostname))
				.uniqueResult();

		if (installation == null) {
			logger.error("While using deleteInstallation, undesired Installation not found");
			throw new RoutineException("While using deleteInstallation, undesired Installation not found");
		}
		try {
			hibSession.beginTransaction();
			hibSession.delete(installation);
			hibSession.getTransaction().commit();
		} catch (HibernateException ex) {
			// Possibly deleted during long conversation
			hibSession.getTransaction().rollback();
			logger.error("While using deleteInstallation, undesired Installation not found");
			throw new RoutineException("While using deleteInstallation, undesired Installation not found");
		} finally {
			hibSession.close();
		}
	}

	public void deleteNode(HttpServletRequest request, String nodeName) throws WTFException, RoutineException {

		HttpSession httpSession = null;
		SessionFactory fac = null;
		Session hibSession = null;

		httpSession = request.getSession();
		fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
		if (fac != null)
			hibSession = fac.openSession();
		if (hibSession == null)
			throw new RoutineException("Hibernate session unavailable");

		Node node = (Node) hibSession.createCriteria(Node.class).add(Restrictions.eq("nodeName", nodeName))
				.uniqueResult();

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

	public void deleteServiceNode(HttpServletRequest request, String hostname) throws WTFException, RoutineException {

		HttpSession httpSession = null;
		SessionFactory fac = null;
		Session hibSession = null;

		httpSession = request.getSession();
		fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
		if (fac != null)
			hibSession = fac.openSession();
		if (hibSession == null)
			throw new RoutineException("Hibernate session unavailable");

		ServiceNode serviceNode = (ServiceNode) hibSession.createCriteria(ServiceNode.class)
				.add(Restrictions.eq("hostname", hostname)).uniqueResult();

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

	public void deleteNodeType(HttpServletRequest request, String nodeTypeName) throws WTFException, RoutineException {

		HttpSession httpSession = null;
		SessionFactory fac = null;
		Session hibSession = null;

		httpSession = request.getSession();
		fac = (SessionFactory) httpSession.getAttribute("sessionFactory");
		if (fac != null)
			hibSession = fac.openSession();
		if (hibSession == null)
			throw new RoutineException("Hibernate session unavailable");

		NodeType nodeType = (NodeType) hibSession.createCriteria(NodeType.class)
				.add(Restrictions.eq("nodeTypeName", nodeTypeName)).uniqueResult();
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

			storedCluster = (Cluster) hibSession.createCriteria(Cluster.class)
					.add(Restrictions.eq("clusterName", updatedClusterName)).uniqueResult();
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
			ClusterSet newClusterSet = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
					.add(Restrictions.eq("clusterSetName", updatedClusterSetName)).uniqueResult();
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

	public void updateNodeSet(HttpServletRequest request) throws WTFException, RoutineException {

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

			storedNodeSet = (NodeSet) hibSession.createCriteria(NodeSet.class)
					.add(Restrictions.eq("nodeSetName", updatedNodeSetName)).uniqueResult();
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

			Cluster cluster = (Cluster) hibSession.createCriteria(Cluster.class)
					.add(Restrictions.eq("clusterName", updatedClusterName)).uniqueResult();
			if (cluster == null) {
				// Possibly deleted during long conversation
				hibSession.getTransaction().rollback();
				logger.error("While using updateNodeSet, desired Cluster  not found");
				throw new RoutineException("While using updateNodeSet, desired Cluster not found");
			}
			storedNodeSet.setCluster(cluster);

			NodeType nodeType = (NodeType) hibSession.createCriteria(NodeType.class)
					.add(Restrictions.eq("nodeTypeName", updatedNodeTypeName)).uniqueResult();
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

	public void updateNode(HttpServletRequest request) throws WTFException, RoutineException {

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

			storedNode = (Node) hibSession.createCriteria(Node.class).add(Restrictions.eq("nodeName", updatedNodeName))
					.uniqueResult();

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

			NodeSet nodeSet = (NodeSet) hibSession.createCriteria(NodeSet.class)
					.add(Restrictions.eq("nodeSetName", updatedNodeSet)).uniqueResult();
			if (nodeSet == null) {
				// Possibly deleted during long conversation
				hibSession.getTransaction().rollback();
				logger.error("While using updateNode, desired nodeSet  not found");
				throw new RoutineException("While using updateNode, desired nodeSet not found");
			}
			storedNode.setNodeSet(nodeSet);

			NodeState nodeState = (NodeState) hibSession.createCriteria(NodeState.class)
					.add(Restrictions.eq("state", updatedNodeState)).uniqueResult();
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

	public void updateNodeType(HttpServletRequest request) throws WTFException, RoutineException {

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

			storedNodeType = (NodeType) hibSession.createCriteria(NodeType.class)
					.add(Restrictions.eq("nodeTypeName", updatedNodeTypeName)).uniqueResult();
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

	public void updateClusterSet(HttpServletRequest request) throws WTFException, RoutineException {

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

			storedClusterSet = (ClusterSet) hibSession.createCriteria(ClusterSet.class)
					.add(Restrictions.eq("clusterSetName", updatedClusterSetName)).uniqueResult();
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

	public void updateServiceNode(HttpServletRequest request) throws WTFException, RoutineException {

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

			storedServiceNode = (ServiceNode) hibSession.createCriteria(ServiceNode.class)
					.add(Restrictions.eq("hostname", updatedHostname)).uniqueResult();

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

			Cluster cluster = (Cluster) hibSession.createCriteria(Cluster.class)
					.add(Restrictions.eq("clusterName", updatedCluster)).uniqueResult();
			if (cluster == null) {
				// Possibly deleted during long conversation
				hibSession.getTransaction().rollback();
				logger.error("While using updateServiceNode, desired Cluster  not found");
				throw new RoutineException("While using updateServiceNode, desired Cluster not found");
			}
			storedServiceNode.setCluster(cluster);

			HostSystem hostSystem = (HostSystem) hibSession.createCriteria(HostSystem.class)
					.add(Restrictions.eq("hostname", updatedHostSystem)).uniqueResult();
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

			storedInstallation = (Installation) hibSession.createCriteria(Installation.class)
					.add(Restrictions.eq("service.serviceName", serviceName))
					.add(Restrictions.eq("serviceNode.hostname", hostname)).uniqueResult();

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

			storedInstallation = (Installation) hibSession.createCriteria(Installation.class)
					.add(Restrictions.eq("service.serviceName", serviceName))
					.add(Restrictions.eq("serviceNode.hostname", hostname)).uniqueResult();

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
			String updatedService = request.getParameter("service");
			String updatedServiceNode = request.getParameter("serviceNode");
			String updatedSoftwareVersion = request.getParameter("softwareVersion");

			Installation storedInstallation = null;
			Installation cachedInstallation = (Installation) httpSession.getAttribute("installation");

			if (cachedInstallation == null) {
				hibSession.getTransaction().rollback();
				logger.error("While using updateInstallation, desired Installation not found");
				throw new RoutineException("While using updateInstallation, desired Installation not found");
			}
			Long cachedInstallationVersion = cachedInstallation.getVersion();
			String serviceName = cachedInstallation.getService().getServiceName();
			String hostname = cachedInstallation.getServiceNode().getHostname();

			storedInstallation = (Installation) hibSession.createCriteria(Installation.class)
					.add(Restrictions.eq("service.serviceName", serviceName))
					.add(Restrictions.eq("serviceNode.hostname", hostname)).uniqueResult();

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

			// Full Java-style query
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
			clusterList = hibSession.createQuery(q).getResultList();

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

	public void logout(HttpServletRequest request) {
		HttpSession httpSession = null;

		httpSession = request.getSession();
		httpSession.invalidate();
		return;
	}
}
