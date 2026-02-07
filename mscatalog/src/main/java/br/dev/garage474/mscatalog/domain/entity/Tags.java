package br.dev.garage474.mscatalog.domain.entity;

import jakarta.persistence.*;

import java.util.List;

@Embeddable
public record Tags(
    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    List<String> values
) {
}
