package com.multiteam.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    /*
    @Modifying
    @Query("UPDATE User u SET u.ownerId = :ownerId")
    void updateOwnerId(@Param("ownerId") UUID ownerId);
     */
    /*
    Page<User> findByOwnerIdAndActiveIsTrue(UUID ownerId, Pageable pageable);

    Optional<User> findByIdAndOwnerId(UUID userId, UUID ownerId);

    @Modifying
    @Query("UPDATE User u SET u.active = false WHERE u.id = :userId AND u.ownerId = :ownerId")
    void inactiveUser(@Param("userId") UUID userId, @Param("ownerId") UUID ownerId);
     */
}
