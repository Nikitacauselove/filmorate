package com.example.filmorate.service;

import com.example.filmorate.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    Review create(Review review);

    Review update(Review review);

    Review findById(int id);

    List<Review> findAll(Optional<Integer> filmId, int count);

    void deleteById(int id);

    void addMark(int id, int userId, Review.MarkType markType);

    void deleteMark(int id, int userId, Review.MarkType markType);
}
