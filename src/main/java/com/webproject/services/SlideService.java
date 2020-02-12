package com.webproject.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.webproject.models.Presentation;
import java.util.Map;
import javax.imageio.ImageIO;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.webproject.models.Slide;
import com.webproject.repositories.SlideRepository;

@Service
public class SlideService {

    @Value("${domain.name}")
    private String domainName;

    @Autowired
    private SlideRepository slideRepository;

    private static Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", "dmxmc676d",
        "api_key", "649891352997634",
        "api_secret", "1UOOSWMU05PKBX2hAxDJEIy9i1c"));

    public List<Slide> createSlides(InputStream fis, Presentation presentation) throws IOException {
        List<Slide> slides = new ArrayList<>();
        XMLSlideShow ppt = new XMLSlideShow(fis);
        Dimension pageSize = ppt.getPageSize();
        List<XSLFSlide> xslfSlides = ppt.getSlides();
        for (int i = 0; i < xslfSlides.size(); i++) {
            slides
                .add(Slide.builder().index(i).presentation(presentation)
                          .qrCodeUrl(createAndSaveQrCode(presentation.getId(), i))
                          .imageUrl(createAndSaveSlideImage(pageSize, xslfSlides.get(i)))
                          .build());
        }
        slideRepository.saveAll(slides);
        return slides;
    }

    public String createAndSaveQrCode(Long presentationId, int index) throws IOException {
        return uploadToCloudinary(QRCode
                                      .from(domainName + "/presentations/p/" + presentationId + "/slide/" + index)
                                      .to(ImageType.PNG)
                                      .withSize(250, 250).file());
    }

    private String createAndSaveSlideImage(Dimension pageSize, XSLFSlide xslfSlide) throws IOException {
        BufferedImage slideImage = new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = slideImage.createGraphics();
        graphics.setPaint(Color.white);
        graphics.fill(new Rectangle2D.Float(0, 0, pageSize.width, pageSize.height));
        xslfSlide.draw(graphics);
        File tempFile = File.createTempFile("temp", ".png");
        ImageIO.write(slideImage, "png", tempFile);
        return uploadToCloudinary(tempFile);

    }

    private String uploadToCloudinary(File qrCode) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(qrCode, ObjectUtils.emptyMap());
        return (String) uploadResult.get("secure_url");
    }


}
