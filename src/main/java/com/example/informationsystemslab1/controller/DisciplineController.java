package com.example.informationsystemslab1.controller;

import com.example.informationsystemslab1.dto.request.create.CreateDisciplineDto;
import com.example.informationsystemslab1.dto.request.update.UpdateDisciplineDto;
import com.example.informationsystemslab1.dto.response.DisciplineResponseDto;
import com.example.informationsystemslab1.service.DisciplineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplines")
public class DisciplineController {

    private final DisciplineService disciplineService;

    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    @PostMapping
    public ResponseEntity<DisciplineResponseDto> create(@Valid @RequestBody CreateDisciplineDto dto) {
        DisciplineResponseDto created = disciplineService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<DisciplineResponseDto>> getAllDisciplines() {
        return ResponseEntity.ok(disciplineService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplineResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(disciplineService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplineResponseDto> update(@PathVariable Long id, @Valid @RequestBody UpdateDisciplineDto dto) {
        DisciplineResponseDto updated = disciplineService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        disciplineService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
