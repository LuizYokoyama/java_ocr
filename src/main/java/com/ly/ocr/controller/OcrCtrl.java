package com.ly.ocr.controller;

import com.ly.ocr.dto.ImageDto;
import com.ly.ocr.service.OcrService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class OcrCtrl {

    @Autowired
    private OcrService ocrService;

    @PostMapping("/ocr")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "post image to do OCR, return uuid")
    public Mono<String> ocr(@RequestBody ImageDto msg){

        return ocrService.ocrSchedule(msg.getImage());

    }

    @GetMapping("/ocr_text/{id}")
    @Operation(summary = "Get the text from the ocr Id")
    public Mono<UUID> ocrResult(@PathVariable(value = "id") UUID id){

        return ocrService.getOcrText(id);

    }

}
