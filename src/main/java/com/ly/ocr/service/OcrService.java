package com.ly.ocr.service;

import com.ly.ocr.model.OcrEntity;
import com.ly.ocr.repository.OcrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OcrService {

    @Autowired
    private OcrRepository ocrRepository;

    public String getOcrText(UUID id){

        Optional<OcrEntity> optionalOcr= ocrRepository.findById(id);
        return optionalOcr.isPresent() ? optionalOcr.get().getText() : "";

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
