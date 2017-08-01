package com.basingwerk.sldb.mvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "Node")
public class Node {

    public Node() {
    }

    @Version
    @Column(name = "version")
    private long version;

    public long getVersion() {
        return version;
    }

    @Id
    private String nodeName;

    @ManyToOne
    @JoinColumn(name = "nodeSetName")
    private NodeSet nodeSet;

    public NodeSet getNodeSet() {
        return nodeSet;
    }

    public void setNodeSet(NodeSet nodeSet) {
        this.nodeSet = nodeSet;
    }
    @ManyToOne
    @JoinColumn(name = "state")
    private NodeState nodeState;

    public NodeState getNodeState() {
        return nodeState;
    }

    public void setNodeState(NodeState nodeState) {
        this.nodeState = nodeState;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    private String description;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "Node [nodeName=" + nodeName + ", nodeSet=" + nodeSet + ", description=" + description + "nodeState= " + nodeState  + "]";
    }
}
