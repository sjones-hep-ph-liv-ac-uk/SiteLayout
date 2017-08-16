package com.basingwerk.sldb.mvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "ServiceNode")
public class ServiceNode {

    public ServiceNode() {
    }
    
    private long cpu; 
    private long mem ;
    private String os ;            
    private String kernel;         
    private String service;        
    private String comment ;       
    

    @Version
    @Column(name = "version")
    private long version;

    public long getVersion() {
        return version;
    }

    @Id
    private String hostname;

    
    
    @ManyToOne
    @JoinColumn(name = "clusterName")
    private Cluster cluster;

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    
    @ManyToOne
    @JoinColumn(name = "hostSystemName")
    private HostSystem hostSystem;

    public HostSystem getHostSystem() {
        return hostSystem;
    }

    public void setHostSystem(HostSystem hostSystem) {
        this.hostSystem = hostSystem;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    
public long getCpu() {
    return cpu;
}

public void setCpu(long cpu) {
    this.cpu = cpu;
}

public long getMem() {
    return mem;
}

public void setMem(long mem) {
    this.mem = mem;
}

public String getOs() {
    return os;
}

public void setOs(String os) {
    this.os = os;
}

public String getKernel() {
    return kernel;
}

public void setKernel(String kernel) {
    this.kernel = kernel;
}

public String getService() {
    return service;
}

public void setService(String service) {
    this.service = service;
}

public String getComment() {
    return comment;
}

public void setComment(String comment) {
    this.comment = comment;
}

public void setVersion(long version) {
    this.version = version;
}




}
