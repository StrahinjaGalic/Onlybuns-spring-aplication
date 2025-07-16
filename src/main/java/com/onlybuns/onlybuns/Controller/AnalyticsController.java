package com.onlybuns.onlybuns.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.Dto.AnalyticsDto;
import com.onlybuns.onlybuns.Service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    
    @Autowired
    public AnalyticsService analyticsService;

    @GetMapping("/get")
    public ResponseEntity<AnalyticsDto> getAnalytics() 
    {
        AnalyticsDto analytics = analyticsService.getAnalytics();
        return ResponseEntity.ok(analytics);
    }
}
