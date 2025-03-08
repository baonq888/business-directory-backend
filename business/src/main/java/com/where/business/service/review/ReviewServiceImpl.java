package com.where.business.service.review;

import com.where.business.dto.request.ReviewCreateRequest;
import com.where.business.entity.Business;
import com.where.business.entity.Review;
import com.where.business.entity.Reviewer;
import com.where.business.exception.ReviewException;
import com.where.business.repository.BusinessRepository;
import com.where.business.repository.ReviewRepository;
import com.where.business.repository.ReviewerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BusinessRepository businessRepository;
    private final ReviewerRepository reviewerRepository;

    @Override
    public Review createReview(ReviewCreateRequest request) {
        try {
            Business business = businessRepository.findById(request.getBusinessId())
                    .orElseThrow(() -> new EntityNotFoundException("Business not found with ID: " + request.getBusinessId()));

            Reviewer reviewer = reviewerRepository.findById(request.getReviewerId())
                    .orElseThrow(() -> new EntityNotFoundException("Reviewer not found with ID: " + request.getReviewerId()));

            if (reviewRepository.existsByBusinessAndReviewer(business, reviewer)) {
                throw new ReviewException("Reviewer has already submitted a review for this business");
            }

            Review review = new Review();
            review.setRating(request.getRating());
            review.setComment(request.getComment());
            review.setBusiness(business);
            review.setReviewer(reviewer);

            return reviewRepository.save(review);
        } catch (Exception e) {
            log.error("Failed to create review for business ID: {}, reviewer ID: {}",
                    request.getBusinessId(), request.getReviewerId(), e);
            throw new ReviewException("Failed to create review. Please try again.", e);
        }
    }

    @Override
    public Optional<Review> getReviewById(Long id) {
        try {
            return reviewRepository.findById(id);
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
