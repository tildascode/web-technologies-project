package com.webproject.services;

import com.webproject.models.Presentation;
import com.webproject.repositories.PresentationRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PresentationService {

    private static final int RESULTS_PER_PAGE = 10;

    @Autowired
    PresentationRepository presentationRepository;

    public List<Presentation> findAll(int pageIndex, String tag) {
        if (tag != null) {
            return findAllForTag(tag, pageIndex);
        } else {
            return findAll(pageIndex);
        }
    }

    public List<Presentation> findAll(int pageIndex) {
        return presentationRepository.findAll(PageRequest.of(pageIndex, RESULTS_PER_PAGE)).getContent();
    }

    public List<Presentation> findAllForTag(String tag, int pageIndex) {
        return presentationRepository.findByTagsContaining(tag, PageRequest.of(pageIndex, RESULTS_PER_PAGE));
    }

    public List<Presentation> findAllForUser(String userId) {
        //all for user id
        return presentationRepository.findAll();
    }

    public Set<String> getAllDistinctTags() {
        Set<String> tags = new HashSet<>();
        List<Presentation> presentations = presentationRepository.findAll();
        presentations.stream().forEach(p -> tags.addAll(Arrays.asList(p.getTags().split(","))));
        return tags;
    }
}
