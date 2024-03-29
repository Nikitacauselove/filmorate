package com.example.filmorate.mapper;

import com.example.filmorate.entity.Review;
import com.example.filmorate.dto.ReviewDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ReviewMapper {

    public Review toReview(ReviewDto reviewDto) {
        return new Review(reviewDto.getReviewId(), reviewDto.getContent(), reviewDto.getIsPositive(), reviewDto.getUserId(), reviewDto.getFilmId(), 0);
    }

    public Review toReview(ReviewDto reviewDto, Review review) {
        return new Review(review.getId(), reviewDto.getContent(), reviewDto.getIsPositive(), review.getUserId(), review.getFilmId(), review.getUseful());
    }

    public ReviewDto toReviewDto(Review review) {
        return new ReviewDto(review.getId(), review.getContent(), review.getIsPositive(), review.getUserId(), review.getFilmId(), review.getUseful());
    }

    public abstract List<ReviewDto> toReviewDto(List<Review> reviews);
}
