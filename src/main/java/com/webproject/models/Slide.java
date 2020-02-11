package com.webproject.models;

import javax.persistence.*;

import lombok.*;
import net.glxn.qrgen.javase.QRCode;

import java.io.File;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Slide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "image", unique = false, length = 100000000)
    private byte[] image;

    @Column(name = "qr_code", unique = false, length = 100000000)
    private byte[] qr_code;

    int index;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "presentation_id", nullable = false)
    Presentation presentation;

}
