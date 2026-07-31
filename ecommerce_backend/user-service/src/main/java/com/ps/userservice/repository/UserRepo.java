package com.ps.userservice.repository;

import com.ps.userservice.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users,Long> {
    Optional<Users> findByEmail(String email);
    Boolean existsByEmailAndUserIdNot(String email,Long id);
    Optional<Users> findByActivationToken(String activationToken);
}
