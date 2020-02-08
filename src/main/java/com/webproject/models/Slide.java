package com.webproject.models;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.*;
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

    //private QRCode code;

    int index;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "presentation_id", nullable = false)
    Presentation presentation;
}
