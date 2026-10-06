package com.example.informationsystemslab1.controller;

import com.example.informationsystemslab1.dto.CreatePersonDto;
import com.example.informationsystemslab1.entity.Person;
import com.example.informationsystemslab1.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Person> create(@Valid @RequestBody CreatePersonDto dto) {
        Person created = personService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
