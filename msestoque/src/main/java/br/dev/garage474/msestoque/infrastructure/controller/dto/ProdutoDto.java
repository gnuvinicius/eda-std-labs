package br.dev.garage474.msestoque.infrastructure.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProdutoDto {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidade;

}
