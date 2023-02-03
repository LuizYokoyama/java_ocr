package com.ly.ocr.service;

import com.ly.ocr.model.OcrEntity;
import com.ly.ocr.repository.OcrRepository;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.OCRResult;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;


@Component
@EnableScheduling
public class OcrProcess {

    public static final Queue ocrQueue = new LinkedList();
    public static final String DATA_PATH = "/usr/share/tessdata/";
    public static final int ENGINE_MODE = 2;
    public static final int PAGE_MODE = 1;
    public static final String LANG = "por";

    @Autowired
    private OcrRepository ocrRepository;

    @Scheduled(fixedRate = 1000)
    public void doOcr(){

        UUID id;
        try {
            id = (UUID) ocrQueue.remove();
        } catch(NoSuchElementException e) {
            return;
        }

        Mono<OcrEntity> ocrEntityMono = ocrRepository.findById(id);
        Optional<OcrEntity> ocrEntityOptional = ocrEntityMono.blockOptional();

        if (ocrEntityOptional.isEmpty()){
            return;
        }
        OcrEntity ocrEntity = ocrEntityOptional.get();

        Tesseract tesseract = new Tesseract();
        tesseract.setOcrEngineMode(ENGINE_MODE);
        tesseract.setPageSegMode(PAGE_MODE);
        tesseract.setLanguage(LANG);
        tesseract.setDatapath(DATA_PATH);

        byte[] imageByte = Base64.getDecoder().decode(ocrEntityOptional.get().getImage());
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(bis);
            bis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String text;
        OCRResult ocrResult;
        List<ITesseract.RenderedFormat> renderedFormats = new ArrayList<>();
        renderedFormats.add(ITesseract.RenderedFormat.TEXT);
        bufferedImage = ImageProcess.filter(bufferedImage);
        try {
            text = tesseract.doOCR(bufferedImage);
            ocrResult =  tesseract.createDocumentsWithResults(bufferedImage, "", "",renderedFormats, 1 );
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Confidence: " + ocrResult.getConfidence());
        System.out.println(ocrResult.getWords().toString());

        ocrEntity.setText(text);
        ocrEntity.setImage(""); //free image from db

        ocrRepository.save(ocrEntity).block();

    }

}
