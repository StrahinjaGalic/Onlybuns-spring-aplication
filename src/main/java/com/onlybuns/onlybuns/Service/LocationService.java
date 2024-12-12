package com.onlybuns.onlybuns.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Model.Location;
import com.onlybuns.onlybuns.Repository.LocationRepository;

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Cacheable(value = "locations", key = "#postId")
    public Location getLocationForPost(Long postId) {
        return locationRepository.findByPostId(postId); // This could be a custom query method to fetch location by postId
    }

    @CachePut(value = "locations", key = "#location.id") 
    public Location createLocation(Location location) {
        return locationRepository.save(location); 
    }
}
