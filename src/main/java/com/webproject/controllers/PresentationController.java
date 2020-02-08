package com.webproject.controllers;

import com.webproject.models.Presentation;
import com.webproject.services.PresentationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/presentations")
public class PresentationController {

    @Autowired
    private PresentationService presentationService;

    @GetMapping(value = "/{userID}")
    public String getPresentationsForUser(Model model, @PathVariable("userID") String userID,
                                          @RequestParam("pageIndex") int pageIndex) {
        model.addAttribute("presentations", presentationService.findAllForUser(userID));
        return "presentations";
    }

    @GetMapping
    public String getAllPresentations(Model model, @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex,
                                      @RequestParam(name = "tag", required = false) String tag) {
        List<Presentation> presentations = presentationService.findAll(pageIndex, tag);
        model.addAttribute("presentations", presentations);
        model.addAttribute("tags", presentationService.getAllDistinctTags());
        return "presentations";
    }

}
