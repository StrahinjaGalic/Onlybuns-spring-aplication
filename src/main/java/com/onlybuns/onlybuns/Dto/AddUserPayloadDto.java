package com.onlybuns.onlybuns.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class AddUserPayloadDto {
    private Long chatId;
    private String usernameToAdd;
    private String adminUsername;
}