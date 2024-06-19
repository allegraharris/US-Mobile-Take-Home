package com.harris.usmob.controller;

import com.harris.usmob.dto.UserDTO;
import com.harris.usmob.entity.User;
import com.harris.usmob.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Error creating user",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<Object> createNewUser(@RequestBody User user) {
        UserDTO u = userService.createUser(user);

        if (u == null) {
            return new ResponseEntity<>("Email is already in use.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error deleting user",
                    content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {
        Boolean b = userService.deleteUser(id);

        if (!b) {
            return new ResponseEntity<>("User does not exist.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("User deleted successfully.", HttpStatus.OK);
    }

    @Operation(summary = "Get a user by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No user found",
                    content = @Content)
    })
    @GetMapping("/search/{email}")
    public ResponseEntity<Object> fetchUserByEmail(@PathVariable String email) {
        Optional<UserDTO> u = userService.getUserByEmail(email);

        return u.<ResponseEntity<Object>>map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>("User does not exist.", HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No users found",
                    content = @Content)
    })
    @GetMapping("/all")
    public ResponseEntity<Object> getUsers() {
        List<UserDTO> u = userService.getAllUsers();

        if (u.isEmpty()) {
            return new ResponseEntity<>("No users found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @Operation(summary = "Update an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Error updating user",
                    content = @Content)
    })
    @PostMapping("/update/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable String userId, @RequestBody User user) {
        Optional<UserDTO> u = userService.updateUser(userId, user);

        return u.<ResponseEntity<Object>>map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>("User does not exist or new email is already in use.", HttpStatus.CONFLICT));
    }

    @Operation(summary = "Transfer MDN from one user to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MDN transferred successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Error transferring MDN",
                    content = @Content)
    })
    @PostMapping("/transfer/{userIdA}/{userIdB}")
    public ResponseEntity<Object> transferMDN(@PathVariable String userIdA, @PathVariable String userIdB) {
        List<UserDTO> u = userService.transferMDN(userIdA, userIdB);

        if (u == null) { return new ResponseEntity<>("Error transferring MDN.", HttpStatus.CONFLICT); }

        return new ResponseEntity<>(u, HttpStatus.OK);
    }
}