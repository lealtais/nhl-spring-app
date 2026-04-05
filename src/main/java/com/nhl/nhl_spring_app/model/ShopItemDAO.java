package com.nhl.nhl_spring_app.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ShopItemDAO {

    private final JdbcTemplate jdbc;

    public ShopItemDAO(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    /**
     * Requisito: SELECT realizado com SQL puro (JDBC) com filtros
     */
    public List<ShopItem> buscarProdutos(String query, String category) {
        StringBuilder sql = new StringBuilder("SELECT * FROM shop_item WHERE 1=1");
        if (query != null && !query.isEmpty()) {
            sql.append(" AND (name ILIKE '%").append(query).append("%' OR description ILIKE '%").append(query).append("%')");
        }
        if (category != null && !category.isEmpty()) {
            sql.append(" AND category = '").append(category).append("'");
        }
        
        List<Map<String, Object>> rows = jdbc.queryForList(sql.toString());
        return rows.stream().map(this::mapRowToShopItem).collect(Collectors.toList());
    }

    private ShopItem mapRowToShopItem(Map<String, Object> row) {
        ShopItem item = new ShopItem();
        item.setId(((Number) row.get("id")).longValue());
        item.setName((String) row.get("name"));
        item.setDescription((String) row.get("description"));
        item.setPrice(((java.math.BigDecimal) row.get("price")));
        item.setImageUrl((String) row.get("image_url"));
        item.setCategory((String) row.get("category"));
        return item;
    }

    public List<ShopItem> listarTodosProdutos() {
        String sql = "SELECT * FROM shop_item";
        List<Map<String, Object>> rows = jdbc.queryForList(sql);
        return rows.stream().map(this::mapRowToShopItem).collect(Collectors.toList());
    }

    /**
     * Requisito: INSERT realizado com SQL puro (JDBC)
     */
    public void inserirProduto(ShopItem item) {
        String sql = "INSERT INTO shop_item (name, description, price, image_url, category) VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql, 
            item.getName(), 
            item.getDescription(), 
            item.getPrice(), 
            item.getImageUrl(), 
            item.getCategory()
        );
    }
}
