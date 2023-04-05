package com.ly.ocr;


import com.ly.ocr.repository.CupomRepository;
import com.ly.ocr.service.CupomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Testcontainers
public class OcrServiceTest {


    @Autowired
    private CupomRepository cupomRepository;

    @Autowired
    private CupomService cupomService;


    @Test
    void givenQrUrlReturnIdGivenIdReturnCupomItems() {

        OcrApplication.webDriverInit();
        var id = cupomService.putOnScrapQueue("http://nfe.sefaz.go.gov.br/nfeweb/sites/nfce/danfeNFCe?p=52230245543915002478650390002672701963325360|2|1|1|0B8D90E623913704300DD14E74C54672D6160DBD");
        var uuid = id.block();
        System.out.println("Integration test");
        System.out.println("id: " + uuid);

        var cupomEntity = cupomRepository.findById(uuid).block();

        assertEquals(cupomEntity.getUrl(), "http://nfe.sefaz.go.gov.br/nfeweb/sites/nfce/danfeNFCe?p=52230245543915002478650390002672701963325360|2|1|1|0B8D90E623913704300DD14E74C54672D6160DBD");
        cupomEntity.setItems("test items");

        cupomEntity = cupomRepository.save(cupomEntity).block();
        assertEquals(cupomEntity.getId(), uuid);

        var nfce = cupomService.getNfCE(uuid);
        assertEquals(nfce.block().getItems(), "test items");
        System.out.println("items: " + nfce.block().getItems());

    }


}
