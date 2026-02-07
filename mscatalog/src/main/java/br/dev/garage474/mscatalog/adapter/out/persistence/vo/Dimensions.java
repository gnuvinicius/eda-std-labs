package br.dev.garage474.mscatalog.adapter.out.persistence.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record Dimensions(double weight, double height, double width, double depth) {
}

