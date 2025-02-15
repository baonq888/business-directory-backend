package com.where.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AppUserDTO {
    private Long id;
    private String name;
    private String username;
    private String password;
}
