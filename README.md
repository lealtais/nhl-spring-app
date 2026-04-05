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

1. Certifique-se de que o **PostgreSQL** está rodando em `localhost:5432`.
2. Verifique se o banco `nhldb` foi criado.
3. Certifique-se de configurar sua senha no arquivo `src/main/resources/application.yml` (já incluído para facilitar o teste).
4. Execute o comando: `./mvnw spring-boot:run`
5. Acesse: `http://localhost:8081`

---
*Nota: A segurança é gerenciada pelo Spring Security. Para usar o carrinho, crie uma conta em "Cadastrar".*
