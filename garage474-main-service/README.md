Perfeito — abaixo está um passo-a-passo prático, em português, para criar um projeto Java Maven que simula um sistema legado (para laboratório) usando: Java 21, Jakarta EE (serviços de plataforma), SOAP (JAX‑WS via Apache CXF), XML, JPA + JTA (container-managed), CDI e JBoss/WildFly como servidor de aplicação. Incluo exemplo de estrutura, arquivos essenciais (pom, persistence.xml, web.xml, uma entidade JPA, EJB/CXF SOAP endpoint e um repositório), comandos de build e instruções de deploy e de configuração do datasource no WildFly.

Resumo do fluxo:
1) Ambiente: JDK 21 + Maven + WildFly/JBoss compatível com Java 21.  
2) Criar projeto Maven (war) com dependência jakartaee-api (scope provided) + Apache CXF para SOAP.  
3) Implementar JPA (persistence.xml), uma entidade, um EJB Stateless que usa @PersistenceContext (JTA), um repositório e um Web Service SOAP (legacy-style: EJB + @WebService).  
4) Incluir web.xml para simular deploy descriptor legado e configurar contexto.  
5) Empacotar com mvn package e fazer deploy no WildFly (copiar .war para standalone/deployments ou usar WildFly Maven plugin).  
6) Testar via WSDL e endpoints SOAP e verificar persistência.

OBS importantes:
- Verifique a compatibilidade do WildFly/JBoss com Java 21 antes de começar. Use a versão mais recente do WildFly que suporte Java 21 (WildFly 28+ / 29+ típico — confira release notes). Se usar JBoss EAP, confirme suporte específico.
- As dependências para SOAP: eu recomendo Apache CXF (maturidade e controle) e usaremos JAX‑WS annotations em estilo legado (ex.: @WebService). Se preferir Metro (JAX‑WS RI), substitua conforme desejar.
- O projeto é pensado para laboratório: portanto mostro também como "simular legado" usando descriptors XML (web.xml), EJBs, e WSDL/SOAP clássicos.

Agora, arquivos exemplo. Crie o projeto com o groupId/artifactId que preferir (aqui uso com.example:legacy-soap-app).

```
mvn archetype:generate -B \
-DarchetypeArtifactId=maven-archetype-webapp \
-DgroupId=br.dev.garage474 \
-DartifactId=legacy-soap-app \
-Dpackage=br.dev.garage474.legacy \
-Dversion=1.0.0-SNAPSHOT
```


Arquivo: pom.xml
```xml name=pom.xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.example</groupId>
  <artifactId>legacy-soap-app</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>war</packaging>

  <properties>
    <maven.compiler.release>21</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <jakartaee.version>10.0.0</jakartaee.version>
    <cxf.version>4.0.0</cxf.version>
    <h2.version>2.1.214</h2.version>
  </properties>

  <dependencies>
    <!-- Jakarta EE API (container fornece em runtime) -->
    <dependency>
      <groupId>jakarta.platform</groupId>
      <artifactId>jakarta.jakartaee-api</artifactId>
      <version>${jakartaee.version}</version>
      <scope>provided</scope>
    </dependency>

    <!-- Apache CXF para JAX-WS -->
    <dependency>
      <groupId>org.apache.cxf</groupId>
      <artifactId>cxf-rt-frontend-jaxws</artifactId>
      <version>${cxf.version}</version>
    </dependency>
    <dependency>
      <groupId>org.apache.cxf</groupId>
      <artifactId>cxf-rt-transports-http</artifactId>
      <version>${cxf.version}</version>
    </dependency>

    <!-- H2 (runtime para laboratório) - versão definida -->
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <version>${h2.version}</version>
      <scope>runtime</scope>
    </dependency>
  </dependencies>

  <build>
    <finalName>legacy-soap-app</finalName>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.11.0</version>
        <configuration>
          <release>${maven.compiler.release}</release>
        </configuration>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-war-plugin</artifactId>
        <version>3.4.0</version>
        <configuration>
          <failOnMissingWebXml>false</failOnMissingWebXml>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

Estrutura de diretórios (resumo)
- src/main/java/com/example/legacy/... (código)
- src/main/resources/META-INF/persistence.xml
- src/main/webapp/WEB-INF/web.xml
- src/main/webapp/WEB-INF/jboss-web.xml (opcional)
- src/main/webapp/ (arquivos estáticos)

Arquivo: src/main/resources/META-INF/persistence.xml
```xml name=src/main/resources/META-INF/persistence.xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence
                                 https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">
  <persistence-unit name="legacyPU" transaction-type="JTA">
    <!-- O datasource será configurado no WildFly -->
    <jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>
    <properties>
      <!-- Propriedades opcionais; Hibernate é provedor comum no WildFly -->
      <property name="jakarta.persistence.schema-generation.database.action" value="none"/>
    </properties>
  </persistence-unit>
