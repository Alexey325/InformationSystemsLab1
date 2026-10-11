package com.example.informationsystemslab1.repository;

import com.example.informationsystemslab1.entity.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {

    @Override
    @Query("SELECT DISTINCT d FROM Discipline d LEFT JOIN FETCH d.labWorks")
    List<Discipline> findAll();
}
