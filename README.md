# 🌲 LeafsNation • NHL Spring App

Uma aplicação Spring Boot moderna para fãs do Toronto Maple Leafs, migrada para **PostgreSQL** e focada em performance e requisitos acadêmicos.

## ✨ Novas Funcionalidades (Leafs Nation Shop)

A loja foi totalmente revitalizada com uma experiência de usuário premium:
- 🛡️ **Cards de Colecionador**: Design inspirado em cartões de jogadores da NHL.
- 🔍 **Busca em Tempo Real**: Encontre produtos instantaneamente por nome.
- 🏷️ **Filtros por Categoria**: Navegue facilmente entre Roupas, Acessórios, Colecionáveis e Kits.
- 🛒 **Carrinho AJAX + Snackbar**: Adicione itens sem recarregar a página. Um "aviso" (snackbar) moderno confirma a adição no centro inferior da tela.
- 🔐 **Navegação Direta**: O fluxo foi simplificado para levar o fã direto para o que importa: os produtos do Toronto!

---

## 👨‍🏫 Requisitos do Professor (SQL Puro / JDBC)

Este projeto cumpre rigorosamente os requisitos de persistência manual via **JDBC (SQL Puro)**, evitando abstrações automáticas em áreas críticas:

### 1. Operações de SELECT (Busca e Filtros)
As consultas de produtos no catálogo não usam o JPA Repository para a busca. Elas são realizadas diretamente no banco via SQL dinâmico.
- **Localização:** `src/main/java/com/nhl/nhl_spring_app/model/ShopItemDAO.java`
- **Método:** `getFilteredItems(String query, String category)`
- **Lógica:** Implementa um `SELECT` com filtros `LIKE` e `WHERE` dinâmicos usando `JdbcTemplate`.

### 2. Operações de INSERT (Cadastro de Usuários)
O registro de novos usuários no sistema é feito manualmente via SQL, garantindo o controle total sobre a persistência.
- **Localização:** `src/main/java/com/nhl/nhl_spring_app/model/AppUserDAO.java`
- **Método:** `inserirUsuarioManual(String username, String password)`
- **Lógica:** Executa um `INSERT INTO app_users` direto no PostgreSQL.

---

## 🛠️ Stack Tecnológica
- **Backend:** Spring Boot 3.3.4 (Java)
- **Banco de Dados:** PostgreSQL (`nhldb`)
- **Acesso a Dados:** JdbcTemplate (SQL Puro) & Spring Data JPA
- **Frontend:** Thymeleaf, CSS3 Moderno, JavaScript Fetch API (AJAX)

## 📦 Como Iniciar

Por razões de **segurança e boas práticas**, os arquivos de configuração que contêm senhas reais foram removidos do histórico do Git. Para rodar o projeto localmente, siga estes passos:

1.  Certifique-se de que o **PostgreSQL** está rodando em `localhost:5432` e o banco `nhldb` foi criado.
2.  **Configuração de Banco de Dados**:
    -   Vá até a pasta `src/main/resources/`.
    -   Copie o arquivo `application.yml.example` e renomeie a cópia para `application.yml`.
    -   Edite este novo arquivo `application.yml` e informe as credenciais (username e password) do seu banco de dados local na **linha 11**.
3.  Execute o comando: `./mvnw spring-boot:run`
4.  Acesse: `http://localhost:8081`

---
> [!TIP]
> **Nota de Segurança**: Esta abordagem de usar um arquivo `.example` é o padrão da indústria para garantir que senhas privadas não sejam expostas publicamente no GitHub, protegendo a integridade do sistema e do desenvolvedor.
