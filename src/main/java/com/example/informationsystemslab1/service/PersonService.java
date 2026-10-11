package com.example.informationsystemslab1.service;

import com.example.informationsystemslab1.dto.request.CreatePersonDto;
import com.example.informationsystemslab1.dto.request.UpdatePersonDto;
import com.example.informationsystemslab1.dto.response.PersonResponseDto;
import com.example.informationsystemslab1.entity.Location;
import com.example.informationsystemslab1.entity.Person;
import com.example.informationsystemslab1.mapper.PersonMapper;
import com.example.informationsystemslab1.repository.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final LocationService locationService;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository, LocationService locationService, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.locationService = locationService;
        this.personMapper = personMapper;
    }

    public PersonResponseDto create(CreatePersonDto dto) {
        Location location = locationService.getEntityById(dto.locationId());

        Person person = new Person();
        person.setName(dto.name());
        person.setEyeColor(dto.eyeColor());
        person.setHairColor(dto.hairColor());
        person.setLocation(location);
        person.setPassportID(dto.passportID());
        person.setNationality(dto.nationality());

        return personMapper.toDto(personRepository.save(person));
    }

    public List<PersonResponseDto> findAll() {
        return personRepository.findAll().stream()
                .map(personMapper::toDto)
                .toList();
    }

    public PersonResponseDto findById(Long id) {
        return personMapper.toDto(getEntityById(id));
    }

    private Person getEntityById(Long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person with id " + id + " not found");
        }

        return optionalPerson.get();
    }

    public PersonResponseDto update(Long id, UpdatePersonDto dto) {
        Person person = getEntityById(id);
        Location location = locationService.getEntityById(dto.locationId());

        person.setName(dto.name());
        person.setEyeColor(dto.eyeColor());
        person.setHairColor(dto.hairColor());
        person.setLocation(location);
        person.setPassportID(dto.passportID());
        person.setNationality(dto.nationality());

        return personMapper.toDto(personRepository.save(person));
    }

    public void delete(Long id) {
        if (!personRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person with id " + id + " not found");
        }

        personRepository.deleteById(id);
    }

    public Set<Person> findAllByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }

        List<Person> foundPeople = personRepository.findAllById(ids);

        if (foundPeople.size() != ids.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some authors were not found");
        }

        return new HashSet<>(foundPeople);
    }
}
