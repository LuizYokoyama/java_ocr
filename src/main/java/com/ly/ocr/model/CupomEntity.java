package com.ly.ocr.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tb_nfce")
public class CupomEntity {

    @EqualsAndHashCode.Include
    @Column("cupom_id")
    @Id
    private UUID id;

    @Column("url")
    private String url;

    @Column("nome_empresa")
    private String nomeEmpresa;

    @Column("cnpj")
    private String cnpj;

    @Column("endereco")
    private String endereco;

    @Column("items")
    private String items;

    @Column("qtd_total_itens")
    private int qtdTotalItens;

    @Column("valor_pagar")
    private Float valorPagar;

    @Column("cartao_credito")
    private Float cartaoCredito;

    @Column("cartao_debito")
    private Float cartaoDebito;

    @Column("dinheiro")
    private Float dinheiro;

    @Column("pix")
    private Float pix;

    @Column("troco")
    private Float troco;

    @Column("tributos")
    private Float tributos;

    @Column("emissao")
    private LocalDateTime emissao;


}
