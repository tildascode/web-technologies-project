package com.webproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webproject.services.SlideService;

@Controller
public class SlideController {

	@Autowired
	private SlideService slideService;

	@GetMapping("slides")
	public String getSlidesForUser(@RequestParam(required = false) Long presentationID, Model model) {
		model.addAttribute("slides", slideService.findAllSlidesForPresentation(presentationID));
		return "presentation";
	}

}
