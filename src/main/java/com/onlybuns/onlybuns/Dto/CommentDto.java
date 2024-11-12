package com.onlybuns.onlybuns.Dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long postId;
    private String username;
    private String comment;
    private Date commentDate;
    private boolean edited;
    private boolean deleted;
}
