package org.springdemobot.repository;

import org.springdemobot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    void deleteByUserName(String username);
    Optional<User> findByUserName(String username);
}