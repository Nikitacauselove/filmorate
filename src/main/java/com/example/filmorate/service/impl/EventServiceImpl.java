package com.example.filmorate.service.impl;

import com.example.filmorate.dao.EventDao;
import com.example.filmorate.dao.UserDao;
import com.example.filmorate.entity.Event;
import com.example.filmorate.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventDao eventDao;
    private final UserDao userDao;

    @Override
    public List<Event> findAllByUserId(int id) {
        if (userDao.existsById(id)) {
            return eventDao.findAllByUserId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с указанным идентификатором не найден.");
        }
    }
}
