package com.kidhood.chat.controller;

import com.kidhood.chat.model.User;
import com.kidhood.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> retrieveAllUse(){
        return userService.getListUser();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerANewUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.getUser(user.getUserName()));
    }
}
