package com.hgok.webapp.compared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Label {

    private Link link;
    private String sourceFileName;
    private String targetFileName;
    private int sourceStartLine;
    private int targetStartLine;

    public Label(String label, Link link){
        labelSplitter(label);
        this.link = link;
    }

    public void labelSplitter(String label){
        String[] separetedFileNames = label.split("->");
        sourceFileName = separetedFileNames[0].split(":")[0] + ":" + separetedFileNames[0].split(":")[1];
        targetFileName = separetedFileNames[1].split(":")[0] + ":" + separetedFileNames[0].split(":")[1];
        sourceStartLine = Integer.parseInt(separetedFileNames[0].split(":")[2]);
        targetStartLine = Integer.parseInt(separetedFileNames[1].split(":")[2]);
    }



}
