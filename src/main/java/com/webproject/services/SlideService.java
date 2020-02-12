package com.webproject.services;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.webproject.models.Presentation;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webproject.models.Slide;
import com.webproject.repositories.SlideRepository;

@Service
public class SlideService {

    @Autowired
    private SlideRepository slideRepository;

    public List<Slide> createSlides(File file, File slidesDir, Presentation presentation) throws IOException {
        List<Slide> slides = new ArrayList<>();
        XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
        Dimension pageSize = ppt.getPageSize();
        List<XSLFSlide> xslfSlides = ppt.getSlides();
        for (int i = 0; i < xslfSlides.size(); i++) {
            String fileName = "slide" + i + ".png";
            String slideFile = slidesDir.getCanonicalPath() + File.separator + fileName;
            createAndSaveSlideImage(slideFile, pageSize, xslfSlides.get(i));
            slides.add(Slide.builder().index(i).presentation(presentation).build());
        }
        slideRepository.saveAll(slides);
        createAndSaveQrCodes(presentation.getId(), slides);
        return slides;
    }

    public void createAndSaveQrCodes(Long presentationId, List<Slide> slides) throws IOException {
        File qrCodeFolder = new File(
            "src/main/resources/static/img/presentations" + File.separator + presentationId, "qrCodes");
        if (!qrCodeFolder.exists()) {
            Files.createDirectory(qrCodeFolder.toPath());
        }
        for (Slide s : slides) {
            File file = QRCode.from("p" + presentationId + "s" + s.getId()).to(ImageType.PNG).withSize(250, 250).file();
            File qrCode = new File(qrCodeFolder + File.separator + file.getName());
            if (!qrCode.exists()) {
                Files.createDirectory(qrCode.toPath());
            }
        }
    }

    private void createAndSaveSlideImage(String slideFile, Dimension pageSize, XSLFSlide xslfSlide) throws IOException {
        BufferedImage slideImage = new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = slideImage.createGraphics();
        graphics.setPaint(Color.white);
        graphics.fill(new Rectangle2D.Float(0, 0, pageSize.width, pageSize.height));
        xslfSlide.draw(graphics);
        System.out.println("Creating " + slideFile);
        OutputStream out = new FileOutputStream(slideFile);
        javax.imageio.ImageIO.write(slideImage, "png", out);
        out.close();
    }

}
