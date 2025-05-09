package com.where.business.repository;

import com.where.business.entity.Review;
import com.where.business.entity.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    boolean existsByBusinessIdAndReviewer(Long businessId, Reviewer reviewer);

}
