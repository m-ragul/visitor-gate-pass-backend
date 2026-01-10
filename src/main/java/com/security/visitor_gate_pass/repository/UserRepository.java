package com.security.visitor_gate_pass.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.visitor_gate_pass.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
