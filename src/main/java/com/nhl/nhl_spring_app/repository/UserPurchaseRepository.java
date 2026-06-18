package com.nhl.nhl_spring_app.repository;

import com.nhl.nhl_spring_app.model.UserPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPurchaseRepository extends JpaRepository<UserPurchase, Long> {
    List<UserPurchase> findByUserIdOrderByPurchaseDateDesc(Long userId);
}
