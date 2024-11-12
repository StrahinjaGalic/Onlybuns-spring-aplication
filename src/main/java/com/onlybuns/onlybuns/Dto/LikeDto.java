package com.onlybuns.onlybuns.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class LikeDto {
    private Long id;
    private Long postId;
    private String username;
}
