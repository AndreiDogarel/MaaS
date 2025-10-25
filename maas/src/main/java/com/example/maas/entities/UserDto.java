package com.example.maas.entities;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String role;

    public UserDto(User user) {
        this.username = user.getUsername();
        this.role = user.getRole().getName().name();
    }
}
