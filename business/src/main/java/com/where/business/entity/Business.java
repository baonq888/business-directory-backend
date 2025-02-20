package com.where.business.entity;

import com.where.business.entity.location.District;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Long districtId;  // GeoNames district ID
    private Long cityId;      // GeoNames city ID
    private String countryCode; // GeoNames country code

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_info_id")
    private ContactInfo contactInfo;
}
