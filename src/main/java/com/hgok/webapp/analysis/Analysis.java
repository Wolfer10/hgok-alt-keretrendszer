package com.hgok.webapp.analysis;

import com.hgok.webapp.compared.ComparedAnalysis;
import com.hgok.webapp.tool.Tool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany()
    private List<Tool> tools;

    private String pathName;

    @OneToOne(
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private ComparedAnalysis comparedAnalysis;


    private String status;

    private Timestamp timestamp;


    public Analysis(List<Tool> tools, String status, Timestamp timestamp, String pathName) {
        this.tools = tools;
        this.status = status;
        this.timestamp = timestamp;
        this.pathName = pathName;
    }

    public Analysis(Long id) {
       this.id = id;
    }


}