</persistence>
```

Arquivo: src/main/webapp/WEB-INF/web.xml (simula descriptor legado)
```xml name=src/main/webapp/WEB-INF/web.xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://jakarta.ee/xml/ns/jakartaee
                             http://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
         version="6.0"
         metadata-complete="true">
  <display-name>Legacy SOAP App</display-name>

  <!-- (Opcional) Se quiser mapear CXF servlet manualmente ou outros servlets legados, adicione aqui. -->
</web-app>
```

Arquivo opcional: src/main/webapp/WEB-INF/jboss-web.xml (configura context-root)
```xml name=src/main/webapp/WEB-INF/jboss-web.xml
<?xml version="1.0" encoding="UTF-8"?>
<jboss-web>
  <context-root>/legacy-soap-app</context-root>
</jboss-web>
```

Exemplo de entidade JPA: src/main/java/com/example/legacy/model/User.java
```java name=src/main/java/com/example/legacy/model/User.java
package com.example.legacy.model;

import jakarta.persistence.*;

@Entity
@Table(name = "L_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    public User() {}

    public User(String name) { this.name = name; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

Repositório/EJB para acessar JPA (container-managed, JTA): src/main/java/com/example/legacy/repo/UserRepository.java
```java name=src/main/java/com/example/legacy/repo/UserRepository.java
package com.example.legacy.repo;

import com.example.legacy.model.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UserRepository {

    @PersistenceContext(unitName = "legacyPU")
    private EntityManager em;

    public User create(String name) {
        User u = new User(name);
        em.persist(u);
        return u;
    }

    public User find(Long id) {
        return em.find(User.class, id);
    }

    public List<User> listAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
}
```

SOAP endpoint (legado estilo EJB + @WebService): src/main/java/com/example/legacy/ws/LegacyService.java
```java name=src/main/java/com/example/legacy/ws/LegacyService.java
package com.example.legacy.ws;

import com.example.legacy.repo.UserRepository;
import com.example.legacy.model.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@WebService(serviceName = "LegacyService", targetNamespace = "http://legacy.example.com/")
public class LegacyService {

    @Inject
    private UserRepository userRepo;

    @WebMethod
    public String sayHello(String name) {
        // Exemplo simples: cria um usuário para simular uso de JPA/JTA dentro do serviço SOAP
        User created = userRepo.create(name);
        return "Olá, " + name + " (id=" + created.getId() + ")";
    }

    @WebMethod
    public List<String> listUserNames() {
        List<User> users = userRepo.listAll();
        return users.stream().map(User::getName).collect(Collectors.toList());
    }
}
```

Observações sobre o endpoint SOAP:
- Ao empacotar e fazer deploy no WildFly, o container (se configurado para JAX-WS) expõe automaticamente o WSDL do EJB webservice. A URL típica (dependendo do contexto e configuração) será algo como:
  http://localhost:8080/legacy-soap-app/LegacyService?wsdl
- Se o WildFly em uso não expõe automaticamente via JAX‑WS, use Apache CXF configurado via servlet (mapear cxf-servlet no web.xml) para publicar endpoints. Para laboratório, a abordagem EJB+@WebService costuma funcionar em servidores Java EE/Jakarta compatíveis.

Passo-a-passo prático (com comandos):

1) Preparar ambiente
   - Instale JDK 21 e defina JAVA_HOME.
   - Instale Maven (ou use wrapper).
   - Baixe e instale WildFly/JBoss (versão que suporte Java 21).
   - Start WildFly: $WILDFLY_HOME/bin/standalone.sh (Linux/mac) ou standalone.bat (Windows).

2) Criar projeto Maven
   - mvn archetype:generate ... (ou crie diretório e coloque pom.xml acima).
   - Estruture src/main conforme arquivos acima.

