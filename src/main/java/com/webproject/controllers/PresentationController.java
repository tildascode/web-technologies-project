package com.webproject.controllers;

import com.webproject.models.Presentation;
import com.webproject.services.PresentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.webproject.form.PresentationForm;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

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

    @PostMapping("/upload")
    public String handleFileUpload(Model model, @Valid PresentationForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuffer message = new StringBuffer();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                message.append(fe.getDefaultMessage() + " \n");
            }
            if (form.getZipFile().getName().isEmpty()) {
                message.append("Невалиден файл\n");
            }
            model.addAttribute("message", message.toString());

            return "upload";
        }
        //1st extract files
        File destination = null;
        try {
            destination = presentationService.decompressZipToDestination(form.getZipFile());
        } catch (IOException e) {
            model.addAttribute("message", "Неуспешно разахивиран файл; Сигурни ли сте, че сте покрили горните критерии?");
            return "upload";
        }
        //2nd for each file create presentation
        try {
            presentationService.createPresentationsFrom(destination, form, 1L);
        } catch (IOException e) {
            model.addAttribute("message", "Неуспешно качване на powerpoint файл; Сигурни ли сте, че сте покрили горните критерии?");
            return "upload";
        } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
            model.addAttribute("message", "Грешна обработка на името на файла.");
            return "upload";
        }
        //3rd for each slide create QR code

        return "redirect:/presentations";
    }


    @GetMapping("/upload")
    public String getUploadPage(PresentationForm form) {
        return "upload";
    }

}
