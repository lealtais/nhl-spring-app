package com.nhl.nhl_spring_app.repository;

import com.nhl.nhl_spring_app.model.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
    List<ShopItem> findByCategory(String category);
    List<ShopItem> findByNameContainingIgnoreCase(String query);
    List<ShopItem> findByCategoryAndNameContainingIgnoreCase(String category, String query);
    
    // Novas rotas da Expansão
    List<ShopItem> findByTeamAbbrev(String teamAbbrev);
    List<ShopItem> findByTeamAbbrevAndCategory(String teamAbbrev, String category);
    List<ShopItem> findByFeaturedTrue();
    
    // Busca global misturada
    List<ShopItem> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String desc);
}
