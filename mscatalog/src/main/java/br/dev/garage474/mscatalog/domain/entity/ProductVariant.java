package br.dev.garage474.mscatalog.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductVariant {

    private UUID id;
    private String skuCode;
    private String barcode;
    private Money price;
    private Money promotionalPrice;
    private Dimensions dimensions;
    private Product product;
    private List<AttributeValue> attributeValueEntities = new ArrayList<>();
}
