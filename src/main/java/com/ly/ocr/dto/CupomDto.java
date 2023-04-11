package com.ly.ocr.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CupomDto {

    @EqualsAndHashCode.Include
    private UUID id;

    @Size(max = 255, message = "Utilize URL menor!")
    private String url;

    private String nomeEmpresa;

    private String cnpj;

    private String endereco;

    private String items;

    private int qtdTotalItens;

    private Float valorPagar;

    private Float cartaoCredito;

    private Float cartaoDebito;

    private Float dinheiro;

    private Float pix;

    private Float outros;

    private Float troco;

    private Float tributos;

    private LocalDateTime emissao;


}
