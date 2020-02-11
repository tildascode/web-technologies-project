package com.webproject.services;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class UtilService {

    public File createQR(Long presentationId) throws IOException {
        return QRCode.from("p" + presentationId).to(ImageType.PNG).withSize(250, 250).file();
    }

    public File createQR(Long presentationId, Long slideId) throws IOException {
        return QRCode.from("p" + presentationId + "s" + slideId).to(ImageType.PNG).withSize(250, 250).file();
    }
}
