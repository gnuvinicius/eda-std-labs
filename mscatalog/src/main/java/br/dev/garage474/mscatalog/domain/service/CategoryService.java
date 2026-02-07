package br.dev.garage474.mscatalog.domain.service;

import org.springframework.stereotype.Service;

/**
 * Domain Service - Regras de Negócio para Categorias.
 *
 * Responsabilidades:
 * - Encapsular lógica de negócio que envolve múltiplas entidades ou agregados
 * - Validar regras de negócio complexas
 * - Operações que não pertencem a uma única entidade
 *
 * Arquitetura: DDD - Domain Layer
 */
@Service
public class CategoryService {

    /**
     * Valida as regras de negócio para criação de uma nova categoria.
     *
     * Regras de Negócio:
     * 1. Nome não pode ser vazio ou nulo
     * 2. Nome deve ter entre 2 e 100 caracteres
     *
     * @param name Nome da categoria
     * @throws IllegalArgumentException Se alguma regra for violada
     */
    public void validateCategoryCreation(String name) {
        // Regra 1: Validar nome
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome da categoria não pode ser vazio");
        }

        if (name.length() < 2) {
            throw new IllegalArgumentException("Nome da categoria deve ter pelo menos 2 caracteres");
        }

        if (name.length() > 100) {
            throw new IllegalArgumentException("Nome da categoria não pode ter mais de 100 caracteres");
        }
    }
}

