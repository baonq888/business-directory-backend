package com.where.review.service;

import com.where.review.dto.request.ReviewCreateRequest;
import com.where.review.entity.Review;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ReviewService {
    Review createReview(ReviewCreateRequest request);
    Review getReviewById(Long id); // Changed to Optional
    Page<Review> getAllReviews(int page, int limit);
    Review updateReview(Long id, ReviewCreateRequest request);
    void deleteReview(Long id);
}
