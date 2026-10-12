package com.example.informationsystemslab1.repository;

import com.example.informationsystemslab1.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Override
    @Query("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.labWorks LEFT JOIN FETCH p.location")
    List<Person> findAll();

    // метод нужен для решения проблемы с удалением сущности, когда на нее присутствует внешняя ссылка not null,
    // вызывается LocationService, что не хорошо, но решает проблему циклического DI между LocationService и LocationService
    boolean existsByLocationId(Long locationId);
}
