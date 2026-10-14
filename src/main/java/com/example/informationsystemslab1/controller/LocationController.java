package com.example.informationsystemslab1.controller;

import com.example.informationsystemslab1.access.BlockDirectAccess;
import com.example.informationsystemslab1.dto.request.create.CreateLocationDto;
import com.example.informationsystemslab1.dto.request.update.UpdateLocationDto;
import com.example.informationsystemslab1.dto.response.LocationResponseDto;
import com.example.informationsystemslab1.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<LocationResponseDto> create(@Valid @RequestBody CreateLocationDto dto) {
        LocationResponseDto created = locationService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<LocationResponseDto>> getAllLocations() {
        return ResponseEntity.ok(locationService.findAll());
    }

    @BlockDirectAccess
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDto> update(@PathVariable Long id, @Valid @RequestBody UpdateLocationDto dto) {
        LocationResponseDto updated = locationService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
