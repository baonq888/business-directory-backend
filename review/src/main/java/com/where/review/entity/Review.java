package com.where.review.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long businessId;
    private int rating;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private Reviewer reviewer;
}
