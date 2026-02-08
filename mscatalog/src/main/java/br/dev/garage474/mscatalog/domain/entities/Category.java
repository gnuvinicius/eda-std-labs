package br.dev.garage474.mscatalog.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Category extends Entity {

    private UUID id;
    private String name;
    private Category parent;
    private List<Category> subCategories = new ArrayList<>();
}
