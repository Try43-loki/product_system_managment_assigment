package org.example.pms_uc_j2ee.repository;

import org.example.pms_uc_j2ee.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUsername(String username);
    // That's it!
    // All standard CRUD methods like findAll(), findById(), save(), deleteById()
    // are automatically available.

    // You can also add custom query methods here if needed, for example:
    // Optional<Admin> findByUsername(String username);
}
