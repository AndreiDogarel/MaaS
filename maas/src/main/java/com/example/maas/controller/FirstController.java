package com.example.maas.controller;

import com.example.maas.entities.User;
import com.example.maas.entities.UserDto;
import com.example.maas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin()
public class FirstController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/showUsers")
    public List<UserDto> sayHello() {
        return new HashSet<User>(userRepository.findAll()).stream().map(u -> new UserDto((User)u)).collect(Collectors.toList());
    }
}