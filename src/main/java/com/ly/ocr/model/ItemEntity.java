package com.ly.ocr.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemEntity {

    @EqualsAndHashCode.Include
    @Column("item_id")
    @Id
    private UUID id;

    private String nomeItem;

    private int qtd;

    private String unidade;

    private float valorUnit;

    private float valorTotalItem;

}
