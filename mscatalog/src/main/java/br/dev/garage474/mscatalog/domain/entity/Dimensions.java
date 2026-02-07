package br.dev.garage474.mscatalog.domain.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public record Dimensions(double weight, double height, double width, double depth) {
}
