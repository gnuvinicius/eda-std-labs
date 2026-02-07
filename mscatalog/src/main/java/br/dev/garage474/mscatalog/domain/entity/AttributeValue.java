package br.dev.garage474.mscatalog.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * É o item físico vendável. É aqui que o código de barras e o preço real residem.
 * ### Attribute & AttributeValue (Grade / Especificações)
 * <p>
 * Diferente de comida, roupas e suplementos têm atributos fixos que geram as variações.
 * Attribute: Define o tipo (ex: "Cor", "Tamanho", "Sabor", "Voltagem", "Moagem do Café").
 * AttributeValue: Define o valor (ex: "Azul", "GG", "Baunilha", "220v", "Grão Inteiro").
 * Relação: O ProductVariant é uma combinação desses valores (ex: Sabor=Baunilha + Peso=900g).
 */
@Getter
@Setter
public class AttributeValue {

    private UUID id;
    private String value;
    private Attribute attribute;
}
