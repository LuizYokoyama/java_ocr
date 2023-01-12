package com.ly.ocr.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name="tb_ocr")
public class OcrEntity{

    @EqualsAndHashCode.Include
    @Column("ocr_id")
    @Id
    private UUID id;

    @Column( "ocr_text")
    private String text;

    @Column("image")
    private String image;

}
