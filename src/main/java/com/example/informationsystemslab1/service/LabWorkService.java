package com.example.informationsystemslab1.service;

import com.example.informationsystemslab1.dto.CreateLabWorkDto;
import com.example.informationsystemslab1.dto.UpdateLabWorkDto;
import com.example.informationsystemslab1.entity.Coordinates;
import com.example.informationsystemslab1.entity.LabWork;
import com.example.informationsystemslab1.repository.LabWorkRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LabWorkService {

    private final LabWorkRepository labWorkRepository;

    public LabWorkService(LabWorkRepository labWorkRepository) {
        this.labWorkRepository = labWorkRepository;
    }

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
        labWork.setDiscipline_id(dto.disciplineId());
        labWork.setPerson_id(dto.personId());
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
        labWork.setDiscipline_id(dto.disciplineId());
        labWork.setPerson_id(dto.personId());

        return labWorkRepository.save(labWork);
    }


    public void delete(Long id) {
        if (!labWorkRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "LabWork with id " + id + " not found");
        }

        labWorkRepository.deleteById(id);
    }


}
