package com.ly.ocr.model;


import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name="TB_OCR")
public class OcrEntity{

    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ocr_id")
    @Id
    private UUID id;

    @Column(name = "ocr_text", length = 2024)
    private String text;

    @Column(name = "image", length = 3145820)
    private String image;

}
