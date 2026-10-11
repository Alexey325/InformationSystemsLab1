package com.example.informationsystemslab1.repository;

import com.example.informationsystemslab1.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Override
    @Query("SELECT DISTINCT l FROM Location l LEFT JOIN FETCH l.people")
    List<Location> findAll();
}
