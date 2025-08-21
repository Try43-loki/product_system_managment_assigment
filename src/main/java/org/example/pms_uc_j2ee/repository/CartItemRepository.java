package org.example.pms_uc_j2ee.repository;


import org.example.pms_uc_j2ee.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    // Finds all cart items for a specific user
    List<CartItem> findByUser_UserId(Integer userId);

    // Finds a specific product in a user's cart
    CartItem findByUser_UserIdAndProduct_ProductId(Integer userId, Integer productId);
}
