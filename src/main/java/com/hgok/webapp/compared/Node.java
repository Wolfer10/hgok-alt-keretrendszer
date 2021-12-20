package com.hgok.webapp.compared;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Node {

    @SerializedName("id")
    @Expose
    public Long id;
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
    public List<String> foundBy = null;

}