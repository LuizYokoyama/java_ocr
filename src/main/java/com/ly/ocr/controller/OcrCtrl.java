package com.ly.ocr.controller;

import com.ly.ocr.dto.OcrMessage;
import com.ly.ocr.service.OcrService;
import io.swagger.v3.oas.annotations.Operation;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@CrossOrigin(origins = "*")
public class OcrCtrl {



    private OcrService ocrService = new OcrService();


    @PostMapping("/ocr")
    @Operation(summary = "post image to do OCR")
    public String ocr(@RequestBody OcrMessage msg){

        return ocrService.ocrRead(msg.getImage());
    }
}
