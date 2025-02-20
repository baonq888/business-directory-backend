package com.where.business.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Business> businesses;

    public Category(Long categoryId) {
    }
}
