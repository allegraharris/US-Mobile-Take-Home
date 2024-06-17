package com.harris.usmob.controller;

import com.harris.usmob.dto.UserDTO;
import com.harris.usmob.entity.User;
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

    @GetMapping("/all")
    public ResponseEntity<Object> getUsers() {
        List<UserDTO> u = userService.getAllUsers();

        if(u.isEmpty()) {
            return new ResponseEntity<>("No users found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createNewUser(@RequestBody User user) {
        UserDTO u = userService.createUser(user);

        if (u == null) {
            return new ResponseEntity<>("Email is already in use.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable String userId, @RequestBody User user) {
        Optional<UserDTO> u = userService.updateUser(userId, user);

        if (u.isEmpty()) {
            return new ResponseEntity<>("User does not exist or new email is already in use.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(u.get(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{mdn}")
    public ResponseEntity<String> deleteUser(@PathVariable String mdn) {
        Boolean b = userService.deleteUser(mdn);
        if (!b) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "text/plain")
                    .body("User does not exist.");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "text/plain")
                .body("User deleted.");
    }

    @GetMapping("/search/{email}")
    public ResponseEntity<Object> fetchUserByEmail(@PathVariable String email) {
        Optional<UserDTO> u = userService.getUserByEmail(email);

        if (u.isEmpty()) {
            return new ResponseEntity<>("User does not exist.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(u.get(), HttpStatus.OK);
    }
}