package com.webproject.services;

import com.webproject.models.Presentation;
import com.webproject.repositories.PresentationRepository;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipUtil;

@Service
public class PresentationService {

    private static final int RESULTS_PER_PAGE = 10;

    @Autowired
    PresentationRepository presentationRepository;

    public Page<Presentation> findAll(int pageIndex, String tag) {
        if (tag != null) {
            return findAllForTag(tag, pageIndex);
        } else {
            return findAll(pageIndex);
        }
    }

    public Page<Presentation> findAll(int pageIndex) {
        return presentationRepository.findAll(PageRequest.of(pageIndex, RESULTS_PER_PAGE));
    }

    public Page<Presentation> findAllForTag(String tag, int pageIndex) {
        return presentationRepository.findByTagsContaining(tag, PageRequest.of(pageIndex, RESULTS_PER_PAGE));
    }

    public Page<Presentation> findAllForUser(String userId, int pageIndex) {
        //all for user id
        return presentationRepository.findAll(PageRequest.of(pageIndex, RESULTS_PER_PAGE));
    }

    public Set<String> getAllDistinctTags() {
        Set<String> tags = new HashSet<>();
        List<Presentation> presentations = presentationRepository.findAll();
        presentations.stream().forEach(p -> tags.addAll(Arrays.asList(p.getTags().split(","))));
        return tags;
    }

    public void exportAll() {
        ZipUtil
            .pack(new File("src/main/resources/static/files"), new File("src/main/resources/static/presentations.zip"));
    }
}
