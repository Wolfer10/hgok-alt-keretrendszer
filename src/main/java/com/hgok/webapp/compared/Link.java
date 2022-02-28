package com.hgok.webapp.compared;

import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nullable;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@ToString
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SerializedName("target")
    @Expose
    private Long target;
    @SerializedName("source")
    @Expose
    private Long source;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("foundBy")
    @Expose
    @ElementCollection
    private List<String> foundBy = null;

    @Column(columnDefinition="text")
    private String targetSnippet;
    @Column(columnDefinition="text")
    private String sourceSnippet;

    @Enumerated(value = EnumType.STRING)
    private LinkState state = LinkState.UNCHECKED;

}