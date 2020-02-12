package com.webproject.services;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.webproject.models.Presentation;
import javax.imageio.ImageIO;
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
            String qrCode = createAndSaveQrCode(presentation.getId(), i);
            slides.add(Slide.builder().index(i).presentation(presentation).qr(qrCode).build());
        }
        slideRepository.saveAll(slides);
        return slides;
    }

    public String createAndSaveQrCode(Long presentationId, int index) throws IOException {
        File dir = new File(
            "src/main/resources/static/img/presentations" + File.separator + presentationId, "qrCodes");
        if (!dir.exists()) {
            Files.createDirectory(dir.toPath());
        }
        ByteArrayOutputStream file = QRCode.from("p" + presentationId + "s" + index).to(ImageType.PNG)
                                           .withSize(250, 250).stream();
        String fileName = "qrCode" + index + ".png";
        File qrCode = new File(dir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(qrCode);
        fos.write(file.toByteArray());
        fos.close();
        return fileName;
    }

    private void createAndSaveSlideImage(String slideFile, Dimension pageSize, XSLFSlide xslfSlide) throws IOException {
        BufferedImage slideImage = new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = slideImage.createGraphics();
        graphics.setPaint(Color.white);
        graphics.fill(new Rectangle2D.Float(0, 0, pageSize.width, pageSize.height));
        xslfSlide.draw(graphics);
        OutputStream out = new FileOutputStream(slideFile);
        ImageIO.write(slideImage, "png", out);
        out.close();
    }

}
