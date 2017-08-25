package com.basingwerk.sldb.mvc.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "Service")
public class Service {


    @Id
    private String serviceName;

    // Fields
    private String provider;
    // private String softwareVersion;

    @Version
    @Column(name = "version")
    private long version;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKey(name = "service")
    private Map<String, Installation> installations = new HashMap<String, Installation>();

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Map<String, Installation> getInstallations() {
        return installations;
    }

    public void setInstallations(Map<String, Installation> installations) {
        this.installations = installations;
    }

    public Service() {
    }
    @Override
    public String toString() {
        return "Service [serviceName=" + serviceName + ", provider=" + provider + ", version=" + version
                + ", installations=" + installations + "]";
    }


}
