# Contacs API

Uma API REST para gerencimanento de contatos. O objetivo deste projeto é praticar o aprendizado de Java Spring Boot.

## 🏗️ Arquitetura e Design
A aplicação segue os princípios da **Clean Architecture** e **SoC (Separation of Concerns)**, organizada em camadas bem definidas:

- **Web Layer**: Controllers REST utilizando `Spring MVC`, documentados via `Swagger/OpenAPI`.
- **Service Layer**: Lógica de negócio isolada com tratamento de exceções customizadas.
- **Persistence Layer**: Relacionamentos complexos entre objetos (Users, Transactions, Wallets) utilizando `Spring Data JPA` e `Hibernate`.
- **Security Layer**: Autenticação Stateless com `JWT` e proteção contra vulnerabilidades comuns.

## 🚀 Tecnologias Utilizadas
- **Java 25** & **Spring Boot 4.0.5+**
- **PostgreSQL**: Banco de dados relacional.
- **Flyway**: Gerenciamento de migrações de banco de dados.
- **Docker & Docker Compose**: Orquestração de ambiente e Multi-stage builds.
- **Testcontainers**: Testes de integração reais com containers PostgreSQL.
- **Logstash Logback Encoder**: Logs estruturados em formato JSON.

## 🛠️ Funcionalidades Implementadas
- [x] **Segurança**: Criptografia de senhas com BCrypt e tokens JWT.
- [x] **Relacionamentos**: Mapeamento de entidades complexas (Ex: Usuário -> Contatos).
- [x] **Validação**: Validação rigorosa de payloads de entrada via Bean Validation.
- [x] **Observabilidade**: Implementação de `Correlation ID` via MDC para rastreamento de logs.
- [x] **Resiliência**: Tratamento global de erros com códigos HTTP semânticos.

## 🧪 Estratégia de Testes (Pirâmide de Testes)
Este projeto mantém uma cobertura rigorosa seguindo a pirâmide de testes:
1.  **Testes Unitários**: Foco na lógica de negócio (Services) usando `JUnit 5` e `Mockito`.
2.  **Testes de Integração**: Validação de persistência e repositórios usando `Testcontainers`.
3.  **Testes de Slice (Web)**: Simulação de chamadas HTTP e segurança via `MockMvc`.

```bash
# Executar todos os testes
./gradlew test
```

## 📦 Como Executar
O projeto está totalmente "dockerizado". Basta ter o Docker instalado e executar:

```bash
docker-compose up --build
```

## 📊 Observabilidade
Os logs da aplicação são exportados em formato JSON estruturado, prontos para ingestão em stacks como ELK ou Splunk. Exemplo de log contextualizado:

```json
{
  "timestamp": "2026-04-21T08:43:00.123Z",
  "level": "INFO",
  "correlation_id": "a1b2c3d4-e5f6-7890",
  "transaction_id": "tx_998877",
  "message": "Contact created successfully."
}
```

## ✒️ Autor
Mateus Fornari Bitencourt - Senior Software Architect