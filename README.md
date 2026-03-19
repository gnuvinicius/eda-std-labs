# EDA Study Labs - Delivery SaaS

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)
![Status](https://img.shields.io/badge/status-development-orange)

## Sobre o Projeto

O **EDA Study Labs** e um laboratorio pratico para estudo de **Arquitetura Orientada a Eventos (EDA)** com foco principal em **DevOps** para microsservicos.

O projeto prioriza:

- pipeline de **CI/CD** com **GitHub Actions**;
- **dockerizacao** dos microsservicos;
- fluxo de **push** e **pull** de imagens Docker no **Amazon Elastic Container Registry (ECR)**;
- integracao entre servicos via **API Gateway** e **mensageria**.

## Arquitetura e Praticas

As aplicacoes seguem:

- principios de **Clean Code**;
- modelo de **Three-tier architecture** (controllers, services e repositories);
- comunicacao assincrona baseada em eventos e filas;
- exposicao de APIs por meio de **API Gateway**.

## Stack Tecnologico

### Servidores

- **Virtualização localhost com KVM/Qemu**
- **VMs Debian 13 com Docker**
- **Github Self-runners**

### Back-end

- **Java 21**
- **Spring Boot**
- **Django**
- **Django Rest Framework**

### Front-end

- **Vue.js**

### Persistencia e Cache

- **PostgreSQL**
- **MongoDB**
- **Redis**

### DevOps e Entrega Continua

- **GitHub Actions** para CI/CD
- **Docker** e **Docker Compose**
- **Amazon ECR** para versionamento e distribuicao de imagens

## Servicos do Dominio

1. **Order Service**: gerenciamento do ciclo de vida de pedidos.
2. **Catalog Service**: catalogo de produtos e estoque.
3. **Payment Service**: processamento de pagamentos.
4. **Delivery Service**: fluxo de entrega.
5. **Recommendation Service**: recomendacao de produtos.

## Objetivo de Estudo

Este repositorio existe para praticar, de ponta a ponta, o ciclo de desenvolvimento e operacao de microsservicos:

- desenvolvimento de servicos desacoplados;
- automacao de build e deploy;
- publicacao de imagens em registry privado;
- execucao de ambientes locais para validacao funcional.

## Como Executar (base local)

```bash
docker compose up -d
```

## Contribuicao

Projeto de estudo aberto para colaboracao. Sugestoes, issues e pull requests sao bem-vindos.
