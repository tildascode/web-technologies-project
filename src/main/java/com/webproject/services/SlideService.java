package com.webproject.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.webproject.models.Presentation;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webproject.models.Slide;
import com.webproject.repositories.SlideRepository;

@Service
public class SlideService {

    @Autowired
    private SlideRepository slideRepository;

    @Autowired
    private UtilService utils;

    public List<Slide> findAllSlidesForPresentation(Long presentationId) {
        List<Slide> slides = slideRepository.findAll();
        List<Slide> result = new ArrayList<>();
        for (Slide slide : slides) {
            if (slide.getPresentation().getId().equals(presentationId)) {
                result.add(slide);
            }
        }
        return result;
    }

    public void createQRCodeForSlidesOf(List<Presentation> presentations) throws IOException {
        for (Presentation p : presentations) {
            createQRCodeForSlidesOf(p);
        }
    }

    public void createQRCodeForSlidesOf(Presentation presentation) throws IOException {
        Long id = presentation.getId();
        List<Slide> slides = findAllSlidesForPresentation(id);
        File qrCodeFolder = new File("src/main/resources/static/img/presentations" + File.separator + presentation.getName(), "qrCodes");
        if (!qrCodeFolder.exists()) {
            Files.createDirectory(qrCodeFolder.toPath());
        }
        for (Slide s : slides) {
            File file = utils.createQR(id, s.getId());
            File qrCode = new File(qrCodeFolder + File.separator + file.getName());
            if (!qrCode.exists()) {
                Files.createDirectory(qrCode.toPath());
            }
            slideRepository.save(s);
        }
    }
}
