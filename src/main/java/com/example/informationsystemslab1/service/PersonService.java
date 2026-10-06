package com.example.informationsystemslab1.service;

import com.example.informationsystemslab1.dto.CreatePersonDto;
import com.example.informationsystemslab1.entity.Location;
import com.example.informationsystemslab1.entity.Person;
import com.example.informationsystemslab1.repository.LocationRepository;
import com.example.informationsystemslab1.repository.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final LocationRepository locationRepository;

    public PersonService(PersonRepository personRepository, LocationRepository locationRepository) {
        this.personRepository = personRepository;
        this.locationRepository = locationRepository;
    }

    public Person create(CreatePersonDto dto) {
        Location location = locationRepository.findById(dto.locationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));

        Person person = new Person();
        person.setName(dto.name());
        person.setEyeColor(dto.eyeColor());
        person.setHairColor(dto.hairColor());
        person.setLocation(location);
        person.setPassportID(dto.passportID());
        person.setNationality(dto.nationality());

        return personRepository.save(person);
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
