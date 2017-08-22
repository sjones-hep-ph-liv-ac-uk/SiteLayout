package com.basingwerk.sldb.mvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@IdClass(KeyOfInstallation.class)
@Table(name = "Installation")
public class Installation {

    private String softwareVersion;
    
    public ServiceNode getServiceNode() {
        return serviceNode;
    }

    public void setServiceNode(ServiceNode serviceNode) {
        this.serviceNode = serviceNode;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "hostname")
    private ServiceNode serviceNode;

    @Id
    @ManyToOne
    @JoinColumn(name = "serviceName")
    private Service service;
    
    @Version
    @Column(name = "version")
    private long version;

    
    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Installation() {
    }

}



