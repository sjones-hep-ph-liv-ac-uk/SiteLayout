package com.basingwerk.sldb.mvc.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;
import com.basingwerk.sldb.mvc.model.Service;

public interface ServiceDao {

    Service readOneService(Session hibSession, String serviceName);

    List<Service> readServiceList(Session hibSession, String col, String order);

    void loadServices(HttpServletRequest request, String col, String order) throws RoutineException, WTFException;

}