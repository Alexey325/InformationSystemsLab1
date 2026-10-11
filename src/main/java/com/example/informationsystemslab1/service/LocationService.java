package com.example.informationsystemslab1.service;

import com.example.informationsystemslab1.dto.request.CreateLocationDto;
import com.example.informationsystemslab1.dto.request.UpdateLocationDto;
import com.example.informationsystemslab1.dto.response.LocationResponseDto;
import com.example.informationsystemslab1.entity.Location;
import com.example.informationsystemslab1.mapper.LocationMapper;
import com.example.informationsystemslab1.repository.LocationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public LocationService(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    public LocationResponseDto create(CreateLocationDto dto) {
        Location location = new Location();
        location.setX(dto.x());
        location.setY(dto.y());
        location.setZ(dto.z());
        location.setName(dto.name());

        return locationMapper.toDto(locationRepository.save(location));
    }

    public List<LocationResponseDto> findAll() {
        return locationRepository.findAll().stream()
                .map(locationMapper::toDto)
                .toList();
    }

    public LocationResponseDto findById(Long id) {
        return locationMapper.toDto(getEntityById(id));
    }

    public Location getEntityById(Long id) {
        Optional<Location> optionalLocation = locationRepository.findById(id);

        if (optionalLocation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with id " + id + " not found");
        }

        return optionalLocation.get();
    }

    public LocationResponseDto update(Long id, UpdateLocationDto dto) {
        Location location = getEntityById(id);

        location.setX(dto.x());
        location.setY(dto.y());
        location.setZ(dto.z());
        location.setName(dto.name());

        return locationMapper.toDto(locationRepository.save(location));
    }

    public void delete(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location with id " + id + " not found");
        }

        locationRepository.deleteById(id);
    }
}
