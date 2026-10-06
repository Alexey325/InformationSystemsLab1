package com.example.informationsystemslab1.service;

import com.example.informationsystemslab1.dto.CreateLabWorkDto;
import com.example.informationsystemslab1.dto.UpdateLabWorkDto;
import com.example.informationsystemslab1.entity.Coordinates;
import com.example.informationsystemslab1.entity.Discipline;
import com.example.informationsystemslab1.entity.LabWork;
import com.example.informationsystemslab1.repository.LabWorkRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LabWorkService {

    private final LabWorkRepository labWorkRepository;
    private final DisciplineService disciplineService;
    private final PersonService personService;

    public LabWorkService(
            LabWorkRepository labWorkRepository,
            DisciplineService disciplineService,
            PersonService personService
    ) {
        this.labWorkRepository = labWorkRepository;
        this.disciplineService = disciplineService;
        this.personService = personService;
    }

    @Transactional
    public LabWork create(CreateLabWorkDto dto) {
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

        Discipline discipline = disciplineService.findById(dto.disciplineId());
        discipline.setLabsCount(discipline.getLabsCount() + 1);

        labWork.setDiscipline(discipline);
        labWork.setAuthors(personService.findAllByIds(dto.authorsIds()));
        labWork.setCreationDate(LocalDate.now());

        return labWorkRepository.save(labWork);
    }

    public List<LabWork> findAll() {
        return labWorkRepository.findAll();
    }

    public LabWork findById(Long id) {
        Optional<LabWork> optionalLabWork = labWorkRepository.findById(id);

        if (optionalLabWork.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return optionalLabWork.get();

    }

    @Transactional
    public LabWork update(Long labWorkId, UpdateLabWorkDto dto) {
        LabWork labWork = findById(labWorkId);

        labWork.getCoordinates().setX(dto.coordinates().x());
        labWork.getCoordinates().setY(dto.coordinates().y());

        labWork.setName(dto.name());
        labWork.setDescription(dto.description());
        labWork.setDifficulty(dto.difficulty());
        labWork.setMinimalPoint(dto.minimalPoint());
        labWork.setPersonalQualitiesMaximum(dto.personalQualitiesMaximum());
        labWork.setTunedInWork(dto.tunedInWork());

        Discipline oldDiscipline = labWork.getDiscipline();
        Discipline newDiscipline = disciplineService.findById(dto.disciplineId());

        if (!oldDiscipline.getId().equals(newDiscipline.getId())) {
            oldDiscipline.setLabsCount(oldDiscipline.getLabsCount() - 1);
            newDiscipline.setLabsCount(newDiscipline.getLabsCount() + 1);
        }

        labWork.setDiscipline(newDiscipline);
        labWork.setAuthors(personService.findAllByIds(dto.authorsIds()));

        return labWorkRepository.save(labWork);
    }

    @Transactional
    public void delete(Long id) {
        LabWork labWork = findById(id);
        Discipline discipline = labWork.getDiscipline();
        discipline.setLabsCount(discipline.getLabsCount() - 1);

        labWorkRepository.delete(labWork);
    }

}
