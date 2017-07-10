package com.basingwerk.sldb.mvc.model;

import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Entity
@Table(name="NodeSet")
public class NodeSet {
    
    @Id
    private String nodeSetName;
    
    private Integer nodeCount;
    
    @ManyToOne
    @JoinColumn(name="clusterName")
    private Cluster cluster;
    
    public Cluster getCluster () {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster= cluster;
    }
    
    @ManyToOne
    @JoinColumn(name="nodeTypeName")
    private NodeType nodeType;
    
    public NodeType getNodeType () {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType= nodeType;
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

//    public String getCluster() {
//        return cluster;
//    }
//
//    public void setCluster(String cluster) {
//        this.cluster = cluster;
//    }

    public NodeSet(){
    }

    public String toString() {
        return "NodeSet [nodeSetName=" + nodeSetName + ", nodeCount=" + nodeCount + ", cluster=" + cluster
                + ", nodeType=" + nodeType + "]";
    }

    

}
