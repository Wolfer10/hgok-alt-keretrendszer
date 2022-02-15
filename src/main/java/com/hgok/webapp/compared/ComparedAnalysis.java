package com.hgok.webapp.compared;

import java.sql.Timestamp;
import java.util.List;
import javax.annotation.Generated;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class ComparedAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Timestamp validationTime;

    @SerializedName("directed")
    @Expose
    public Boolean directed;
    @SerializedName("multigraph")
    @Expose
    public Boolean multigraph;

    @SerializedName("nodes")
    @Expose
    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    public List<Node> nodes = null;

    @SerializedName("links")
    @Expose
    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    public List<Link> links = null;

    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<MetricContainer> metricContainers;




}