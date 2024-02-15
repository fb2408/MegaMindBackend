package com.example.megamindbackend.dto;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class UserReq {

    Optional<String> userName;
    Optional<String> firstname;
    Optional<String> lastname;
    Optional<String> password;
    Optional<List<Integer>> favouriteCategoryIds;

}
