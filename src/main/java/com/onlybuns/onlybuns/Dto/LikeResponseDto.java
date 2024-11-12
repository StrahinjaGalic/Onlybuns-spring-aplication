package com.onlybuns.onlybuns.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class LikeResponseDto {
    private int likeCount;
    private boolean userLiked;
    private Long userLikeId;
}
