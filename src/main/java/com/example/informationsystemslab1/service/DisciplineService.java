package com.example.informationsystemslab1.service;

import com.example.informationsystemslab1.dto.request.create.CreateDisciplineDto;
import com.example.informationsystemslab1.dto.request.update.UpdateDisciplineDto;
import com.example.informationsystemslab1.dto.response.DisciplineResponseDto;
import com.example.informationsystemslab1.entity.Discipline;
import com.example.informationsystemslab1.exception.custom.EntityNotFoundException;
import com.example.informationsystemslab1.mapper.DisciplineMapper;
import com.example.informationsystemslab1.repository.DisciplineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final DisciplineMapper disciplineMapper;

    public DisciplineService(DisciplineRepository disciplineRepository, DisciplineMapper disciplineMapper) {
        this.disciplineRepository = disciplineRepository;
        this.disciplineMapper = disciplineMapper;
    }

    public DisciplineResponseDto create(CreateDisciplineDto dto) {
        Discipline discipline = new Discipline();
        discipline.setName(dto.name());
        discipline.setLabsCount(0);

        return disciplineMapper.toDto(disciplineRepository.save(discipline));
    }

    public List<DisciplineResponseDto> findAll() {
        return disciplineRepository.findAll().stream()
                .map(disciplineMapper::toDto)
                .toList();
    }

    public DisciplineResponseDto findById(Long id) {
        return disciplineMapper.toDto(getEntityById(id));
    }

    public Discipline getEntityById(Long id) {
        Optional<Discipline> optionalDiscipline = disciplineRepository.findById(id);

        if (optionalDiscipline.isEmpty()) {
            throw new EntityNotFoundException(Discipline.class, id);
        }

        return optionalDiscipline.get();
    }

    public DisciplineResponseDto update(Long id, UpdateDisciplineDto dto) {
        Discipline discipline = getEntityById(id);

        discipline.setName(dto.name());

        return disciplineMapper.toDto(disciplineRepository.save(discipline));
    }

    public void delete(Long id) {
        if (!disciplineRepository.existsById(id)) {
            throw new EntityNotFoundException(Discipline.class, id);
        }

        disciplineRepository.deleteById(id);
    }
}
