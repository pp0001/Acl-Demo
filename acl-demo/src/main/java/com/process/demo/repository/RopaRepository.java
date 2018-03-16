package com.process.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.process.demo.model.Ropa;

public interface RopaRepository extends JpaRepository<Ropa, Long> {

	Ropa findByName(String name);

}
