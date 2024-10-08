package com.example.application.repository;

import com.example.application.repository.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByUserId(Long userId);
}
