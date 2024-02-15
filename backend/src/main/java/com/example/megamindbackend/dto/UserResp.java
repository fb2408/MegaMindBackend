package com.example.megamindbackend.dto;

import lombok.Data;

@Data
public class UserResp {
    Integer id;
    String mail;
    String userName;
    Integer level;
    Integer userXp;
    Integer credits;

}
