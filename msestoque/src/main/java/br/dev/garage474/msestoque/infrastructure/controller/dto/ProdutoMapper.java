package br.dev.garage474.msestoque.infrastructure.controller.dto;

import br.dev.garage474.msestoque.domain.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toDomain(ProdutoDto dto) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setQuantidade(dto.getQuantidade());
        return produto;
    }

    public ProdutoDto toDto(Produto produto) {
        ProdutoDto dto = new ProdutoDto();
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setQuantidade(produto.getQuantidade());
        return dto;
    }
}
