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
@Table(name = "Cluster")

public class Cluster {

    @Version
    @Column(name = "version")
    private long version;

    public long getVersion() {
        return version;
    }

    @Id
    private String clusterName;

    private String descr;

    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKey(name="nodeSetName")
    private Map<String,NodeSet> nodeSets = new HashMap<String,NodeSet>();

    public Map<String,NodeSet> getNodeSets() {
        return nodeSets;
    }

    public void setNodeSets(Map<String,NodeSet> nodeSets) {
        this.nodeSets = nodeSets;
    }

    @ManyToOne
    @JoinColumn(name = "clusterSetName")
    private ClusterSet clusterSet;

    public ClusterSet getClusterSet() {
        return clusterSet;
    }

    public void setClusterSet(ClusterSet clusterSet) {
        this.clusterSet = clusterSet;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Cluster() {
    }

    public String toString() {
        return "Cluster [clusterName=" + clusterName + ", descr=" + descr + ", clusterSetName=" + clusterSet + "]";
    }

}
