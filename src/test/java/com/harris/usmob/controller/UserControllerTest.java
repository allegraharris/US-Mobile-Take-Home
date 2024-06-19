package com.harris.usmob.controller;

import com.harris.usmob.dto.UserDTO;
import com.harris.usmob.entity.User;
import com.harris.usmob.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private static final String BASE_URL = "/api/user";

    private User mockUser;
    private UserDTO mockUserDTO;

    @BeforeEach
    public void setup() {
        mockUser = new User("jdew09xm092zm09x", "2024600871", "John", "Doe", "john@doe.com", "password");
        mockUserDTO = new UserDTO("jdew09xm092zm09x", "2024600871", "John", "Doe", "john@doe.com");
    }

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

    @Test
    public void testCreateUserConflict() throws Exception {
        Mockito.when(userService.createUser(any(User.class))).thenReturn(null);

        mockMvc.perform(post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUser)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Email is already in use."));
    }

    @Test
    public void testDeleteUser() throws Exception {
        Mockito.when(userService.deleteUser(anyString())).thenReturn(true);

        mockMvc.perform(delete(BASE_URL + "/delete/{id}", mockUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully."));
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        Mockito.when(userService.deleteUser(anyString())).thenReturn(false);

        mockMvc.perform(delete(BASE_URL + "/delete/{id}", mockUser.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User does not exist."));
    }

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

    @Test
    public void testFetchUserByEmailNotFound() throws Exception {
        Mockito.when(userService.getUserByEmail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/search/{email}", mockUser.getEmail()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User does not exist."));
    }

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

    @Test
    public void testGetAllUsersNotFound() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/all"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No users found."));
    }
}
