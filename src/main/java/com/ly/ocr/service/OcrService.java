package com.ly.ocr.service;

import com.ly.ocr.model.OcrEntity;
import com.ly.ocr.repository.OcrRepository;
import org.opencv.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.UUID;

@Service
public class OcrService implements IOcrService {



    @Autowired
    private OcrRepository ocrRepository;

    public String getOcrText(UUID id){

        return ocrRepository.findById(id).get().getText();

    }

    public String ocrScheduler(String image){

        OcrEntity ocrEntity = new OcrEntity();
        ocrEntity.setImage(image);
        ocrEntity = ocrRepository.save(ocrEntity);
        UUID id = ocrEntity.getId();
        OcrProcess.ocrQueue.add(id);

        return id.toString();
    }



}
