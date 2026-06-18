package com.nhl.nhl_spring_app.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AppUserDAO {

    private final JdbcTemplate jdbc;
    private final PasswordEncoder passwordEncoder;

    public AppUserDAO(DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.jdbc = new JdbcTemplate(dataSource);
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Requisito: INSERT realizado com SQL puro (JDBC)
     */
    public void inserirUsuarioManual(String username, String password, String email) {
        String sql = "INSERT INTO app_user (username, password, role, email, is_banned, created_at) VALUES (?, ?, ?, ?, false, CURRENT_TIMESTAMP)";
        jdbc.update(sql, username, passwordEncoder.encode(password), "USER", email);
    }
}
