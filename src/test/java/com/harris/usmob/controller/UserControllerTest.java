package com.harris.usmob.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harris.usmob.dto.UserDTO;
import com.harris.usmob.entity.User;
import com.harris.usmob.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the UserController class.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final String BASE_URL = "/api/user";

    @Autowired
    private MockMvc mockMvc;

    private User mockUser;

    private UserDTO mockUserDTO;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    /**
     * Set up mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        mockUser = new User("jdew09xm092zm09x", "2024600871", "John", "Doe", "john@doe.com", "password");
        mockUserDTO = new UserDTO("jdew09xm092zm09x", "2024600871", "John", "Doe", "john@doe.com");
    }

    /**
     * Test creating a user.
     * Expect a 201 status code and the user details.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateUser() throws Exception {
        Mockito.when(userService.createUser(any(User.class))).thenReturn(mockUserDTO);

        mockMvc.perform(post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockUserDTO.getId()))
                .andExpect(jsonPath("$.firstName").value(mockUserDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(mockUserDTO.getLastName()))
                .andExpect(jsonPath("$.email").value(mockUserDTO.getEmail()));
    }

    /**
     * Test creating a user when the email is already in use.
     * Expect a 409 status code and an error message.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateUserConflict() throws Exception {
        Mockito.when(userService.createUser(any(User.class))).thenReturn(null);

        mockMvc.perform(post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUser)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Email is already in use."));
    }

    /**
     * Test deleting a user.
     * Expect a 200 status code and a success message.
     * @throws Exception if an error occurs
     */
    @Test
    public void testDeleteUser() throws Exception {
        Mockito.when(userService.deleteUser(anyString())).thenReturn(true);

        mockMvc.perform(delete(BASE_URL + "/delete/{id}", mockUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully."));
    }

    /**
     * Test deleting a user when the user is not found.
     * Expect a 404 status code and an error message.
     * @throws Exception if an error occurs
     */
    @Test
    public void testDeleteUserNotFound() throws Exception {
        Mockito.when(userService.deleteUser(anyString())).thenReturn(false);

        mockMvc.perform(delete(BASE_URL + "/delete/{id}", mockUser.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User does not exist."));
    }

    /**
     * Test fetching a user by email.
     * Expect a 200 status code and the user details.
     * @throws Exception if an error occurs
     */
    @Test
    public void testFetchUserByEmail() throws Exception {
        Mockito.when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(mockUserDTO));

        mockMvc.perform(get(BASE_URL + "/search/{email}", mockUser.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUserDTO.getId()))
                .andExpect(jsonPath("$.firstName").value(mockUserDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(mockUserDTO.getLastName()))
                .andExpect(jsonPath("$.email").value(mockUserDTO.getEmail()));
    }

    /**
     * Test fetching a user by email when the user is not found.
     * Expect a 404 status code and an error message.
     * @throws Exception if an error occurs
     */
    @Test
    public void testFetchUserByEmailNotFound() throws Exception {
        Mockito.when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/search/{email}", mockUser.getEmail()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User does not exist."));
    }

    /**
     * Test fetching all users.
     * Expect a 200 status code and the list of users.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.singletonList(mockUserDTO));

        mockMvc.perform(get(BASE_URL + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(mockUserDTO.getId()))
                .andExpect(jsonPath("$[0].firstName").value(mockUserDTO.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(mockUserDTO.getLastName()))
                .andExpect(jsonPath("$[0].email").value(mockUserDTO.getEmail()));
    }

    /**
     * Test fetching all users when no users exist.
     * Expect a 404 status code and an error message.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetAllUsersNotFound() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/all"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No users found."));
    }
}
