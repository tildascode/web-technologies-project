package com.webproject.services;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class UtilService {

    public byte[] createQR(Long presentationId) throws IOException {
        File qrCode = QRCode.from("p" + presentationId).to(ImageType.PNG).withSize(250, 250).file();
        return Files.readAllBytes(qrCode.toPath());
    }

    public byte[] createQR(Long presentationId, Long slideId) throws IOException {
        File qrCode = QRCode.from("p" + presentationId + "s" + slideId).to(ImageType.PNG).withSize(250, 250).file();
        return Files.readAllBytes(qrCode.toPath());
    }
}
