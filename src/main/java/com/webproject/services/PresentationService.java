package com.webproject.services;

import com.webproject.models.Presentation;
import com.webproject.repositories.PresentationRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PresentationService {

    @Autowired
    PresentationRepository presentationRepository;

    public List<Presentation> findAll() {
        return presentationRepository.findAll();
    }

    public List<Presentation> findAll(String userId) {
        //all for user id
        return presentationRepository.findAll();
    }
}
