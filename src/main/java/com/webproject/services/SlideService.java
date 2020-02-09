package com.webproject.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webproject.models.Slide;
import com.webproject.repositories.SlideRepository;

@Service
public class SlideService {

	@Autowired
    private SlideRepository slideRepository;
	
	public List<Slide> findAllSlidesForPresentation(Long presentationId) {
        List<Slide> slides = slideRepository.findAll();
        List<Slide> result = new ArrayList<Slide>();
        for (Slide slide : slides) {
			if(slide.getPresentation().getId() == presentationId) {
				result.add(slide);
			}
		}
        return result;
    }
}
