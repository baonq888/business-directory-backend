package com.where.business.entity;

import com.where.business.enums.BusinessStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "business",
        indexes = {
                @Index(name = "idx_business_name", columnList = "name"),
                @Index(name = "idx_business_district", columnList = "districtName"),
                @Index(name = "idx_business_city", columnList = "cityName"),
        }
)
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String districtName;
    private String cityName;
    private String countryName;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_info_id")
    private ContactInfo contactInfo;

    @Enumerated(EnumType.STRING)
    private BusinessStatus status;

}
