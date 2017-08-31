package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.Installation;

public interface InstallationDao {

    void updateInstallation(HttpServletRequest request) throws WTFException, RoutineException;

    List<Installation> readInstallationList(Session hibSession, String col, String order);

    Installation readOneInstallation(Session hibSession, String serviceName, String hostname);

    void addInstallation(HttpServletRequest request) throws WTFException, RoutineException;

    void deleteIndexedInstallation(HttpServletRequest request, Integer installationIndex)
            throws WTFException, RoutineException;

    void loadIndexedInstallation(HttpServletRequest request, Integer installationIndex)
            throws WTFException, RoutineException;

    void loadInstallations(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

}