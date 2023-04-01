package com.ly.ocr.repository;

import com.ly.ocr.model.CupomEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("cupomRepository")
public interface CupomRepository extends ReactiveCrudRepository<CupomEntity, UUID> {
}
