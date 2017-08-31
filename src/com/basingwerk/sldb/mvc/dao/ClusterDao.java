package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.Cluster;

public interface ClusterDao {

    void updateCluster(HttpServletRequest request) throws WTFException, RoutineException;

    List<Cluster> readClusterList(Session hibSession, String col, String order);

    List<Cluster> readClustersOfClusterSet(Session hibSession, String clusterSetName, String col, String order);

    Cluster readOneCluster(Session hibSession, String clusterName);

    void addCluster(HttpServletRequest request) throws RoutineException, WTFException;

    void deleteCluster(HttpServletRequest request, String clusterName) throws WTFException, RoutineException;

    void loadClustersOfClusterSet(HttpServletRequest request, String clusterSetName, String col, String order)
            throws RoutineException, WTFException;

    void loadClusters(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

    void loadIndexedCluster(HttpServletRequest request, Integer clusterIndex) throws WTFException, RoutineException;

}