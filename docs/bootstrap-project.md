Perfeito — vou te dar um **passo-a-passo completo** (prático) para iniciar um SaaS com **Next.js (frontend)** e **Spring Boot (backend)**. Vou cobrir: estrutura, comandos, autenticação (JWT via cookie HttpOnly), banco (Postgres + Flyway), Docker, `docker-compose`, e um CI/CD básico (GitHub Actions). Tudo em português e com trechos de código para você já copiar e colar.

---

# 1) Visão geral (rápida)

* Frontend: Next.js + TypeScript + App Router + Tailwind + React Query (ou SWR) + Formik/Zod para forms/validação.
* Backend: Spring Boot (Maven) + Spring Security + JWT + Spring Data JPA + PostgreSQL + Flyway.
* Integração de auth: backend emite **JWT** e grava em **cookie HttpOnly, Secure, SameSite=Strict**; frontend usa fetch com `credentials: 'include'`.
* Dev local com `docker-compose` (Postgres + backend + frontend).
* Deploy: contêineres Docker (AWS ECS / GCP Cloud Run / DigitalOcean App / Railway — escolha), CI com GitHub Actions.

---

# 2) Pré-requisitos

* Node.js (>=18) e npm/yarn
* Java 17+ (recomendado) e Maven
* Docker & docker-compose
* Git

---

# 3) Estrutura sugerida do repositório (monorepo opcional)

Exemplo com monorepo (recomendado para SaaS inicial):

```
/my-saas
├─ /frontend    (nextjs)
├─ /backend     (spring-boot)
├─ docker-compose.yml
├─ .github/workflows/
```

Se preferir, mantenha em repositórios separados.

---

# 4) Criar o frontend Next.js (TypeScript + App Router + Tailwind)

No terminal:

```bash
# cria app Next.js com typescript, app router e eslint
npx create-next-app@latest frontend --ts --eslint --app --src-dir --use-pnpm=false
# ou com npm
# cd frontend
# npm init next-app@latest frontend -- --typescript --eslint --app
```

Instale dependências recomendadas:

```bash
cd frontend
npm install tailwindcss postcss autoprefixer
npx tailwindcss init -p

# libs úteis
npm install @tanstack/react-query axios zod react-hook-form
# se usar next-auth (opcional): npm install next-auth
```

`tailwind.config.js` (exemplo rápido):

```js
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./app/**/*.{ts,tsx}", "./components/**/*.{ts,tsx}"],
  theme: { extend: {} },
  plugins: [],
}
```

Exemplo `.env.local` (frontend):

```
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api
```

Configurar fetch para enviar cookies:

```ts
// lib/api.ts
export const apiFetch = async (path: string, opts: RequestInit = {}) => {
  const base = process.env.NEXT_PUBLIC_API_BASE_URL;
  const res = await fetch(`${base}${path}`, {
    credentials: 'include', // essencial para cookies HttpOnly
    headers: { 'Content-Type': 'application/json' },
    ...opts
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
};
```

Exemplo de página de login (app route):

```tsx
// app/login/page.tsx (exemplo resumido)
"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { apiFetch } from "@/lib/api";

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    await apiFetch("/auth/login", {
      method: "POST",
      body: JSON.stringify({ email, password }),
    });
    router.push("/dashboard");
  }

  return (
    <form onSubmit={submit} className="max-w-md mx-auto p-6">
      <input value={email} onChange={e => setEmail(e.target.value)} placeholder="Email" />
      <input value={password} onChange={e => setPassword(e.target.value)} placeholder="Senha" type="password" />
      <button type="submit">Entrar</button>
    </form>
  );
}
```

Observações:

* Use React Query para cache de dados e gerenciamento de sincronização.
* Proteja rotas do lado do servidor (Server Components) verificando sessão via chamada ao backend (SSR) ou usando middleware do Next para redirect se necessário.

---

# 5) Criar o backend Spring Boot

Use o Spring Initializr (web) ou CLI. Dependências recomendadas:

