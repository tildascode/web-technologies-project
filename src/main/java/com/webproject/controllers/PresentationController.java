package com.webproject.controllers;

import com.webproject.models.Presentation;
import com.webproject.services.PresentationService;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/presentations")
public class PresentationController {

    @Autowired
    private PresentationService presentationService;

    @GetMapping(value = "/{userID}")
    public String getPresentationsForUser(Model model, @PathVariable("userID") String userID,
                                          @RequestParam(name = "page", defaultValue = "0") int page) {
        addAttributes(presentationService.findAllForUser(userID, page), model);
        return "profile";
    }

    @GetMapping
    public String getAllPresentations(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "tag", required = false) String tag) {
        addAttributes(presentationService.findAll(page, tag), model);
        model.addAttribute("tags", presentationService.getAllDistinctTags());
        model.addAttribute("selectedTag", tag);
        return "all-presentations";
    }

    @GetMapping(value = "/export")
    @ResponseBody
    public String export() {
        presentationService.exportAll();
        return null;
    }

    private void addAttributes(Page<Presentation> presentations, Model model) {
        model.addAttribute("presentations", presentations.getContent());
        model.addAttribute("pages", presentations.getTotalPages());
        model.addAttribute("pageNumber", presentations.getNumber());
    }

}
