package com.ly.ocr.service;

import java.util.UUID;

public interface IOcrService {

    String getOcrText(UUID id);
    String ocrRead(String image);

}
