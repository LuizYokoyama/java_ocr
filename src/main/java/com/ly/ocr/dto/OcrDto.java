package com.ly.ocr.dto;


import lombok.*;

import javax.validation.constraints.Size;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OcrDto {

    public static final int PHOTO_MAX_SIZE = 3145728;  // 3 Mb

    @EqualsAndHashCode.Include
    private UUID id;

    private String text;

    @Size(max = PHOTO_MAX_SIZE, message = "Utilize uma imagem de tamanho menor!")
    private String image;

}
