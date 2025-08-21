package org.example.pms_uc_j2ee.repository;


import org.example.pms_uc_j2ee.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Product entity.
 * This interface provides all standard CRUD (Create, Read, Update, Delete)
 * operations for Product objects out of the box.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // No method implementations are needed here.
    // Spring Data JPA provides them automatically.
    List<Product> findByAdmin_AdminId(Integer adminId);
}
