package com.example.informationsystemslab1.controller;

import com.example.informationsystemslab1.dto.CreateDisciplineDto;
import com.example.informationsystemslab1.entity.Discipline;
import com.example.informationsystemslab1.service.DisciplineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/disciplines")
public class DisciplineController {

    private final DisciplineService disciplineService;

    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    @PostMapping
    public ResponseEntity<Discipline> create(@Valid @RequestBody CreateDisciplineDto dto) {
        Discipline created = disciplineService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