3) Verificar/ajustar dependências
   - Confirme versões de Jakarta EE e CXF (usar versões atualizadas compatíveis com Jakarta). Se usar Metro, trocar dependências.

4) Configurar datasource no WildFly (exemplo usando CLI, cria ExampleDS apontando H2 para laboratório)
   - Executar $WILDFLY_HOME/bin/jboss-cli.sh --connect
   - Comandos (exemplo H2 em memória - para laboratório):
     /subsystem=datasources/data-source=ExampleDS:remove
     /subsystem=datasources/data-source=ExampleDS:add(jndi-name=java:jboss/datasources/ExampleDS,driver-name=h2,connection-url=jdbc:h2:mem:lab;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE,user=sa,password=sa)
   - (Se usar Postgres/MySQL, crie driver module ou configure datasource apontando para driver já instalado.)

5) Build do projeto
   - mvn clean package
   - Você obterá target/legacy-soap-app.war

6) Deploy
   - Copie target/legacy-soap-app.war para $WILDFLY_HOME/standalone/deployments/ (WildFly irá auto-deploy)
   - Alternativamente, use WildFly Maven Plugin para deploy automatizado.

7) Teste do SOAP
   - Abra navegador/cliente SOAP para: http://localhost:8080/legacy-soap-app/LegacyService?wsdl
   - Use SoapUI ou curl (se for um POST SOAP), ou gere cliente com wsimport/cxf-codegen-plugin para testar operações sayHello, listUserNames.

8) Teste de persistência
   - Chame sayHello via SOAP para criar usuário.  
   - Verifique DB (H2 console ou logs) ou implemente um endpoint REST simples para listar entities para debug.

Como "simular um sistema legado" (dicas práticas para laboratório)
- Use descriptors XML (web.xml, ejb-jar.xml) com metadata-complete, em vez só de anotações.
- Use EJBs @Stateless, transações container-managed (não usar @Transactional do Jakarta CDI).
- Prefira mensagens SOAP com WSDL gerado automaticamente para simular integração legada.
- Deixe logs verbosos para ver SQL do Hibernate (configure properties temporariamente).
- Simule conversões XML antigas (use JAXB custom, use classes que montem/parsem XML bruto).
- Adicione testes que usem arquivos WSDL e SOAP com payloads antigos (carregue XML de exemplos).
- Introduza bagunça intencional (nomeclaturas antigas, tabelas com prefixo L_, uso de SQL nativo) para treinar migração/integração.

Checklist rápido de verificação
- [ ] Java 21 instalado e apontando para o WildFly.
- [ ] WildFly suporta Java 21.
- [ ] Datasource configurado (java:jboss/datasources/ExampleDS) ou altere persistence.xml.
- [ ] mvn package gera .war sem erros.
- [ ] Deploy .war no WildFly e WSDL acessível.
- [ ] SOAP operations executam e persistência JPA grava corretamente.

Extras (opções e extensões)
- Gerar código cliente SOAP: use cxf-codegen-plugin ou wsimport (caso o WSDL seja compatível).
- Para registrar logs de SQL: no WildFly configure subsistema logging ou adicione propriedades de hibernate (só para dev).
- Se preferir não expor EJBs via @WebService, publique endpoint com CXF servlet (mapear cxf-servlet.xml na WEB-INF).
- Para simular autenticação antiga: configure BASIC auth no web.xml e no WildFly security domain (legacy-style).

Arquivo README.md (comandos resumidos e notas)
````markdown name=README.md
# Legacy SOAP App (laboratório)

Projeto de exemplo para simular um sistema legado usando:
- Java 21
- Jakarta EE (JPA, JTA, CDI, EJB)
- SOAP (JAX-WS / Apache CXF)
- Implantação em WildFly / JBoss

