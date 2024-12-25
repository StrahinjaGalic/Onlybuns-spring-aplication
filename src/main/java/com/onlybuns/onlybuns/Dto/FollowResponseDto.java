package com.onlybuns.onlybuns.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class FollowResponseDto {
    private boolean isFollowing;
    private Long followId;
    
}
