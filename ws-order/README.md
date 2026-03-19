## WebService ORDER v1.0.0

O `ws-order` e uma aplicacao de estudo que simula um webservice SOAP para operacoes relacionadas a pedidos (order), utilizando stack Java/Jakarta EE com empacotamento em WAR para execucao em servidor de aplicacao.

## Objetivo

Este modulo foi criado para apoiar os estudos de arquitetura orientada a eventos (EDA) e DevOps, permitindo validar cenarios de integracao com um servico no formato SOAP em ambiente controlado.

## Geracao inicial do projeto

```bash
mvn -B archetype:generate \
 -DarchetypeGroupId=org.wildfly.archetype \
 -DarchetypeArtifactId=wildfly-jakartaee-webapp-archetype \
 -DarchetypeVersion=33.0.0.Final \
 -DgroupId=br.dev.garage474 \
 -DartifactId=msorder \
 -Dversion=1.0-SNAPSHOT \
 -Dpackage=br.dev.garage474.msorder
```

## CI/CD

O workflow do GitHub Actions desse servico foi ajustado para **execucao manual apenas** (`workflow_dispatch`), removendo o gatilho por `push` para reduzir o consumo de memoria RAM no ambiente de estudos de DevOps/EDA.[^1]

[^1]: No momento, o `ws-order` encontra-se desativado no fluxo regular do laboratorio.
