package com.onlybuns.onlybuns.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onlybuns.onlybuns.Metrics.PostMetricsFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class WebConfig {

    private final PostMetricsFilter postMetricsFilter;

    public WebConfig(PostMetricsFilter postMetricsFilter) {
        this.postMetricsFilter = postMetricsFilter;
    }

    @Bean
    public FilterRegistrationBean<PostMetricsFilter> postMetricsFilterRegistration() {
        FilterRegistrationBean<PostMetricsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(postMetricsFilter); // Ensure this is a valid Filter
        registration.addUrlPatterns("/api/posts/create"); // Adjust the path as needed
        registration.setName("postMetricsFilter");
        registration.setOrder(1); // Filter order
        return registration;
    }
}
