package br.dev.garage474.msestoque.infrastructure.repository;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.dev.garage474.msestoque.domain.Produto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
public class ProdutoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tenant_id", nullable = false, columnDefinition = "uuid")
    private UUID tenantId;

    @Column(name = "versao", nullable = false)
    private int versao;

    @Column(name = "status", nullable = false)
    private String status;

    private String nome;

    @Column(columnDefinition = "text")
    private String descricao;

    @Column(precision = 14, scale = 2)
    private BigDecimal preco;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private OffsetDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm;

    public Produto toDomain() {
        Produto p = new Produto();
        p.setId(this.id);
        p.setTenantId(this.tenantId);
        p.setNome(this.nome);
        p.setDescricao(this.descricao);
        p.setPreco(this.preco);
        p.setCriadoEm(this.criadoEm);
        p.setAtualizadoEm(this.atualizadoEm);
        return p;
    }

    public static ProdutoEntity fromDomain(Produto p) {
        ProdutoEntity e = new ProdutoEntity();
        e.setId(p.getId());
        e.setTenantId(p.getTenantId());
        e.setNome(p.getNome());
        e.setDescricao(p.getDescricao());
        e.setPreco(p.getPreco());
        e.setCriadoEm(p.getCriadoEm());
        e.setAtualizadoEm(p.getAtualizadoEm());
        return e;
    }
}
