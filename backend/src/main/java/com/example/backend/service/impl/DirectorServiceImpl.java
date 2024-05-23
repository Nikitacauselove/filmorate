package com.example.backend.service.impl;

import com.example.api.dto.Director;
import com.example.backend.dao.DirectorDao;
import com.example.backend.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DirectorServiceImpl implements DirectorService {

    private final DirectorDao directorDao;

    @Override
    @Transactional
    public Director create(Director director) {
        if (director.isValid()) {
            directorDao.create(director);
        }
        return findById(director.getId());
    }

    @Override
    @Transactional
    public Director update(Director director) {
        if (director.isValid()) {
            directorDao.update(director);
        }
        return findById(director.getId());
    }

    @Override
    public Director findById(int id) {
        return directorDao.findById(id);
    }

    @Override
    public List<Director> findAll() {
        return directorDao.findAll();
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        directorDao.deleteById(id);
    }
}
