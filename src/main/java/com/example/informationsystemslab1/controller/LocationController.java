package com.example.informationsystemslab1.controller;

import com.example.informationsystemslab1.dto.CreateLocationDto;
import com.example.informationsystemslab1.entity.Location;
import com.example.informationsystemslab1.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<Location> create(@Valid @RequestBody CreateLocationDto dto) {
        Location created = locationService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
