package com.harris.usmob.controller;

import com.harris.usmob.dto.UserDTO;
import com.harris.usmob.entity.User;
import com.harris.usmob.error.ErrorResponse;
import com.harris.usmob.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createNewUser(@RequestBody User user) {
        UserDTO u = userService.createUser(user);

        if (u == null) {
            ErrorResponse errorResponse = new ErrorResponse("Email is already in use. Please try again.");
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable String userId, @RequestBody User user) {
        Optional<UserDTO> u = userService.updateUser(userId, user);

        if (u.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("User does not exist or new email is already in use. Please try again.");
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(u.get(), HttpStatus.OK);
    }

    @GetMapping("/search/{email}")
    public ResponseEntity<Object> fetchUserByEmail(@PathVariable String email) {
        Optional<UserDTO> u = userService.getUserByEmail(email);

        if (u.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("User does not exist.");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(u.get(), HttpStatus.OK);
    }
}