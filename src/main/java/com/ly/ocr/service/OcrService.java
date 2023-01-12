package com.ly.ocr.service;

import com.ly.ocr.model.OcrEntity;
import com.ly.ocr.repository.OcrRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.ly.ocr.service.OcrProcess.ocrQueue;

@Service
@Slf4j
@Transactional
public class OcrService {

    @Autowired
    private OcrRepository ocrRepository;

    public Mono<String> getOcrText(UUID id){

        Mono<OcrEntity> entityMono= ocrRepository.findById(id);
        return entityMono.map(OcrEntity::getText);


    }

    public Mono<UUID> ocrSchedule(String image){

        OcrEntity ocrEntity = new OcrEntity();
        ocrEntity.setId(UUID.randomUUID());
        ocrEntity.setImage(image);
        //Mono<OcrEntity> ocrEntityMono = ocrRepository.save(ocrEntity);
        //ocrQueue.add(ocrEntity.getId());

        //return ocrEntityMono.map(OcrEntity::getId);
        return ocrRepository.save(ocrEntity).map(savedEntity -> {
            ocrQueue.add(savedEntity.getId());
            return savedEntity.getId();
        });
    }

}
