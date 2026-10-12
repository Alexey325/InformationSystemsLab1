package com.example.informationsystemslab1.service;

import com.example.informationsystemslab1.dto.request.create.CreateDisciplineDto;
import com.example.informationsystemslab1.dto.request.update.UpdateDisciplineDto;
import com.example.informationsystemslab1.dto.response.DisciplineResponseDto;
import com.example.informationsystemslab1.entity.Discipline;
import com.example.informationsystemslab1.exception.custom.EntityInUseException;
import com.example.informationsystemslab1.exception.custom.EntityNotFoundException;
import com.example.informationsystemslab1.mapper.DisciplineMapper;
import com.example.informationsystemslab1.repository.DisciplineRepository;
import com.example.informationsystemslab1.repository.LabWorkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final LabWorkRepository labWorkRepository;
    private final DisciplineMapper disciplineMapper;

    public DisciplineService(DisciplineRepository disciplineRepository, LabWorkRepository labWorkRepository, DisciplineMapper disciplineMapper) {
        this.disciplineRepository = disciplineRepository;
        this.labWorkRepository = labWorkRepository;
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

        // discipline у LabWork @NotNull — удалить дисциплину, на которую ещё ссылаются LabWork, нельзя
        if (labWorkRepository.existsByDisciplineId(id)) {
            throw new EntityInUseException(Discipline.class, id);
        }

        disciplineRepository.deleteById(id);
    }
}
