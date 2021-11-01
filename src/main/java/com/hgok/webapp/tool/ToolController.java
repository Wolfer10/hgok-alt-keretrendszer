package com.hgok.webapp.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ToolController {

    @Autowired
    private ToolRepository toolRepository;


    @GetMapping("/addTool")
    public String showAddToolForm(@ModelAttribute("tool") Tool tool){
        return"add-tool";
    }

    @PostMapping("/addTool")
    public String addTool(@Valid Tool tool, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "add-tool";
        }
        toolRepository.save(tool);
        return "redirect:/toolList";
    }

    @GetMapping("/toolList")
    public String showToolList(Model model) {
        model.addAttribute("tools", toolRepository.findAll());
        return "tool-list";
    }



    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tool Id:" + id));

        model.addAttribute("tool", tool);
        return "update-tool";
    }

    @PostMapping("/update/{id}")
    public String updateTool(@PathVariable("id") long id, @Valid Tool tool,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            tool.setId(id);
            return "update-tool";
        }

        toolRepository.save(tool);
        return "redirect:/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteTool(@PathVariable("id") long id, Model model) {
        Tool tool = toolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        toolRepository.delete(tool);
        return "redirect:/index";
    }


}
