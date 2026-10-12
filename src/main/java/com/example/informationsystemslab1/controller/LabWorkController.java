package com.example.informationsystemslab1.controller;

import com.example.informationsystemslab1.dto.request.create.CreateLabWorkDto;
import com.example.informationsystemslab1.dto.request.update.UpdateLabWorkDto;
import com.example.informationsystemslab1.dto.response.LabWorkResponseDto;
import com.example.informationsystemslab1.service.LabWorkService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/labworks")
public class LabWorkController {

    private final LabWorkService labWorkService;

    public LabWorkController(LabWorkService labWorkService) {
        this.labWorkService = labWorkService;
    }

    @PostMapping
    public ResponseEntity<LabWorkResponseDto> create(@Valid @RequestBody CreateLabWorkDto dto) {
        LabWorkResponseDto created = labWorkService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<LabWorkResponseDto>> getAllLabWorks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            Pageable pageable
    ) {
        return ResponseEntity.ok(labWorkService.findAll(name, description, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabWorkResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(labWorkService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabWorkResponseDto> update(@PathVariable Long id, @Valid @RequestBody UpdateLabWorkDto dto) {
        LabWorkResponseDto updated = labWorkService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        labWorkService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
