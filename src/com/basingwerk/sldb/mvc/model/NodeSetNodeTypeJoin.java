package com.basingwerk.sldb.mvc.model;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.basingwerk.sldb.mvc.exceptions.ModelException;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class NodeSetNodeTypeJoin {
    final static Logger logger = Logger.getLogger(NodeSetNodeTypeJoin.class);

    private String nodeSetName;
    private String nodeTypeName;
    private Integer nodeCount;
    private Integer cpu;
    private Integer slot;
    private Float hs06PerSlot;

    public NodeSetNodeTypeJoin(String nodeSetName, String nodeTypeName, Integer nodeCount, Integer cpu, Integer slot,
            Float hs06PerSlot, Float nodeSetHs06) {
        super();
        this.nodeSetName = nodeSetName;
        this.nodeTypeName = nodeTypeName;
        this.nodeCount = nodeCount;
        this.cpu = cpu;
        this.slot = slot;
        this.hs06PerSlot = hs06PerSlot;
        this.nodeSetHs06 = nodeSetHs06;
    }

    @Override
    public String toString() {
        return "NodeSetNodeTypeJoin [nodeSetHs06=" + nodeSetHs06 + ", nodeSetName=" + nodeSetName + ", nodeTypeName="
                + nodeTypeName + ", nodeCount=" + nodeCount + ", cpu=" + cpu + ", slot=" + slot + ", hs06PerSlot="
                + hs06PerSlot + "]" + "\n";
    }

    public String getNodeSetName() {
        return nodeSetName;
    }

    public void setNodeSetName(String nodeSetName) {
        this.nodeSetName = nodeSetName;
    }

    public String getNodeTypeName() {
        return nodeTypeName;
    }

    public void setNodeTypeName(String nodeTypeName) {
        this.nodeTypeName = nodeTypeName;
    }

    public Integer getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public Float getHs06PerSlot() {
        return hs06PerSlot;
    }

    public void setHs06PerSlot(Float hs06PerSlot) {
        this.hs06PerSlot = hs06PerSlot;
    }

    public Float getNodeSetHs06() {
        return nodeSetHs06;
    }

    public void setNodeSetHs06(Float nodeSetHs06) {
        this.nodeSetHs06 = nodeSetHs06;
    }

    private Float nodeSetHs06;

}
