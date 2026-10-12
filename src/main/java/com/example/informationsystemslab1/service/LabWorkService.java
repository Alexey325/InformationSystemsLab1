package com.example.informationsystemslab1.service;

import com.example.informationsystemslab1.dto.request.create.CreateLabWorkDto;
import com.example.informationsystemslab1.dto.request.update.UpdateLabWorkDto;
import com.example.informationsystemslab1.dto.response.LabWorkResponseDto;
import com.example.informationsystemslab1.dto.response.operations.NameCountDto;
import com.example.informationsystemslab1.entity.Coordinates;
import com.example.informationsystemslab1.entity.Discipline;
import com.example.informationsystemslab1.entity.LabWork;
import com.example.informationsystemslab1.exception.custom.EntityNotFoundException;
import com.example.informationsystemslab1.mapper.LabWorkMapper;
import com.example.informationsystemslab1.repository.LabWorkRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class LabWorkService {

    private static final int HARDEST_COUNT = 3;

    private final LabWorkRepository labWorkRepository;
    private final DisciplineService disciplineService;
    private final PersonService personService;
    private final LabWorkMapper labWorkMapper;

    public LabWorkService(
            LabWorkRepository labWorkRepository,
            DisciplineService disciplineService,
            PersonService personService,
            LabWorkMapper labWorkMapper
    ) {
        this.labWorkRepository = labWorkRepository;
        this.disciplineService = disciplineService;
        this.personService = personService;
        this.labWorkMapper = labWorkMapper;
    }

    @Transactional
    public LabWorkResponseDto create(CreateLabWorkDto dto) {
        Coordinates coordinates = new Coordinates();
        coordinates.setX(dto.coordinates().x());
        coordinates.setY(dto.coordinates().y());

        LabWork labWork = new LabWork();
        labWork.setName(dto.name());
        labWork.setCoordinates(coordinates);
        labWork.setDescription(dto.description());
        labWork.setDifficulty(dto.difficulty());
        labWork.setMinimalPoint(dto.minimalPoint());
        labWork.setPersonalQualitiesMaximum(dto.personalQualitiesMaximum());
        labWork.setTunedInWork(dto.tunedInWork());

        Discipline discipline = disciplineService.getEntityById(dto.disciplineId());
        discipline.setLabsCount(discipline.getLabsCount() + 1);

        labWork.setDiscipline(discipline);
        labWork.setAuthors(personService.findAllByIds(dto.authorsIds()));
        labWork.setCreationDate(LocalDate.now());

        return labWorkMapper.toDto(labWorkRepository.saveAndFlush(labWork));
    }

    public Page<LabWorkResponseDto> findAll(String name, String description, Pageable pageable) {
        return labWorkRepository.findAllFiltered(name, description, pageable)
                .map(labWorkMapper::toDto);
    }

    public LabWorkResponseDto findById(Long id) {
        return labWorkMapper.toDto(getEntityById(id));
    }

    private LabWork getEntityById(Long id) {
        Optional<LabWork> optionalLabWork = labWorkRepository.findById(id);

        if (optionalLabWork.isEmpty()) {
            throw new EntityNotFoundException(LabWork.class, id);
        }

        return optionalLabWork.get();
    }

    @Transactional
    public LabWorkResponseDto update(Long labWorkId, UpdateLabWorkDto dto) {
        LabWork labWork = getEntityById(labWorkId);

        labWork.getCoordinates().setX(dto.coordinates().x());
        labWork.getCoordinates().setY(dto.coordinates().y());

        labWork.setName(dto.name());
        labWork.setDescription(dto.description());
        labWork.setDifficulty(dto.difficulty());
        labWork.setMinimalPoint(dto.minimalPoint());
        labWork.setPersonalQualitiesMaximum(dto.personalQualitiesMaximum());
        labWork.setTunedInWork(dto.tunedInWork());

        Discipline oldDiscipline = labWork.getDiscipline();
        Discipline newDiscipline = disciplineService.getEntityById(dto.disciplineId());

        if (!oldDiscipline.getId().equals(newDiscipline.getId())) {
            oldDiscipline.setLabsCount(oldDiscipline.getLabsCount() - 1);
            newDiscipline.setLabsCount(newDiscipline.getLabsCount() + 1);
        }

        labWork.setDiscipline(newDiscipline);
        labWork.setAuthors(personService.findAllByIds(dto.authorsIds()));

        return labWorkMapper.toDto(labWorkRepository.save(labWork));
    }

    @Transactional
    public void delete(Long id) {
        LabWork labWork = getEntityById(id);
        Discipline discipline = labWork.getDiscipline();
        discipline.setLabsCount(discipline.getLabsCount() - 1);

        labWorkRepository.delete(labWork);
    }


    //обработка специальных операций

    public LabWorkResponseDto findMaxedByTunedInWork() {
        Optional<LabWork> optionalMaxedByTunedInWork = labWorkRepository.findFirstByOrderByTunedInWorkDesc();

        if (optionalMaxedByTunedInWork.isEmpty()) {
            throw new EntityNotFoundException(LabWork.class);
        }

        return labWorkMapper.toDto(optionalMaxedByTunedInWork.get());
    }

    public List<NameCountDto> countGroupedByName() {
        return labWorkRepository.countGroupedByNameRaw().stream()
                .map(row -> new NameCountDto((String) row[0], (Long) row[1]))
                .toList();
    }

    public List<LabWorkResponseDto> findByMinimalPointGreaterThan(int minimalPoint) {
        return labWorkRepository.findByMinimalPointGreaterThan(minimalPoint).stream()
                .map(labWorkMapper::toDto)
                .toList();
    }

    @Transactional
    public List<LabWorkResponseDto> addHardestToDiscipline(Long disciplineId) {
        Discipline targetDiscipline = disciplineService.getEntityById(disciplineId);

        List<LabWork> hardest = labWorkRepository.findAll().stream()
                .filter(l -> l.getDifficulty() != null)
                .filter(l -> !l.getDiscipline().getId().equals(disciplineId))
                .sorted(Comparator.comparing((LabWork l) -> l.getDifficulty().getWeight()).reversed())
                .limit(HARDEST_COUNT)
                .toList();

        for (LabWork labWork : hardest) {
            Discipline oldDiscipline = labWork.getDiscipline();
            oldDiscipline.setLabsCount(oldDiscipline.getLabsCount() - 1);
            targetDiscipline.setLabsCount(targetDiscipline.getLabsCount() + 1);
            labWork.setDiscipline(targetDiscipline);
        }

        return labWorkRepository.saveAll(hardest).stream()
                .map(labWorkMapper::toDto)
                .toList();
    }
}