Quickstart:
1. Configure JAVA_HOME para JDK 21 e instale Maven.
2. Configure WildFly (versão compatível com Java 21).
3. Configure datasource `java:jboss/datasources/ExampleDS` no WildFly.
4. Build: `mvn clean package`
5. Deploy: copie `target/legacy-soap-app.war` para `$WILDFLY_HOME/standalone/deployments/`
6. Acesse WSDL: `http://localhost:8080/legacy-soap-app/LegacyService?wsdl`
7. Teste operações SOAP com SoapUI ou gerando um cliente JAX-WS.

Notas:
- O projeto usa `jakarta.jakartaee-api` com scope provided: o servidor fornece as implementações.
- Ajuste datasource no `persistence.xml` se não usar `ExampleDS`.
- Para laboratório, considere usar H2 (já incluído) para facilitar testes locais.
````

Próximos passos que posso executar para você (diga qual prefere):
- Gerar um esqueleto de projeto Maven inteiro com esses arquivos e te entregar zip/arquivo com estrutura.  
- Adaptar o projeto para usar Apache CXF via servlet (em vez de EJB+@WebService) com web.xml e cxf-servlet.xml.  
- Criar scripts para configurar o datasource no WildFly para H2 / PostgreSQL (CLI + CLI script).  
- Fornecer um exemplo de cliente SOAP (Java) gerado com wsimport/cxf que consome o WSDL automaticamente.  
- Incluir exemplos de ejb-jar.xml / webservices.xml para ainda mais "sabor legado".


## Dockerfile para criar imagem.

Segue um modelo pronto para você usar no seu laboratório. Vou incluir:

- Dockerfile (base WildFly, adiciona driver PostgreSQL, copia standalone.xml customizado e faz deploy do .war de target/)
- module.xml para o módulo do driver PostgreSQL
- standalone.xml (template com datasource configurado usando variáveis de ambiente: DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD)

Observação importante: adapte a tag da imagem WildFly para a versão que você confirmar ser compatível com Java 21. Teste localmente antes de usar em produção.

```text
name=Dockerfile
```
```dockerfile
# Use uma versão do WildFly compatível com Java 21 (ajuste a tag conforme necessário)
ARG WILDFLY_VERSION=28.0.1.Final
FROM quay.io/wildfly/wildfly:${WILDFLY_VERSION}

ENV WILDFLY_HOME=/opt/jboss/wildfly
ENV JBOSS_HOME=${WILDFLY_HOME}

# Versão do driver PostgreSQL (ajuste conforme necessário)
ARG POSTGRESQL_DRIVER_VERSION=42.6.0

# Cria diretórios e adiciona o driver PostgreSQL como módulo
RUN mkdir -p ${WILDFLY_HOME}/modules/system/layers/base/org/postgresql/main \
 && curl -L -o /tmp/postgresql.jar https://repo1.maven.org/maven2/org/postgresql/postgresql/${POSTGRESQL_DRIVER_VERSION}/postgresql-${POSTGRESQL_DRIVER_VERSION}.jar \
 && mv /tmp/postgresql.jar ${WILDFLY_HOME}/modules/system/layers/base/org/postgresql/main/postgresql.jar

# Copia o module.xml do driver (substitua pelo arquivo module.xml abaixo)
COPY modules/org/postgresql/main/module.xml ${WILDFLY_HOME}/modules/system/layers/base/org/postgresql/main/module.xml

# Copia o standalone.xml customizado (substitua pelo arquivo standalone.xml abaixo)
COPY configuration/standalone.xml ${WILDFLY_HOME}/standalone/configuration/standalone.xml

# Copia o WAR gerado pelo Maven (o .war deve existir em target/ no contexto de build)
# Execute `mvn clean package` antes de `docker build`
COPY target/legacy-soap-app.war ${WILDFLY_HOME}/standalone/deployments/legacy-soap-app.war
# .dodeploy garante que o WildFly implante o .war em startup
RUN touch ${WILDFLY_HOME}/standalone/deployments/legacy-soap-app.war.dodeploy

EXPOSE 8080 9990

# Start WildFly
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
```

