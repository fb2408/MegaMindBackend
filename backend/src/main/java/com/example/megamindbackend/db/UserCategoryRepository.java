package com.example.megamindbackend.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCategoryRepository extends JpaRepository<UserCategoryEntity, Integer> {
    Optional<UserCategoryEntity> findByUser_IdAndCategory_Id(Integer id, Integer id1);

    List<UserCategoryEntity> findByUser_IdAndFavouriteCategoryTrue(Integer id);

    List<UserCategoryEntity> findByUser_Id(Integer id);
}