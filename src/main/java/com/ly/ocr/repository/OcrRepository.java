package com.ly.ocr.repository;

import com.ly.ocr.model.OcrEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("ocrRepository")
public interface OcrRepository extends ReactiveCrudRepository<OcrEntity, UUID> {

}
