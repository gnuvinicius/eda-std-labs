package br.dev.garage474.msestoque.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Produto {
    @EqualsAndHashCode.Include
    private UUID id;

    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int quantidade;

    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;

    @EqualsAndHashCode.Include
    private UUID tenantId;
}
