package com.basingwerk.sldb.mvc.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "ClusterSet")
public class ClusterSet {

    @Version
    @Column(name="version")
    private long version;

    public long getVersion() {
        return version;
    }

    @Id
    private String clusterSetName;
    private String description;
    private String location;
    private double longitude;
    private double latitude;
    private String admin;

    @OneToMany(mappedBy = "clusterSet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Cluster> clusters = new HashSet<Cluster>();

    public Set<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(Set<Cluster> clusters) {
        this.clusters = clusters;
    }

    public String getClusterSetName() {
        return clusterSetName;
    }

    public void setClusterSetName(String clusterSetName) {
        this.clusterSetName = clusterSetName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double d) {
        this.longitude = d;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double d) {
        this.latitude = d;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public ClusterSet() {
    }

    public String toString() {
        return "ClusterSet [clusterSetName=" + clusterSetName + ", description=" + description + ", location="
                + location + ", longitude=" + longitude + ", latitude=" + latitude + ", admin=" + admin + "]";
    }
}
