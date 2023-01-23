package com.ly.ocr;


import com.ly.ocr.repository.OcrRepository;
import com.ly.ocr.service.OcrService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles(profiles = "test")
public class OcrServiceTest {

    @Autowired
    private OcrRepository ocrRepository;

    @Autowired
    private OcrService ocrService;


    @Test
    void givenImageReturnIdGivenIdReturnText() {

        var id = ocrService.ocrSchedule("test image");
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


}
