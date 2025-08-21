package org.example.pms_uc_j2ee.repository;


import org.example.pms_uc_j2ee.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {

    /**
     * Finds a user by their email address.
     * Used for the login process.
     * @param email The email to search for.
     * @return An Optional containing the user if found.
     */
    Optional<AppUser> findByEmail(String email);
}

