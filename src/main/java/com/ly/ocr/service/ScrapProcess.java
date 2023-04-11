package com.ly.ocr.service;


import com.ly.ocr.model.CupomEntity;
import com.ly.ocr.model.ItemEntity;
import com.ly.ocr.repository.CupomRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.ly.ocr.OcrApplication.webDriver;

@Component
@EnableScheduling
public class ScrapProcess {

    public static final Queue scrapQueue = new LinkedList();

    @Autowired
    private CupomRepository cupomRepository;


    @Scheduled(fixedRate = 1000)
    public void doScrapQueued(){


        UUID id;
        try {
            id = (UUID) scrapQueue.remove();
        } catch(NoSuchElementException e) {
            return;
        }

        Mono<CupomEntity> cupomEntityMono = cupomRepository.findById(id);
        Optional<CupomEntity> cupomEntityOptional = cupomEntityMono.blockOptional();

        if (cupomEntityOptional.isEmpty()){
            return;
        }

        CupomEntity cupomEntity = cupomEntityOptional.get();

        cupomEntity = scrapSummarizedItems(cupomEntity.getUrl(), id);

        cupomRepository.save(cupomEntity).block();

    }

    public static CupomEntity scrapSummarizedItems(String nfeUrl, UUID id) {

        webDriver.get(nfeUrl);

        //Store the web element
        WebElement iframe = webDriver.findElement(By.className("iframe-danfe-nfce"));

        //Switch to the frame
        webDriver.switchTo().frame(iframe);

        CupomEntity cupom = new CupomEntity();

        cupom.setId(id);

        cupom.setUrl(nfeUrl);

        cupom.setNomeEmpresa(webDriver.findElement(By.className("txtTopo")).getText());

        cupom.setCnpj(webDriver.findElements(By.className("text")).get(0).getText().replace("CNPJ: ", ""));

        cupom.setEndereco(webDriver.findElements(By.className("text")).get(1).getText());

        String items = "";
        var nomeItem = webDriver.findElements(By.className("txtTit"));
        var qtd = webDriver.findElements(By.className("Rqtd"));
        var unidade = webDriver.findElements(By.className("RUN"));
        var valorUnit = webDriver.findElements(By.className("RvlUnit"));
        var valorTotalItem = webDriver.findElements(By.className("valor"));
        if (nomeItem.size() > 0){
            for (int i = 0; i < nomeItem.size(); i+=2){
                items += nomeItem.get(i).getText() + " "
                + qtd.get(i/2).getText().replace("Qtde.:", "") + " "
                + unidade.get(i/2).getText().replace("UN: ", "") + " x "
                + valorUnit.get(i/2).getText()
                        .replace("Vl. Unit.:   ", "").replace(",",".") + " Total: "
                + valorTotalItem.get(i/2).getText()
                        .replace(",",".") + "\n";
            }
        }
        cupom.setItems(items);

        var linhaTotal = webDriver.findElements(By.id("linhaTotal"));
        if (linhaTotal.size() > 0){
            for (int i = 0; i < linhaTotal.size(); i++){
                switch (linhaTotal.get(i).findElement(By.tagName("label")).getText()){
                    case "Qtd. total de itens:":
                        cupom.setQtdTotalItens(Integer.parseInt(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()));
                        break;
                    case "Valor a pagar R$:":
                        cupom.setValorPagar(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                    case "Cartão de Crédito":
                        cupom.setCartaoCredito(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                    case "Cartão de Débito":
                        cupom.setCartaoDebito(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                    case "Dinheiro":
                        cupom.setDinheiro(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                    case "Pix":
                        cupom.setPix(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                    case "Outros":
                        cupom.setOutros(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                    case "Troco":
                        cupom.setTroco(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                    case "Informação dos Tributos Totais Incidentes (Lei Federal 12.741/2012) R$":
                        cupom.setTributos(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                }
            }
        }

        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss ");
        String text = webDriver.findElement(By.className("ui-li-static")).
                getText().split(" Emissão: ")[1].split("- Via Consumidor")[0];
        LocalDateTime dateTime = LocalDateTime.parse(text, formatter);

        cupom.setEmissao(dateTime);

        return cupom;

    }

    public static CupomEntity scrap(String nfeUrl) {

        webDriver.get(nfeUrl);

        //Store the web element
        WebElement iframe = webDriver.findElement(By.className("iframe-danfe-nfce"));

        //Switch to the frame
        webDriver.switchTo().frame(iframe);

        CupomEntity cupom = new CupomEntity();

        cupom.setUrl(nfeUrl);

        cupom.setNomeEmpresa(webDriver.findElement(By.className("txtTopo")).getText());

        cupom.setCnpj(webDriver.findElements(By.className("text")).get(0).getText().replace("CNPJ: ", ""));

        cupom.setEndereco(webDriver.findElements(By.className("text")).get(1).getText());

        List<ItemEntity> itens = new ArrayList<>();
        var nomeItem = webDriver.findElements(By.className("txtTit"));
        var qtd = webDriver.findElements(By.className("Rqtd"));
        var unidade = webDriver.findElements(By.className("RUN"));
        var valorUnit = webDriver.findElements(By.className("RvlUnit"));
        var valorTotalItem = webDriver.findElements(By.className("valor"));
        if (nomeItem.size() > 0){
            for (int i = 0; i < nomeItem.size(); i+=2){
                ItemEntity itenEntity = new ItemEntity();

                itenEntity.setNomeItem(nomeItem.get(i).getText());
                itenEntity.setQtd(Integer.parseInt(qtd.get(i/2).getText().replace("Qtde.:", "")));
                itenEntity.setUnidade(unidade.get(i/2).getText().replace("UN: ", ""));
                itenEntity.setValorUnit(Float.parseFloat(valorUnit.get(i/2).getText()
                        .replace("Vl. Unit.:   ", "").replace(",",".")));
                itenEntity.setValorTotalItem(Float.parseFloat(valorTotalItem.get(i/2).getText()
                        .replace(",",".")));
                itens.add(itenEntity);
            }
        }
      //  cupom.setItemEntityList(itens);

        var linhaTotal = webDriver.findElements(By.id("linhaTotal"));
        if (linhaTotal.size() > 0){
            for (int i = 0; i < linhaTotal.size(); i++){
                switch (linhaTotal.get(i).findElement(By.tagName("label")).getText()){
                    case "Qtd. total de itens:":
                        cupom.setQtdTotalItens(Integer.parseInt(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()));
                        break;
                    case "Valor a pagar R$:":
                        cupom.setValorPagar(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                    case "Cartão de Crédito":
                        cupom.setCartaoCredito(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                    case "Cartão de Débito":
                        cupom.setCartaoDebito(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                    case "Dinheiro":
                        cupom.setDinheiro(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                    case "Pix":
                        cupom.setPix(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                    case "Troco":
                        cupom.setTroco(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                    case "Informação dos Tributos Totais Incidentes (Lei Federal 12.741/2012) R$":
                        cupom.setTributos(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
                                .replace(",",".")));
                        break;
                }
            }
        }

        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss ");
        String text = webDriver.findElement(By.className("ui-li-static")).
                getText().split(" Emissão: ")[1].split("- Via Consumidor")[0];
        LocalDateTime dateTime = LocalDateTime.parse(text, formatter);

        cupom.setEmissao(dateTime);

        return cupom;

    }

}