* Spring Web
* Spring Security
* Spring Data JPA
* PostgreSQL Driver
* Flyway Migration
* Spring Boot DevTools
* Lombok (opcional)

Exemplo via curl (substitua `group`/`artifact` conforme desejar):
(ou use [https://start.spring.io](https://start.spring.io))

```bash
# cria um zip, depois descompacte para /backend
# alternativa: crie via IDE (IntelliJ)
```

`application.yml` (exemplo):

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/saasdb
    username: saas_user
    password: saas_pass
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        format_sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration
```

### Domain & Repository (exemplo usuário)

```java
// User.java
@Entity
@Table(name="users")
public class User {
  @Id @GeneratedValue
  private Long id;
  private String email;
  private String passwordHash;
  private String role;
  private boolean enabled;
  // getters/setters
}
```

```java
// UserRepository.java
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
```

### Service: autenticação e JWT

* Gere JWTs com `io.jsonwebtoken` (jjwt) ou `nimbus-jose-jwt`.
* Ao login: validar credenciais, emitir JWT com claims (userId, roles), setar cookie:

  ```java
  ResponseCookie cookie = ResponseCookie.from("SESSION", token)
      .httpOnly(true)
      .secure(true) // em dev pode false, em prod true
      .path("/")
      .sameSite("Strict")
      .maxAge(Duration.ofDays(7))
      .build();
  response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  ```
* Para cada requisição protegida: verificar cookie `SESSION`, validar JWT e popular `SecurityContext`.

Esqueleto de filtro JWT:

```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
    String token = Arrays.stream(Optional.ofNullable(req.getCookies()).orElse(new Cookie[0]))
                         .filter(c -> "SESSION".equals(c.getName()))
                         .map(Cookie::getValue).findFirst().orElse(null);
    if (token != null && validate(token)) {
      Authentication auth = getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    chain.doFilter(req, res);
  }
}
```

Configuração de segurança (resumida):

```java
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf().disable() // se usar cookies, considerar CSRF tokens
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .authorizeHttpRequests()
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
      .and()
      .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
```

**Nota importante sobre CSRF:** se você usa cookies para autenticação, habilitar proteção CSRF é recomendado. Uma alternativa é enviar header customizado (e.g. `X-CSRF-Token`) obtido via endpoint GET.

---

# 6) Migrations com Flyway

Crie scripts SQL em `src/main/resources/db/migration/V1__init.sql`:

```sql
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(50),
  enabled BOOLEAN DEFAULT TRUE
);
```

---

# 7) Dockerize (Dockerfiles) e docker-compose

`backend/Dockerfile`:

```dockerfile
FROM eclipse-temurin:17-jdk-jammy
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

`frontend/Dockerfile` (Next.js):

```dockerfile
FROM node:18-alpine AS deps
WORKDIR /app
COPY package*.json ./
RUN npm ci

FROM node:18-alpine AS builder
WORKDIR /app
COPY . .
RUN npm run build

FROM node:18-alpine AS runner
WORKDIR /app
ENV PORT 3000
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/public ./public
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./package.json
EXPOSE 3000
CMD ["npm","start"]  # ou "npm run start"
```

`docker-compose.yml` (local):

```yaml
version: '3.8'
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: saasdb
      POSTGRES_USER: saas_user
      POSTGRES_PASSWORD: saas_pass
    volumes:
      - pgdata:/var/lib/postgresql/data
    ports: ["5432:5432"]

  backend:
    build: ./backend
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/saasdb
      SPRING_DATASOURCE_USERNAME: saas_user
      SPRING_DATASOURCE_PASSWORD: saas_pass
    depends_on: ["db"]
    ports: ["8080:8080"]

  frontend:
    build: ./frontend
    environment:
      NEXT_PUBLIC_API_BASE_URL: http://localhost:8080/api
    ports: ["3000:3000"]
    depends_on: ["backend"]

volumes:
  pgdata:
```

Rodar tudo:

```bash
docker-compose up --build
```

---

# 8) Rotas API sugeridas (exemplo)

