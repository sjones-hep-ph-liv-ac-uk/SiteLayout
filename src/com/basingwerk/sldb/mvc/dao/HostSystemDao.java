package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.HostSystem;

public interface HostSystemDao {

    List<HostSystem> readHostSystemList(Session hibSession, String col, String order);

    HostSystem readOneHostSystem(Session hibSession, String hostname);

    void loadHostSystems(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

}