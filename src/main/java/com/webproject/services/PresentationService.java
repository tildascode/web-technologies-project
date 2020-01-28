package com.webproject.services;

import com.webproject.models.Presentation;
import com.webproject.repositories.PresentationRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class PresentationService {

    @Autowired
    PresentationRepository presentationRepository;

    public List<Presentation> findAll() {
        return presentationRepository.findAll();
    }
}