```text
name=modules/org/postgresql/main/module.xml
```
```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.5" name="org.postgresql">
  <resources>
    <resource-root path="postgresql.jar"/>
  </resources>
  <dependencies>
    <module name="javax.api"/>
    <module name="org.jboss.logging"/>
  </dependencies>
</module>
```

```text
name=configuration/standalone.xml
```
```xml
<?xml version='1.0' encoding='UTF-8'?>
<!--
  Modelo de standalone.xml com datasource PostgreSQL.
  Este arquivo é um template para WildFly. Ajuste conforme sua versão do WildFly.
  O datasource usa expressões de ambiente para permitir parametrização em tempo de execução:
    DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD
-->
<server xmlns="urn:jboss:domain:18.0">
  <extensions>
    <!-- extensões mínimas necessárias; mantenha as que seu servidor requer -->
    <extension module="org.jboss.as.clustering.infinispan"/>
    <extension module="org.jboss.as.connector"/>
    <extension module="org.jboss.as.deployment-scanner"/>
    <extension module="org.jboss.as.ejb3"/>
    <extension module="org.jboss.as.jaxws"/>
    <extension module="org.jboss.as.jaxrs"/>
    <extension module="org.jboss.as.jpa"/>
    <extension module="org.jboss.as.naming"/>
    <extension module="org.jboss.as.remoting"/>
    <extension module="org.jboss.as.transactions"/>
    <extension module="org.jboss.as.webservices"/>
    <extension module="org.wildfly.extension.undertow"/>
    <extension module="org.jboss.as.logging"/>
    <extension module="org.jboss.as.security"/>
    <extension module="org.jboss.as.server"/>
    <extension module="org.jboss.as.clustering.singleton"/>
    <!-- ... outras extensões padrão do standalone.xml podem ser mantidas -->
  </extensions>

  <management>
    <security-realms>
      <security-realm name="ManagementRealm">
        <server-identities>
          <secret value="quickstart"/>
        </server-identities>
        <authentication>
          <local default-user="$local"/>
          <properties path="mgmt-users.properties" relative-to="jboss.server.config.dir"/>
        </authentication>
      </security-realm>
    </security-realms>
  </management>

  <profile>
    <!-- subsystems essenciais (simplificado). Mantenha outros subsystems conforme sua versão. -->
    <subsystem xmlns="urn:jboss:domain:logging:7.0">
      <console-handler name="CONSOLE">
        <level name="INFO"/>
        <formatter>
          <named-formatter name="COLOR-PATTERN"/>
        </formatter>
      </console-handler>
      <root-logger>
        <level name="INFO"/>
        <handlers>
          <handler name="CONSOLE"/>
        </handlers>
      </root-logger>
      <formatter name="COLOR-PATTERN">
        <pattern-formatter pattern="%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n"/>
      </formatter>
    </subsystem>

    <!-- Datasources subsystem com datasource PostgreSQL configurado -->
    <subsystem xmlns="urn:jboss:domain:datasources:7.0">
      <datasources>
        <!-- Datasource JTA para a aplicação -->
        <datasource jta="true" jndi-name="java:jboss/datasources/LegacyDS" pool-name="LegacyDS" enabled="true" use-ccm="true">
          <connection-url>jdbc:postgresql://${env.DB_HOST:postgres}:${env.DB_PORT:5432}/${env.DB_NAME:legacydb}</connection-url>
          <driver>postgresql</driver>
          <security>
            <user-name>${env.DB_USER:legacyuser}</user-name>
            <password>${env.DB_PASSWORD:legacypass}</password>
          </security>
          <validation>
            <validate-on-match>false</validate-on-match>
            <background-validation>false</background-validation>
          </validation>
          <timeout>
            <set-tx-query-timeout>true</set-tx-query-timeout>
            <blocking-timeout-millis>5000</blocking-timeout-millis>
          </timeout>
        </datasource>

        <!-- Optional: uma datasource de exemplo (ExampleDS) se sua app usar) -->
        <drivers>
          <driver name="postgresql" module="org.postgresql">
            <driver-class>org.postgresql.Driver</driver-class>
            <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
          </driver>
        </drivers>
      </datasources>
    </subsystem>

    <!-- Mantenha outros subsistemas necessários (ejb3, jpa, transactions, undertow, etc.) -->
    <subsystem xmlns="urn:jboss:domain:transactions:7.0">
      <core-environment>
        <process-id>
          <uuid/>
        </process-id>
      </core-environment>
      <coordinator-environment/>
      <object-store path="tx-object-store" relative-to="jboss.server.data.dir"/>
    </subsystem>

    <subsystem xmlns="urn:jboss:domain:jpa:4.0">
      <jpa default-datasource="java:jboss/datasources/LegacyDS" default-extended-persistence-inheritance="DEEP"/>
    </subsystem>

    <subsystem xmlns="urn:jboss:domain:undertow:14.0">
      <server name="default-server">
        <http-listener name="default" socket-binding="http"/>
        <host name="default-host" alias="localhost"/>
      </server>
      <servlet-container name="default">
        <jsp-config/>
      </servlet-container>
    </subsystem>

    <!-- Adicione aqui outros subsistemas conforme sua necessidade -->
  </profile>

  <interfaces>
    <interface name="management">
      <inet-address value="${jboss.bind.address.management:0.0.0.0}"/>
    </interface>
    <interface name="public">
      <inet-address value="${jboss.bind.address:0.0.0.0}"/>
    </interface>
  </interfaces>

  <socket-binding-group name="standard-sockets" default-interface="public">
    <socket-binding name="http" port="${jboss.http.port:8080}"/>
    <socket-binding name="https" port="${jboss.https.port:8443}"/>
    <socket-binding name="management-http" port="${jboss.management.http.port:9990}"/>
    <socket-binding name="ajp" port="8009"/>
  </socket-binding-group>
</server>
```

