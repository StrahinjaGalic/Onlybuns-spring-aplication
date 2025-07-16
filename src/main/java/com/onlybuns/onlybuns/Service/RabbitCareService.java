package com.onlybuns.onlybuns.Service;

import com.onlybuns.onlybuns.Model.RabbitCare;
import com.onlybuns.onlybuns.Repository.RabbitCareRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitCareService {

    private final RabbitCareRepository repository;

    public RabbitCareService(RabbitCareRepository repository) {
        this.repository = repository;
    }

    public RabbitCare saveRabbitCare(RabbitCare rabbitCare) {
        return repository.save(rabbitCare);
    }

    public List<RabbitCare> getAllRabbitCareLocations() {
        return repository.findAll();
    }
}
