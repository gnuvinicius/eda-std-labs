package br.dev.garage474.mscatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowcaseFilterDto {
    private String q;
    private UUID brandId;
    private UUID categoryId;
    private String platform; // web or mobile
}

