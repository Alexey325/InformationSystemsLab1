package com.example.informationsystemslab1.repository;

import com.example.informationsystemslab1.entity.LabWork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabWorkRepository extends JpaRepository<LabWork, Long> {

    // authors не фетчится из-за увеличения строк результата JOIN

    @Query(
            value = "SELECT l FROM LabWork l LEFT JOIN FETCH l.discipline " +
                    "WHERE (:name IS NULL OR l.name = :name) " +
                    "AND (:description IS NULL OR l.description = :description)",
            countQuery = "SELECT COUNT(l) FROM LabWork l " +
                    "WHERE (:name IS NULL OR l.name = :name) " +
                    "AND (:description IS NULL OR l.description = :description)"
    )
    Page<LabWork> findAllFiltered(@Param("name") String name, @Param("description") String description, Pageable pageable);

    Optional<LabWork> findFirstByOrderByTunedInWorkDesc();
}
