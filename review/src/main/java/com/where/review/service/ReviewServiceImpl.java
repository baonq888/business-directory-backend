package com.where.review.service;

import com.where.review.dto.request.ReviewCreateRequest;
import com.where.review.entity.Review;
import com.where.review.entity.Reviewer;
import com.where.review.exception.ReviewException;
import com.where.review.repository.ReviewRepository;
import com.where.review.repository.ReviewerRepository;
import org.springframework.data.domain.Page;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(ReviewCreateRequest request) {
        try {
            Reviewer reviewer = new Reviewer(
                    request.getReviewerId(),
                    request.getEmail(),
                    request.getName()
            );
            if (reviewRepository.existsByBusinessIdAndReviewer(request.getBusinessId(),reviewer)) {
                throw new ReviewException("Reviewer has already submitted a review for this business");
            }

            Review review = new Review();

            review.setRating(request.getRating());
            review.setComment(request.getComment());
            review.setBusinessId(review.getBusinessId());
            review.setReviewer(reviewer);

            return reviewRepository.save(review);
        } catch (Exception e) {
            log.error("Failed to create review for business ID: {}, reviewer ID: {}",
                    request.getBusinessId(), request.getReviewerId(), e);
            throw new ReviewException("Failed to create review. Please try again.", e);
        }
    }

    @Override
    public Review getReviewById(Long id) {
        try {
            return reviewRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("Review not found with ID: " + id));

        } catch (Exception e) {
            log.error("Error retrieving review with ID: {}", id, e);
            throw new ReviewException("Failed to retrieve review. Please try again later.", e);
        }
    }

    @Override
    public Page<Review> getAllReviews(int page, int limit) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            return reviewRepository.findAll(pageable);
        } catch (Exception e) {
            log.error("Error fetching reviews", e);
            throw new ReviewException("Failed to fetch reviews. Please try again later.", e);
        }
    }

    @Override
    public Review updateReview(Long id, ReviewCreateRequest request) {
        try {
            Review existingReview = reviewRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + id));

            existingReview.setRating(request.getRating());
            existingReview.setComment(request.getComment());

            return reviewRepository.save(existingReview);
        } catch (Exception e) {
            log.error("Failed to update review with ID: {}", id, e);
            throw new ReviewException("Failed to update review. Please try again later.", e);
        }
    }

    @Override
    public void deleteReview(Long id) {
        try {
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + id));

            reviewRepository.delete(review);
        } catch (Exception e) {
            log.error("Failed to delete review with ID: {}", id, e);
            throw new ReviewException("Failed to delete review. Please try again later.", e);
        }
    }
}
