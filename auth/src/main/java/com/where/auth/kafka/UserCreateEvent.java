package com.where.auth.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCreateEvent {
    private Long id;
    private String name;
    private String email;
    Set<String> roles;
}

