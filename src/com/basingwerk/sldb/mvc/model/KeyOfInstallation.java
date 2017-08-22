package com.basingwerk.sldb.mvc.model;

import java.io.Serializable;


public class KeyOfInstallation implements Serializable {

    protected Service service;
    protected ServiceNode serviceNode;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public ServiceNode getServiceNode() {
        return serviceNode;
    }

    public void setServiceNode(ServiceNode serviceNode) {
        this.serviceNode = serviceNode;
    }

    public KeyOfInstallation() {
    }

}
