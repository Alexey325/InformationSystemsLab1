package com.example.informationsystemslab1.service;

import com.example.informationsystemslab1.dto.CreateLocationDto;
import com.example.informationsystemslab1.entity.Location;
import com.example.informationsystemslab1.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location create(CreateLocationDto dto) {
        Location location = new Location();
        location.setX(dto.x());
        location.setY(dto.y());
        location.setZ(dto.z());
        location.setName(dto.name());

        return locationRepository.save(location);
    }
}
