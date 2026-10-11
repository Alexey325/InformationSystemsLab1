package com.example.informationsystemslab1.repository;

import com.example.informationsystemslab1.entity.LabWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabWorkRepository extends JpaRepository<LabWork, Long> {

    @Override
    @Query("SELECT DISTINCT l FROM LabWork l LEFT JOIN FETCH l.authors LEFT JOIN FETCH l.discipline")
    List<LabWork> findAll();
}
