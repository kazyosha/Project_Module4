package com.codegym.demo.repositories;

import com.codegym.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

