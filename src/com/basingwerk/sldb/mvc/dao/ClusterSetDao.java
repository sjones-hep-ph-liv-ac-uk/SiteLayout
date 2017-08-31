package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.ClusterSet;

public interface ClusterSetDao {

    void updateClusterSet(HttpServletRequest request) throws WTFException, RoutineException;

    List<ClusterSet> readClusterSetList(Session hibSession, String col, String order);

    ClusterSet readOneClusterSet(Session hibSession, String clusterSetName);

    void addClusterSet(HttpServletRequest request) throws WTFException, RoutineException;

    void deleteClusterSet(HttpServletRequest request, String clusterSetName) throws WTFException, RoutineException;

    void loadClusterSets(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

    void loadIndexedClusterSet(HttpServletRequest request, Integer clusterSetIndex)
            throws WTFException, RoutineException;

}