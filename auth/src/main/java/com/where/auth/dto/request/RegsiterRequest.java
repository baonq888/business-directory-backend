package com.where.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegsiterRequest {
    private String name;
    private String email;
    private String password;
}
