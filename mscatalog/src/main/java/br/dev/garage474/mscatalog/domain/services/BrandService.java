package br.dev.garage474.mscatalog.domain.services;

import org.springframework.stereotype.Service;

/**
 * Domain Service - Regras de Negócio para Marcas.
 *
 * Responsabilidades:
 * - Encapsular lógica de negócio que envolve múltiplas entidades ou agregados
 * - Validar regras de negócio complexas
 * - Operações que não pertencem a uma única entidade
 *
 * Arquitetura: DDD - Domain Layer
 */
@Service
public class BrandService {

    /**
     * Valida as regras de negócio para criação de uma nova marca.
     *
     * Regras de Negócio:
     * 1. Nome não pode ser vazio ou nulo
     * 2. Nome deve ter entre 2 e 100 caracteres
     *
     * @param name Nome da marca
     * @throws IllegalArgumentException Se alguma regra for violada
     */
    public void validateBrandCreation(String name) {
        // Regra 1: Validar nome
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome da marca não pode ser vazio");
        }

        if (name.length() < 2) {
            throw new IllegalArgumentException("Nome da marca deve ter pelo menos 2 caracteres");
        }

        if (name.length() > 100) {
            throw new IllegalArgumentException("Nome da marca não pode ter mais de 100 caracteres");
        }
    }
}

