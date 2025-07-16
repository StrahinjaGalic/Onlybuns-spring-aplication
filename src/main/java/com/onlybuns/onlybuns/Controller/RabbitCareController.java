package com.onlybuns.onlybuns.Controller;

import com.onlybuns.onlybuns.Model.RabbitCare;
import com.onlybuns.onlybuns.Service.RabbitCareService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rabbitCare")
public class RabbitCareController {

    private final RabbitCareService service;

    public RabbitCareController(RabbitCareService service) {
        this.service = service;
    }

    @GetMapping("/locations")
    public List<RabbitCare> getRabbitCareLocations() {
        return service.getAllRabbitCareLocations();
    }
}

