package com.webproject.controllers;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.webproject.models.Presentation;
import com.webproject.models.Slide;
import com.webproject.services.PresentationService;
import com.webproject.services.SlideService;

@Controller
public class SlideController {

	@Autowired
	private SlideService slideService;
	
	@Autowired
	private PresentationService presentationService;

	@GetMapping("slides")
	public String showSlides(@RequestParam(required = false) Long presentationID, Model model) throws IOException {
		//List<Slide> slides = slideService.findAllSlidesForPresentation(presentationID);
		Presentation presentation = presentationService.getPresentationById(presentationID).get();
		List<String> fileNames = getResourceFiles("src/main/resources/img/presentations" + File.separator + presentation.getName());	
		model.addAttribute("images",fileNames);
		return "presentation";

	}
	
	private List<String> getResourceFiles(String path) throws IOException {
	    List<String> filenames = new ArrayList<>();

	    try (
	            InputStream in = getResourceAsStream(path);
	            BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
	        String resource;

	        while ((resource = br.readLine()) != null) {
	            filenames.add(resource);
	        }
	    }

	    return filenames;
	}

	private InputStream getResourceAsStream(String resource) {
	    final InputStream in
	            = getContextClassLoader().getResourceAsStream(resource);

	    return in == null ? getClass().getResourceAsStream(resource) : in;
	}

	private ClassLoader getContextClassLoader() {
	    return Thread.currentThread().getContextClassLoader();
	}
}
