package com.where.email.kafka.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessStatusUpdateEvent {
    private String businessOwnerEmail;
    private String businessName;
    private String status;
}
