package com.blps.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
public class Authority implements GrantedAuthority {
    private static final long serialVersionUID = 1L;

    private String authority;
}
