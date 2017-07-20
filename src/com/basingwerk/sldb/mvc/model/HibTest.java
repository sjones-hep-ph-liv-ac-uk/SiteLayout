package com.basingwerk.sldb.mvc.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.basingwerk.sldb.mvc.model.Cluster;
import com.basingwerk.sldb.mvc.model.ClusterSet;
import com.basingwerk.sldb.mvc.model.NodeSet;
import com.basingwerk.sldb.mvc.model.NodeType;

public class HibTest {
    
}
//public class HibTest {
//
//    public HibTest() {
//    }
//
//    public static void main(String[] args) {
//        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//        Session session = sessionFactory.openSession();
//
//        //
//        ClusterSet clusterSetA = new ClusterSet();
//        clusterSetA.setClusterSetName("TESTCLUSTERSETA");
//        clusterSetA.setDescription("Some cluster set");
//        clusterSetA.setLocation("Liverpool");
//        clusterSetA.setLatitude(10.0);
//        clusterSetA.setLongitude(20.0);
//        clusterSetA.setAdmin("sjones@hep.ph.liv.ac.uk");
//
//        Cluster cluster1 = new Cluster();
//        cluster1.setClusterName("MYCLUSTER1");
//        cluster1.setDescr("First Cluster");
//        clusterSetA.getClusters().add(cluster1);
//        cluster1.setClusterSet(clusterSetA);
//
//        Cluster cluster2 = new Cluster();
//        cluster2.setClusterName("MYCLUSTER2");
//        cluster2.setDescr("Second Cluster");
//        clusterSetA.getClusters().add(cluster2);
//        cluster2.setClusterSet(clusterSetA);
//
//        ClusterSet clusterSetB = new ClusterSet();
//        clusterSetB.setClusterSetName("TESTCLUSTERSETB");
//        clusterSetB.setDescription("Some other cluster set");
//        clusterSetB.setLocation("Liverpool");
//        clusterSetB.setLatitude(10.0);
//        clusterSetB.setLongitude(20.0);
//        clusterSetB.setAdmin("sjones@hep.ph.liv.ac.uk");
//
//        Cluster cluster3 = new Cluster();
//        cluster3.setClusterName("MYCLUSTER3");
//        cluster3.setDescr("Third  Cluster");
//        clusterSetB.getClusters().add(cluster3);
//        cluster3.setClusterSet(clusterSetB);
//
//        // Cluster cluster4 = new Cluster();
//        // cluster4.setClusterName("MYCLUSTER4");
//        // cluster4.setDescr("4th Cluster");
//        // clusterSetB.getClusters().add(cluster4);
//        // cluster4.setClusterSet(clusterSetB);
//
//        session.beginTransaction();
//        session.save(clusterSetA);
//        session.save(clusterSetB);
//        session.getTransaction().commit();
//
//        System.out.println("Done save");
//
//        session = sessionFactory.openSession();
//        ClusterSet cs = (ClusterSet) session.get(com.basingwerk.sldb.mvc.model.ClusterSet.class, "TESTCLUSTERSETB");
//        System.out.println(cs.getClusters().size());
//
//        NodeType nodeType = new NodeType();
//        nodeType.setNodeTypeName("FASTNODE");
//        nodeType.setCpu(2);
//        nodeType.setSlot(8);
//        nodeType.setHs06PerSlot(10.5);
//        nodeType.setMemPerNode(30.0);
//
//        session.beginTransaction();
//        session.save(nodeType);
//        session.getTransaction().commit();
//
//        NodeSet ns = new NodeSet();
//        ns.setNodeCount(10);
//        ns.setNodeSetName("SPECIALNODESET");
//        ns.setNodeType(nodeType);
//        ns.setCluster(cluster3);
//
//        session.beginTransaction();
//        session.save(ns);
//        session.getTransaction().commit();
//
//    }
//}
