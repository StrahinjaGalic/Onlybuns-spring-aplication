package com.onlybuns.onlybuns.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class RemoveUserPayloadDto {
    private Long chatId;
    private String usernameToRemove;
    private String adminUsername;
}