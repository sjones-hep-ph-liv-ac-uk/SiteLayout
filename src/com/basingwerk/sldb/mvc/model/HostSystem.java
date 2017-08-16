package com.basingwerk.sldb.mvc.model;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Version;

@Entity
public class HostSystem {

    
    public HostSystem() {
    }

    private long cpu;
    private long mem;
    private String os;
    private String kernel;
    private String comment;

    @Version
    @Column(name = "version")
    private long version;

    public long getVersion() {
        return version;
    }

    @Id
    private String hostname;

    @OneToMany(mappedBy = "hostSystem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKey(name = "hostname")
    private Map<String, ServiceNode> serviceNodes = new HashMap<String, ServiceNode>();

    public Map<String, ServiceNode> getServiceNodes() {
        return serviceNodes;
    }

    public void setServiceNodes(Map<String, ServiceNode> nodes) {
        this.serviceNodes = nodes;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String state) {
        this.hostname = state;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String toString() {
        return hostname;
    }
}
