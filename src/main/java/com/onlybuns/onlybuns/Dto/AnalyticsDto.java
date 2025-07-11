package com.onlybuns.onlybuns.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class AnalyticsDto {
    private Long WeeklyPosts;
    private Long MonthlyPosts;
    private Long YearlyPosts;
    private Long WeeklyComments;
    private Long MonthlyComments;
    private Long YearlyComments;
    private Double percentWithPosts;
    private Double percentWithOnlyComments;
    private Double percentWithoutPostsOrComments;
}
