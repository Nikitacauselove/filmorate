package com.example.application.service.impl;

import com.example.api.dto.FilmDto;
import com.example.api.dto.enums.EventOperation;
import com.example.api.dto.enums.EventType;
import com.example.application.mapper.FilmMapper;
import com.example.application.repository.entity.Genre;
import com.example.application.repository.entity.Director;
import com.example.application.repository.entity.Film;
import com.example.application.repository.entity.User;
import com.example.application.repository.DirectorRepository;
import com.example.application.repository.FilmRepository;
import com.example.application.service.EventService;
import com.example.application.service.FilmService;
import com.example.application.service.GenreService;
import com.example.application.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FilmServiceImpl implements FilmService {

    private final FilmMapper filmMapper;
    private final EventService eventService;
    private final UserService userService;
    private final GenreService genreService;
    private final DirectorRepository directorRepository;
    private final FilmRepository filmRepository;

    @Override
    @Transactional
    public Film create(FilmDto filmDto) {
        Film film = filmMapper.toFilm(filmDto);

        return filmRepository.save(film);
    }

    @Override
    @Transactional
    public Film update(FilmDto filmDto) {
        Film film = findById(filmDto.id());

        return filmMapper.updateFilm(filmDto, film);
    }

    @Override
    @Transactional(readOnly = true)
    public Film findById(Long id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с указанным идентификатором не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Film> findAll() {
        return filmRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Film> findAllByDirectorId(Long directorId, Film.SortBy sortBy) {
        if (!directorRepository.existsById(directorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Режиссер с указанным идентификатором не найден");
        }
        return filmRepository.findAllByDirectors_Id(directorId, Sort.by(sortBy.getProperty()));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        filmRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addLike(Long id, Long userId) {
        Film film = findById(id);
        User user = userService.findById(userId);

        if (!film.getLikingUsers().contains(user)) {
            film.getLikingUsers().add(user);
            film.setLikesAmount(film.getLikesAmount() + 1);
        }
        eventService.create(userId, EventType.LIKE, EventOperation.ADD, id);
    }

    @Override
    @Transactional
    public void deleteLike(Long id, Long userId) {
        Film film = findById(id);
        User user = userService.findById(userId);

        film.getLikingUsers().remove(user);
        film.setLikesAmount(film.getLikesAmount() - 1);
        eventService.create(userId, EventType.LIKE, EventOperation.REMOVE, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Film> findCommon(Long userId, Long friendId) {
        return filmRepository.findCommon(userId, friendId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Film> findPopular(Integer count, Long genreId, Integer year) {
        Pageable pageable = PageRequest.of(0, count, Sort.by(Sort.Direction.DESC, "likesAmount"));

        return filmRepository.findAll(createFindPopularSpecification(genreId, year), pageable).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Film> search(String query, List<String> by) {
        List<Sort.Order> orders = List.of(Sort.Order.desc("likesAmount"), Sort.Order.asc("id"));

        return filmRepository.findAll(createSearchSpecification(query, by), Sort.by(orders));
    }

    private Specification<Film> createFindPopularSpecification(Long genreId, Integer year) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (genreId != null) {
                Genre genre = genreService.findById(genreId);

                predicates.add(criteriaBuilder.isMember(genre, root.get("genres")));
            }
            if (year != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.function("date_part", Integer.class, criteriaBuilder.literal("year"), root.get("releaseDate")), year));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    private Specification<Film> createSearchSpecification(String query, List<String> by) {
        return ((root, query1, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (by.contains("director")) {
                Optional<Director> director = directorRepository.findByNameContainingIgnoreCase(query);

                director.ifPresent(value -> predicates.add(criteriaBuilder.isMember(value, root.get("directors"))));
            }
            if (by.contains("title")) {
                Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), criteriaBuilder.lower(criteriaBuilder.literal("%"+ query +"%"))));

                predicates.add(predicate);
            }
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        });
    }
}
