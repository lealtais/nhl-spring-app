package com.nhl.nhl_spring_app.service;

import com.nhl.nhl_spring_app.model.ShopItem;
import com.nhl.nhl_spring_app.model.ShopItemDAO;
import com.nhl.nhl_spring_app.repository.ShopItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    private final ShopItemDAO shopItemDAO;

    public ShopService(ShopItemDAO shopItemDAO) {
        this.shopItemDAO = shopItemDAO;
    }

    public List<ShopItem> getAllItems() {
        // Agora usando SQL puro (JDBC) via DAO conforme requisito do professor
        return shopItemDAO.listarTodosProdutos();
    }

    public List<ShopItem> getFilteredItems(String query, String category) {
        return shopItemDAO.buscarProdutos(query, category);
    }
}
