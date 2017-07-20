package com.basingwerk.sldb.mvc.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Version;

@Entity
public class NodeType {

    @Version
    @Column(name = "version")
    private long version;

    public long getVersion() {
        return version;
    }

    @Id
    private String nodeTypeName;

    private int cpu;
    private int slot;
    private double hs06PerSlot;
    private double memPerNode;

    @OneToMany(mappedBy = "nodeType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKey(name="nodeSetName")
    private Map<String,NodeSet> nodeSets = new HashMap<String,NodeSet>();

    public Map<String,NodeSet> getNodeSets() {
        return nodeSets;
    }

    public void setNodeSets(Map<String,NodeSet> nodeSets) {
        this.nodeSets = nodeSets;
    }

    public String getNodeTypeName() {
        return nodeTypeName;
    }

    public void setNodeTypeName(String nodeTypeName) {
        this.nodeTypeName = nodeTypeName;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public double getHs06PerSlot() {
        return hs06PerSlot;
    }

    public void setHs06PerSlot(double hs06PerSlot) {
        this.hs06PerSlot = hs06PerSlot;
    }

    public double getMemPerNode() {
        return memPerNode;
    }

    public void setMemPerNode(double memPerNode) {
        this.memPerNode = memPerNode;
    }

    public NodeType() {
    }

    public String toString() {
        String result;
        result = nodeTypeName + Integer.toString(cpu) + Integer.toString(slot) + Double.toString(hs06PerSlot)
                + Double.toString(memPerNode);
        return result;
    }

}
