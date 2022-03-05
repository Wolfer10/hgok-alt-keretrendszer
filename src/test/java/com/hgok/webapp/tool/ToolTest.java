package com.hgok.webapp.tool;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


class ToolTest {

    @Test
    void generateTokensFromFilePaths() {
        Tool tool = new Tool();
        tool.setLanguage("Javascript");
        tool.setArguments("command %s");
        tool.setPath("utvonal");
        String[] expected = tool.generateTokensFromFilePaths(List.of(Path.of("alma"), Path.of("barna"),Path.of("dog")));
        assertThat(expected).hasSize(6);
        assertThat(StringUtils.join(expected, " ")).isEqualTo("node utvonal command alma barna dog");

    }
}