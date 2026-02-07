package br.dev.garage474.mscatalog.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Product {

    private UUID id;
    private String name;
    private String description;
    private Brand brand;
    private Category category;
    private String slug;
    private Tags tags;
    private List<ProductVariant> variants = new ArrayList<>();
}
