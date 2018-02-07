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
@Table(name = "NodeSet")
public class NodeSet {

    @OneToMany(mappedBy = "nodeSet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapKey(name="nodeName")
    private Map<String, Node> nodes = new HashMap<String,Node>();
    
    public Map<String, Node> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, Node> nodes) {
        this.nodes = nodes;
    }

    public NodeSet() {
    }

    @Version
    @Column(name = "version")
    private long version;

    public long getVersion() {
        return version;
    }

    @Id
    private String nodeSetName;

    // This field is redundant, due to Nodes table.
    private Integer nodeCount;

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
    @JoinColumn(name = "nodeTypeName")
    private NodeType nodeType;

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeSetName() {
        return nodeSetName;
    }

    public void setNodeSetName(String nodeSetName) {
        this.nodeSetName = nodeSetName;
    }

    public Integer getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }

    public String toString() {
        return "NodeSet [nodeSetName=" + nodeSetName + ", nodeCount=" + nodeCount + ", cluster=" + cluster
                + ", nodeType=" + nodeType + "]";
    }

}
