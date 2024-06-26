package com.example.backend.controller;

import com.example.api.ReviewApi;
import com.example.api.dto.ReviewDto;
import com.example.api.dto.enums.MarkType;
import com.example.backend.repository.entity.Review;
import com.example.backend.mapper.ReviewMapper;
import com.example.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController implements ReviewApi {

    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService;

    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto create(ReviewDto reviewDto) {
        return reviewMapper.toReviewDto(reviewService.create(reviewDto));
    }

    public ReviewDto update(ReviewDto reviewDto) {
        return reviewMapper.toReviewDto(reviewService.update(reviewDto));
    }

    public ReviewDto findById(Long id) {
        return reviewMapper.toReviewDto(reviewService.findById(id));
    }

    public List<ReviewDto> findAll(Long filmId, Integer count) {
        return reviewMapper.toReviewDto(reviewService.findAll(filmId, count));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(Long id) {
        reviewService.deleteById(id);
    }

    public void addLike(Long id, Long userId) {
        reviewService.addMark(id, userId, MarkType.LIKE);
    }

    public void deleteLike(Long id, Long userId) {
        reviewService.deleteMark(id, userId, MarkType.LIKE);
    }

    public void addDislike(Long id, Long userId) {
        reviewService.addMark(id, userId, MarkType.DISLIKE);
    }

    public void deleteDislike(Long id, Long userId) {
        reviewService.deleteMark(id, userId, MarkType.DISLIKE);
    }
}