O que eu preparei e por quê
- Dockerfile: pega uma imagem WildFly, adiciona o driver PostgreSQL e copia seu WAR para deployments. É o fluxo padrão para criar uma imagem que já inicia com a aplicação deployada.
- module.xml: define o módulo org.postgresql para que o WildFly reconheça o driver.
- standalone.xml: template com o datasource apontando para jdbc:postgresql://${env.DB_HOST:...} usando expressões de ambiente. Assim você pode parametrizar host/porta/banco/usuário/senha no docker run.

Como usar (exemplo)
1) Gere o .war:
   mvn clean package

2) Estrutura do build context (exemplo)
   .
   ├─ Dockerfile
   ├─ modules/org/postgresql/main/module.xml
   ├─ configuration/standalone.xml
   ├─ target/legacy-soap-app.war   <-- gerado pelo mvn
   └─ ...

3) Build da imagem:
   docker build -t legacy-soap-wildfly:local .

4) Run (exemplo com Postgres local/contêiner):
   docker run --rm -e DB_HOST=meu-postgres-host -e DB_PORT=5432 \
     -e DB_NAME=legacydb -e DB_USER=legacyuser -e DB_PASSWORD=legacypass \
     -p 8080:8080 -p 9990:9990 legacy-soap-wildfly:local

Dicas / ajustes finais
- Verifique a versão do WildFly compatível com Java 21 e ajuste ARG WILDFLY_VERSION no Dockerfile.
- Se preferir não copiar um standalone.xml inteiro, você pode manter o default e usar um script JBoss CLI para adicionar o datasource em tempo de startup. Isso é mais seguro para atualizações da configuração padrão do WildFly.
- Se for usar um container PostgreSQL na mesma rede Docker, use --network e o nome do serviço como DB_HOST.
- Para debug, veja os logs do container (docker logs -f <id>) e endpoints: http://localhost:8080/legacy-soap-app/LegacyService?wsdl (ajuste nome/context-root conforme jboss-web.xml).

Quer que eu:
- gere um Dockerfile ajustado para uma tag WildFly específica (por exemplo 29.0.0.Final) e para a sua versão do driver PostgreSQL?
- ou gere o script JBoss CLI para adicionar o datasource em tempo de startup (em vez de substituir standalone.xml)?

Diga qual opção prefere que eu gere a seguir e eu preparo os arquivos prontos para você copiar.