package com.process.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.process.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
