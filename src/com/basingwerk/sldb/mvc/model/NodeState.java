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
public class NodeState {

    @Version
    @Column(name = "version")
    private long version;

    public long getVersion() {
        return version;
    }

    @Id
    private String state;

    @OneToMany(mappedBy = "nodeState", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKey(name="nodeName")
    private Map<String,Node> nodes = new HashMap<String,Node>();

    public Map<String,Node> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String,Node> nodes) {
        this.nodes = nodes;
    }

    public String getstate() {
        return state;
    }

    public void setstate(String state) {
        this.state = state;
    }

    public NodeState() {
    }

    public String toString() {
        return state;
    }
}
