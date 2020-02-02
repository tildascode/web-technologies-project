package com.webproject.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.glxn.qrgen.javase.QRCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.File;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slide {

    @Id
    @GeneratedValue
    private Long id;

    private File image;

    private QRCode code;

    int index;
}
