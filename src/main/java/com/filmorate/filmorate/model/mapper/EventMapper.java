package com.filmorate.filmorate.model.mapper;

import com.filmorate.filmorate.model.Event;
import com.filmorate.filmorate.model.dto.EventDto;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {
    public static EventDto toEventDto(Event event) {
        return new EventDto(event.getId(), event.getTimestamp(), event.getUserId(), event.getEventType(), event.getOperation(), event.getEntityId());
    }

    public static List<EventDto> toEventDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
    }
}
