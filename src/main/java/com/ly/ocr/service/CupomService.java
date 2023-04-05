package com.ly.ocr.service;

import com.ly.ocr.dto.CupomDto;
import com.ly.ocr.model.CupomEntity;
import com.ly.ocr.repository.CupomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.CoreSubscriber;
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

    public Mono<CupomDto> getNfCE(UUID id) {

        Mono<CupomEntity> cupomEntityMono= cupomRepository.findById(id);

        return cupomRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(new RuntimeException("Cupom não encontrado para este Id!")))
                .map(cupomEntity -> {
                            CupomDto cupomDto = new CupomDto();
                            BeanUtils.copyProperties(cupomEntity, cupomDto);
                            return cupomDto;
                });
    }
}
