package com.harris.usmob.service;

import com.harris.usmob.dto.UserDTO;
import com.harris.usmob.entity.User;
import com.harris.usmob.repository.CycleRepository;
import com.harris.usmob.repository.DailyUsageRepository;
import com.harris.usmob.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private DailyUsageRepository dailyUsageRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        cycleRepository.deleteAll();
        dailyUsageRepository.deleteAll();
        userService = new UserService(cycleRepository, dailyUsageRepository, userRepository);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        cycleRepository.deleteAll();
        dailyUsageRepository.deleteAll();
    }

    @Test
    void testCreateUser() {
        // Given
        User user = new User("user-id-1", "2024600871", "John", "Doe", "john@doe.com", "password");

        // When
        UserDTO createdUser = userService.createUser(user);

        // Then
        assertNotNull(createdUser);
        assertEquals("john@doe.com", createdUser.getEmail());
        // Add more assertions based on your business logic
    }

    @Test
    void testDeleteUser() {
        // Given
        User user = new User("userId2", "2024600872", "Jane", "Doe", "jane@doe.com", "password");
        userRepository.save(user);

        // When
        boolean result = userService.deleteUser("userId2");

        // Then
        assertTrue(result);
        assertFalse(userRepository.findById("userId2").isPresent());
    }

    @Test
    void testTransferMDN() {
        // Given
        User userA = new User("userId3", "2024600873", "Alice", "Smith", "alice@example.com", "passwordA");
        User userB = new User("userId4", "", "Bob", "Johnson", "bob@example.com", "passwordB");
        userRepository.save(userA);
        userRepository.save(userB);

        // When
        List<UserDTO> updatedUsers = userService.transferMDN("userId4", "userId3");

        // Then
        assertNotNull(updatedUsers);
        assertEquals(2, updatedUsers.size());
        assertEquals("2024600873", updatedUsers.getFirst().getMdn());
        assertEquals("", updatedUsers.get(1).getMdn());
    }

    @Test
    void testGetAllUsers() {
        // Given
        userRepository.save(new User("userId5", "2024600874", "Charlie", "Brown", "charlie@example.com", "password"));

        // When
        List<UserDTO> allUsers = userService.getAllUsers();

        // Then
        assertEquals(1, allUsers.size());
        assertEquals("2024600874", allUsers.getFirst().getMdn());
    }

    @Test
    void testGetUserByEmail() {
        // Given
        User user = new User("userId6", "2024600875", "Eve", "Davis", "eve@example.com", "password");
        userRepository.save(user);

        // When
        Optional<UserDTO> foundUser = userService.getUserByEmail("eve@example.com");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("2024600875", foundUser.get().getMdn());
    }

    @Test
    void testUpdateUser() {
        // Given
        User user = new User("userId7", "2024600876", "Frank", "Evans", "frank@example.com", "password");
        userRepository.save(user);

        User updatedUser = new User("userId7", "2024600876", "Franklin", "Evans", "franklin@example.com", "newPassword");

        // When
        Optional<UserDTO> updatedUserDTO = userService.updateUser("userId7", updatedUser);

        // Then
        assertTrue(updatedUserDTO.isPresent());
        assertEquals("Franklin", updatedUserDTO.get().getFirstName());
        assertEquals("franklin@example.com", updatedUserDTO.get().getEmail());
    }

    @Test
    void testCreateUser_WhenEmailAlreadyExists() {
        // Given
        User existingUser = new User("userId1", "2024600871", "John", "Doe", "john@doe.com", "password");
        userRepository.save(existingUser);

        User newUser = new User("user-id-1", "2024600872", "Jane", "Doe", "john@doe.com", "password");

        // When
        UserDTO createdUser = userService.createUser(newUser);

        // Then
        assertNull(createdUser); // Expecting null because user creation should fail
    }

    @Test
    void testDeleteUser_WhenUserNotFound() {
        // Given - no user in database

        // When
        boolean result = userService.deleteUser("nonexistent-user-id");

        // Then
        assertFalse(result);
    }

    @Test
    void testTransferMDN_WhenUserNotFound() {
        // Given - no users in database

        // When
        List<UserDTO> updatedUsers = userService.transferMDN("nonexistent-user-id", "another-nonexistent-user-id");

        // Then
        assertNull(updatedUsers); // Expecting null because transfer should fail
    }

    @Test
    void testGetUserByEmail_WhenEmailNotFound() {
        // Given - no users in database

        // When
        Optional<UserDTO> foundUser = userService.getUserByEmail("nonexistent@example.com");

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testUpdateUser_WhenUserNotFound() {
        // Given - no users in database

        User updatedUser = new User("nonexistent-user-id", "2024600876", "Franklin", "Evans", "franklin@example.com", "newPassword");

        // When
        Optional<UserDTO> updatedUserDTO = userService.updateUser("nonexistent-user-id", updatedUser);

        // Then
        assertFalse(updatedUserDTO.isPresent());
    }

    @Test
    void testUpdateUser_WhenEmailAlreadyExists() {
        // Given
        User existingUser1 = new User("userId1", "2024600871", "John", "Doe", "john@doe.com", "password");
        userRepository.save(existingUser1);

        User existingUser2 = new User("userId2", "2024600872", "Jane", "Doe", "jane@doe.com", "password");
        userRepository.save(existingUser2);

        User newUser = new User("userId3", "2024600873", "Alice", "Smith", "alice@example.com", "password");
        userRepository.save(newUser);

        User updatedUser = new User("userId3", "2024600873", "Alice", "Smith", "jane@doe.com", "newPassword");

        // When
        Optional<UserDTO> updatedUserDTO = userService.updateUser("userId3", updatedUser);

        // Then
        assertFalse(updatedUserDTO.isPresent()); // Expecting empty optional because email is already taken
    }
}
