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
    private ToolService toolService;


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
        toolService.save(tool);
        return "redirect:/toolList";
    }

    @GetMapping("/toolList")
    public String showToolList(Model model) {
        model.addAttribute("tools", toolService.findAll());
        return "tool-list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Tool tool = toolService.findById(id);
        model.addAttribute("tool", tool);
        return "add-tool";
    }

    @PostMapping("/addTool/{id}")
    public String editTool(@PathVariable("id") long id, @Valid Tool tool,  Model model){
        Tool existingTool = toolService.findById(id);
        existingTool.copyTool(tool);
        toolService.save(existingTool);
        return  "redirect:/toolList";
    }


    @GetMapping("/delete/{id}")
    public String deleteTool(@PathVariable("id") long id, Model model) {
        Tool tool = toolService.findById(id);
        toolService.delete(tool);
        return "redirect:/toolList";
    }


}
