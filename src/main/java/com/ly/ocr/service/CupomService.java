package com.ly.ocr.service;

import com.ly.ocr.model.CupomEntity;
import com.ly.ocr.repository.CupomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.ly.ocr.service.ScrapProcess.scrapQueue;

@Service
@Slf4j
@Transactional
public class CupomService {

    @Autowired
    private CupomRepository cupomRepository;

    public Mono<UUID> putOnScrapQueue(String url) {
        CupomEntity cupomEntity = new CupomEntity();
        cupomEntity.setUrl(url);

        return cupomRepository.save(cupomEntity).map(savedEntity -> {
            scrapQueue.add(savedEntity.getId());
            return savedEntity.getId();
        });
    }

    public Mono<CupomEntity> getNfCE(UUID id) {

        Mono<CupomEntity> entityMono= cupomRepository.findById(id);
        return entityMono;

    }
}
