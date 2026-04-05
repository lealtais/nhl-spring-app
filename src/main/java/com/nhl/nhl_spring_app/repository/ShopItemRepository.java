package com.nhl.nhl_spring_app.repository;

import com.nhl.nhl_spring_app.model.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopItemRepository extends JpaRepository<ShopItem, Long> {
}
