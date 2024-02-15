package com.example.megamindbackend.dto;

import lombok.Data;

@Data
public class DeleteUserReq {
    private Integer userId;
    private Integer userToDeleteId;
}