* `POST /api/auth/register` — criar usuário (hash da senha com BCrypt)
* `POST /api/auth/login` — autenticar e setar cookie JWT
* `POST /api/auth/logout` — apagar cookie
* `GET /api/auth/me` — retorna dados do usuário atual
* `/api/tenants/*` — endpoints multi-tenant (se aplicável) / planos etc.
* `/api/billing/*` — integração com Stripe (recomendado para SaaS)

---

# 9) Billing / SaaS concerns

* Integre Stripe (plano, subscription) no backend; eventos via webhooks.
* Armazene `customerId`, `subscriptionId` no usuário/tenant.
* Tenancy: comece com single-tenant por usuário; depois migre para **tenant_id** em cada tabela ou schema separado conforme necessidades de isolamento.

---

# 10) Segurança e produções hardening (essenciais)

* Nunca exponha secrets no repo. Use Secrets Manager (AWS/GCP) ou GitHub Secrets para CI.
* Cookies HttpOnly + Secure + SameSite=strict.
* Habilite rate limiting (ex: bucket4j para Spring Boot) nas APIs públicas.
* Use HTTPS em produção (TLS).
* Valide inputs com DTOs e Zod (frontend) / javax.validation (backend).
* Proteja endpoints de pagamento com verificação de webhooks (ver assinatura).

---

# 11) CI/CD — exemplo GitHub Actions (simples)

`.github/workflows/frontend.yml` (build & deploy):

```yaml
name: Frontend CI
on: [push]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with: node-version: 18
      - run: cd frontend && npm ci && npm run build
      # publicar imagem docker / deploy para provedor...
```

`.github/workflows/backend.yml` (maven build & test):

```yaml
name: Backend CI
on: [push]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Setup Java
        uses: actions/setup-java@v4
        with: java-version: '17'
      - run: cd backend && mvn -B -DskipTests package
      # testes, security scan, publicar imagem
```

Para deploy automático, adicione etapas para:

* Build Docker image -> push para registry (DockerHub / GitHub Container Registry)
* Deploy para seu serviço (ECS / Cloud Run / DigitalOcean / Railway)

---

# 12) Checklist rápido para rodar local (resumido)

1. Clone repo com `frontend/` e `backend/`.
2. Ajuste envs (`.env.local` no frontend; `application.yml` ou env vars no backend).
3. `docker-compose up --build`
4. Acesse: `http://localhost:3000` (frontend) e `http://localhost:8080` (backend).
5. Teste registro/login. Verifique cookie `SESSION` HttpOnly.

---

# 13) Snippets úteis finais

**bcrypt em Spring (exemplo)**:

```java
@Bean
public PasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder();
}
```

**Gerar token JWT (exemplo simples com jjwt)**:

```java
String jwt = Jwts.builder()
    .setSubject(userId.toString())
    .claim("roles", roles)
    .setIssuedAt(new Date())
    .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
    .signWith(SignatureAlgorithm.HS256, secretKey)
    .compact();
```

**Logout (Spring)**:

* No backend: setar cookie com `maxAge=0` para remover.
* No frontend: chamar `POST /api/auth/logout` com `credentials: 'include'`.

---

# 14) Próximos passos recomendados

* Implementar testes: unit + integração (Spring Boot Test, Jest/React Testing Library).
* Adicionar analytics e logs estruturados.
* Implementar feature flags para rollout (LaunchDarkly ou Unleash).
* Planejar multi-tenant (schema por tenant ou tenant_id).
* Integrar Stripe para billing e webhooks seguros.

---

Se quiser, já posso:

* Gerar um **esqueleto com comandos prontos** (arquivo `docker-compose.yml`, `Dockerfile`s, `application.yml`, `next.config.ts`) e **código de exemplo** (controller login + filtro JWT) pronto para colar — eu monto aqui já.
* Ou **montar o fluxo de autenticação** (sequência completa de requisições e payloads) com exemplos `curl`.

Qual dessas opções prefere agora? Posso já escrever os arquivos (por exemplo, `backend/src/...` e `frontend/...`) prontos para você copiar.
