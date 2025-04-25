package com.where.business.repository;

import com.where.business.entity.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    @Query("SELECT b FROM Business b " +
            "WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(b.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(b.districtName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(b.cityName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(b.countryName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(b.category.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "ORDER BY " +
            "LOWER(b.category.name) ASC, " +
            "LOWER(b.name) ASC, " +
            "LOWER(b.description) ASC")
    Page<Business> searchByTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
}
