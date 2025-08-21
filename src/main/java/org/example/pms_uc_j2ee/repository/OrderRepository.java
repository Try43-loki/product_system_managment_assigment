package org.example.pms_uc_j2ee.repository;
import org.example.pms_uc_j2ee.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser_UserIdOrderByOrderDateDesc(Integer userId);
}


