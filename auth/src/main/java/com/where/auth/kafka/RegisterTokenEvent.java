package com.where.auth.kafka;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterTokenEvent {
    private String email;
    private String name;
    private String link;
}
