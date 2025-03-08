package com.where.business.service.review;

import com.where.business.dto.request.ReviewCreateRequest;
import com.where.business.entity.Review;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ReviewService {
    Review createReview(ReviewCreateRequest request);
    Optional<Review> getReviewById(Long id); // Changed to Optional
    Page<Review> getAllReviews(int page, int limit);
    Review updateReview(Long id, ReviewCreateRequest request);
    void deleteReview(Long id);
}
