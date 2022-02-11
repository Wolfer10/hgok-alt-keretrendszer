package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.Link;
import com.hgok.webapp.tool.Tool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(
            cascade = CascadeType.ALL)
    private List<Tool> tools;

    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Link> links;

    private String status;

    private Timestamp timestamp;


    public Analysis(List<Tool> tools, String status, Timestamp timestamp) {
        this.tools = tools;
        this.status = status;
        this.timestamp = timestamp;
        links = new ArrayList<>();
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public Analysis(Long id) {
       this.id = id;
    }

}
