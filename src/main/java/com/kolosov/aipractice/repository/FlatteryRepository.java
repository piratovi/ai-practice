package com.kolosov.aipractice.repository;

import com.kolosov.aipractice.dto.Flattery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlatteryRepository extends JpaRepository<Flattery, Long> {

    default List<Flattery> findLastFiveFlatters() {
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        return findAll(pageRequest).toList();
    }
}