package com.example.informationsystemslab1.service;

import com.example.informationsystemslab1.dto.request.CreateLocationDto;
import com.example.informationsystemslab1.entity.Location;
import com.example.informationsystemslab1.repository.LocationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

    public Location getEntityById(Long id) {
        Optional<Location> optionalLocation = locationRepository.findById(id);

        if (optionalLocation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with id " + id + " not found");
        }

        return optionalLocation.get();
    }
}
