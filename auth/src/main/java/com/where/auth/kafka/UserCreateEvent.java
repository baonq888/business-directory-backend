package com.where.auth.kafka;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
public class UserCreateEvent {
    private Long id;
    private String name;
    private String username;
    Set<String> roles;
}

