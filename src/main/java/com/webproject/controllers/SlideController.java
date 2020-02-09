package com.webproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.webproject.services.SlideService;

@Controller
@RequestMapping("/slides")
public class SlideController {

	@Autowired
	private SlideService slideService;

	@GetMapping(value = "/{presentationID}")
	public String getSlidesForUser(Model model, @PathVariable("presentationID") Long presentationID) {
		model.addAttribute("slides", slideService.findAllSlidesForPresentation(presentationID));
		return "presentation";
	}

}
