package com.katash.grocery.repository;

import com.katash.grocery.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUserUserIdAndProductProductId(int userId, int productId);

    List<Cart> findByUserUserId(int userId);

    void deleteByUserUserIdAndProductProductId(int userId, int productId);
}
