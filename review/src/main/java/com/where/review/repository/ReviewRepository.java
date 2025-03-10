package com.where.review.repository;

import com.where.review.entity.Review;
import com.where.review.entity.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    boolean existsByBusinessIdAndReviewer(Long businessId, Reviewer reviewer);

}
