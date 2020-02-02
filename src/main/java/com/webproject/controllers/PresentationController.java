package com.webproject.controllers;

import com.webproject.services.PresentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/presentations")
public class PresentationController {

    @Autowired
    private PresentationService presentationService;

    @GetMapping
    public String getPresentations(Model model) {
        model.addAttribute("presentations", presentationService.findAll());
        return "presentations";
    }


}
