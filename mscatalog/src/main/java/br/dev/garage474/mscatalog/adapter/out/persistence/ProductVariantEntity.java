package br.dev.garage474.mscatalog.adapter.out.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
public class ProductVariantEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(unique = true)
    private String skuCode;

    private String barcode;

    @NotNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "promotional_price_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "promotional_price_currency"))
    })
    private Money promotionalPrice;

    @Embedded
    private Dimensions dimensions;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @ManyToMany
    @JoinTable(
        name = "product_variant_attribute_values",
        joinColumns = @JoinColumn(name = "variant_id"),
        inverseJoinColumns = @JoinColumn(name = "attribute_value_id")
    )
    private List<AttributeValueEntity> attributeValueEntities = new ArrayList<>();
}
