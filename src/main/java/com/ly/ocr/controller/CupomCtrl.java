package com.ly.ocr.controller;

import com.ly.ocr.dto.CupomDto;
import com.ly.ocr.model.CupomEntity;
import com.ly.ocr.service.CupomService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class CupomCtrl {

    @Autowired
    private CupomService cupomService;

    @PostMapping("/qrcode-url")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "post url readed from qrcode's NFC-e, return uuid")
    public Mono<UUID> scrap(@RequestBody String url){

        return cupomService.putOnScrapQueue(url);

    }

    @GetMapping("/nfce/{id}")
    @Operation(summary = "Get the NFC-e from the Id")
    public Mono<CupomDto> ocrResult(@PathVariable(value = "id") UUID id){

        return cupomService.getNfCE(id);

    }

}
