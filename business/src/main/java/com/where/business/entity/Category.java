package com.where.business.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    public Category(String name) {
        this.name = name;
    }

    public Category(Long categoryId) {
        this.id = categoryId;
    }
}
