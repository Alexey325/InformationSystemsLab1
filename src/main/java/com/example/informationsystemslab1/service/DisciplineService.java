package com.example.informationsystemslab1.service;

import com.example.informationsystemslab1.dto.CreateDisciplineDto;
import com.example.informationsystemslab1.entity.Discipline;
import com.example.informationsystemslab1.repository.DisciplineRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public DisciplineService(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    public Discipline create(CreateDisciplineDto dto) {
        Discipline discipline = new Discipline();
        discipline.setName(dto.name());
        discipline.setLabsCount(0);

        return disciplineRepository.save(discipline);
    }

    public Discipline findById(Long id) {
        Optional<Discipline> optionalDiscipline = disciplineRepository.findById(id);

        if (optionalDiscipline.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline with id " + id + " not found");
        }

        return optionalDiscipline.get();
    }
}
