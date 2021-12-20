package com.hgok.webapp.compared;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ComparedTools {

    @SerializedName("directed")
    @Expose
    public Boolean directed;
    @SerializedName("multigraph")
    @Expose
    public Boolean multigraph;
    @SerializedName("nodes")
    @Expose
    public List<Node> nodes = null;
    @SerializedName("links")
    @Expose
    public List<Link> links = null;

}