package com.hgok.webapp.compared;

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
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Expose (serialize = false, deserialize = false)
    private Long generatedId;

    @ManyToOne
    @JoinColumn(name = "compared_analysis_id")
    ComparedAnalysis comparedAnalysis;

    @SerializedName("id")
    @Expose
    public Long number;

    @SerializedName("label")
    @Expose
    public String label;
    @SerializedName("pos")
    @Expose
    public String pos;
    @SerializedName("entry")
    @Expose
    public Boolean entry;
    @SerializedName("final")
    @Expose
    public Boolean _final;
    @SerializedName("foundBy")
    @Expose
    @ElementCollection
    public List<String> foundBy = null;

}