package com.nhl.nhl_spring_app.repository;

import com.nhl.nhl_spring_app.model.FavoriteGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteGoalRepository extends JpaRepository<FavoriteGoal, Long> {
    List<FavoriteGoal> findByUserId(Long userId);
    Optional<FavoriteGoal> findByUserIdAndGameIdAndEventId(Long userId, Long gameId, Integer eventId);
    void deleteByIdAndUserId(Long id, Long userId);
}
