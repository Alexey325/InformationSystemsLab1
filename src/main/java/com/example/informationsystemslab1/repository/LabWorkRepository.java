package com.example.informationsystemslab1.repository;

import com.example.informationsystemslab1.entity.LabWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabWorkRepository extends JpaRepository<LabWork, Long> {
}
