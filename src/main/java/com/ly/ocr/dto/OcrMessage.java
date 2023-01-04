package com.ly.ocr.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OcrMessage {

    @EqualsAndHashCode.Include
    private String image;

}
