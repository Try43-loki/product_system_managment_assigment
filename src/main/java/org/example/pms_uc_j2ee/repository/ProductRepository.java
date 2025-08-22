package org.example.pms_uc_j2ee.repository;


import org.example.pms_uc_j2ee.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByAdmin_AdminId(Integer adminId);
}
