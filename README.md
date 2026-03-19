# EDA Study Labs - Delivery SaaS

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)
![Status](https://img.shields.io/badge/status-development-orange)

## Sobre o Projeto

O **EDA Study Labs** é um laboratório prático para estudo de **Arquitetura Orientada a Eventos (EDA)** com foco principal em **DevOps** para microsserviços.

O projeto prioriza:

- pipeline de **CI/CD** com **GitHub Actions**;
- **dockerização** dos microsserviços;
- fluxo de **push** e **pull** de imagens Docker no **Amazon Elastic Container Registry (ECR)**;
- integração entre serviços via **API Gateway** e **mensageria**.

## Arquitetura e Práticas

As aplicações seguem:

- princípios de **Clean Code**;
- modelo de **Three-tier architecture** (controllers, services e repositories);
- comunicação assíncrona baseada em eventos e filas;
- exposição de APIs por meio de **API Gateway**.

## Stack Tecnológico

### Servidores

- **Virtualização local com KVM/QEMU**
- **VMs Debian 13 com Docker**
- **GitHub Self-hosted Runners**

### Back-end

- **Java 21**
- **Spring Boot**
- **Django**
- **Django Rest Framework**

### Front-end

- **Vue.js**

### Persistência e Cache

- **PostgreSQL**
- **MongoDB**
- **Redis**

### DevOps e Entrega Contínua

- **GitHub Actions** para CI/CD
- **Docker** e **Docker Compose**
- **Amazon ECR** para versionamento e distribuição de imagens

## Serviços do Domínio

1. **Order Service**: gerenciamento do ciclo de vida de pedidos.
2. **Catalog Service**: catálogo de produtos e estoque.
3. **Payment Service**: processamento de pagamentos.
4. **Delivery Service**: fluxo de entrega.
5. **Recommendation Service**: recomendação de produtos.

## Objetivo de Estudo

Este repositório existe para praticar, de ponta a ponta, o ciclo de desenvolvimento e operação de microsserviços:

- desenvolvimento de serviços desacoplados;
- automação de build e deploy;
- publicação de imagens em registry privado;
- execução de ambientes locais para validação funcional.

## Contribuição

Projeto de estudo aberto para colaboração. Sugestões, issues e pull requests são bem-vindos.
