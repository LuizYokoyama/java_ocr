package com.ly.ocr.dto;

import lombok.*;

import javax.validation.constraints.Size;
import static com.ly.ocr.dto.OcrDto.PHOTO_MAX_SIZE;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ImageDto {

    @EqualsAndHashCode.Include
    @Size(max = PHOTO_MAX_SIZE, message = "Utilize uma imagem de tamanho menor!")
    private String image;

}
