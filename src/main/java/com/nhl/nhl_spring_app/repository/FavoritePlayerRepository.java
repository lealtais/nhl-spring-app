package com.nhl.nhl_spring_app.repository;

import com.nhl.nhl_spring_app.model.FavoritePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritePlayerRepository extends JpaRepository<FavoritePlayer, Long> {
    List<FavoritePlayer> findByUserId(Long userId);
    Optional<FavoritePlayer> findByUserIdAndPlayerId(Long userId, Long playerId);
    void deleteByIdAndUserId(Long id, Long userId);
}
