package com.hgok.webapp.tool;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tool")
public class Tool {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique=true)
    @NotBlank
    String name;

    @NotBlank
    String language;

    @NotBlank
    String path;

    @NotBlank
    String arguments;

    String environmentArgs;
}
