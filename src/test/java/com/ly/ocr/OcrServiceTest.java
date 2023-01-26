package com.ly.ocr;


import com.ly.ocr.dto.ImageDto;
import com.ly.ocr.repository.OcrRepository;
import com.ly.ocr.service.OcrProcess;
import com.ly.ocr.service.OcrService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
@ActiveProfiles(profiles = "integration-test")
public class OcrServiceTest {

    @Autowired
    private OcrRepository ocrRepository;

    @Autowired
    private OcrService ocrService;

    @Autowired
    private OcrProcess ocrProcess;

    @Test
    void givenImageReturnIdGivenIdReturnText() {

        var id = ocrService.putOnOcrQueue("test image");
        var uuid = id.block();
        System.out.println("id: " + uuid);

        var ocrEntity = ocrRepository.findById(uuid).block();

        assertEquals(ocrEntity.getImage(), "test image");
        ocrEntity.setText("test text");

        ocrEntity = ocrRepository.save(ocrEntity).block();
        assertEquals(ocrEntity.getId(), uuid);

        var ocrText = ocrService.getOcrText(uuid);
        assertEquals(ocrText.block(), "test text");
        System.out.println("ocrText: " + ocrText.block());

    }

    @Test
    void loadImagePutOnQueueReturnIdDoOcrReturnText() throws IOException {
        final var json = Paths.get("src", "test", "resources", "data.json");
        final var imageDto = new ObjectMapper().readValue(json.toFile(), ImageDto.class);
        var id = ocrService.putOnOcrQueue(imageDto.getImage());
        var uuid = id.block();

        var ocrEntity = ocrRepository.findById(uuid).block();
        assertEquals(ocrEntity.getImage(), imageDto.getImage());

        ocrProcess.doOcr();

        var ocrText = ocrService.getOcrText(uuid);
        assertNotNull(ocrText.block());
        System.out.println("ocrText: " + ocrText.block());


    }


}
