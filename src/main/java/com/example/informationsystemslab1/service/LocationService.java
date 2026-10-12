package com.example.informationsystemslab1.service;

import com.example.informationsystemslab1.dto.request.create.CreateLocationDto;
import com.example.informationsystemslab1.dto.request.update.UpdateLocationDto;
import com.example.informationsystemslab1.dto.response.LocationResponseDto;
import com.example.informationsystemslab1.entity.Location;
import com.example.informationsystemslab1.exception.custom.EntityInUseException;
import com.example.informationsystemslab1.exception.custom.EntityNotFoundException;
import com.example.informationsystemslab1.mapper.LocationMapper;
import com.example.informationsystemslab1.repository.LocationRepository;
import com.example.informationsystemslab1.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final PersonRepository personRepository;
    private final LocationMapper locationMapper;

    public LocationService(LocationRepository locationRepository, PersonRepository personRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.personRepository = personRepository;
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
            throw new EntityNotFoundException(Location.class, id);
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
            throw new EntityNotFoundException(Location.class, id);
        }

        // location у Person @NotNull — удалить локацию, на которую ещё ссылаются Person, нельзя
        if (personRepository.existsByLocationId(id)) {
            throw new EntityInUseException(Location.class, id);
        }

        locationRepository.deleteById(id);
    }
}
