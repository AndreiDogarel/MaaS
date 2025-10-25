package com.example.maas.entities;

import lombok.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserLoginDto {
    private String username;
    private String password;
}