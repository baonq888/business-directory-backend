package com.where.email.kafka.register;

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
