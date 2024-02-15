package com.example.megamindbackend.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserItemRepository extends JpaRepository<UserItemEntity, Integer> {
}