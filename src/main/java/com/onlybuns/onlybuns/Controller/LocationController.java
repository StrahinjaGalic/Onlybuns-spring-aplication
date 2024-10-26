package com.onlybuns.onlybuns.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import com.onlybuns.onlybuns.Repository.LocationRepository;
import com.onlybuns.onlybuns.Model.Location;


@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private LocationRepository locationRepository;

    public LocationController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @GetMapping("/{id}")
    public Location getLocation(@PathVariable long id) {
        return locationRepository.findById(id);
    }

    @PostMapping("/create")
    public Location createLocation(@RequestBody Location location) {
        return locationRepository.save(location);
    }
    
}

