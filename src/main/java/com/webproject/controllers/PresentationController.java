package com.webproject.controllers;

import com.webproject.services.PresentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/presentations")
public class PresentationController {

    @Autowired
    private PresentationService presentationService;

    @GetMapping(value = "/{userID}")
    public String getPresentationsForUser(Model model, @PathVariable("userID") String userID) {
        model.addAttribute("presentations", presentationService.findAll(userID));
        return "presentations";
    }

    @GetMapping
    public String getAllPresentations(Model model) {
        model.addAttribute("presentations", presentationService.findAll());
        return "presentations";
    }

}
