package com.ly.ocr.dto;


import lombok.*;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OcrDto {

    @EqualsAndHashCode.Include
    private UUID id;

    private String text;

}
