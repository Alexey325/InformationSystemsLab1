package com.example.informationsystemslab1.controller;

import com.example.informationsystemslab1.dto.CreateLabWorkDto;
import com.example.informationsystemslab1.dto.UpdateLabWorkDto;
import com.example.informationsystemslab1.entity.LabWork;
import com.example.informationsystemslab1.service.LabWorkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labworks")
public class LabWorkController {

    private final LabWorkService labWorkService;

    public LabWorkController(LabWorkService labWorkService) {
        this.labWorkService = labWorkService;
    }

    @PostMapping
    public ResponseEntity<LabWork> create(@Valid @RequestBody CreateLabWorkDto dto) {
        LabWork createdLabWork = labWorkService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLabWork);
    }

    @GetMapping
    public ResponseEntity<List<LabWork>> getAllLabWorks() {
        List<LabWork> labWorks = labWorkService.findAll();
        return ResponseEntity.ok(labWorks);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LabWork> getById(@PathVariable Long id) {
        LabWork labWork = labWorkService.findById(id);
        return ResponseEntity.ok(labWork);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabWork> update(@PathVariable Long id, @Valid @RequestBody UpdateLabWorkDto dto) {
        LabWork updatedLabWork =  this.labWorkService.update(id, dto);
        return ResponseEntity.ok(updatedLabWork);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        labWorkService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
