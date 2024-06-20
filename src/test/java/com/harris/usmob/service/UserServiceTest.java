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

/**
 * Unit tests for the UserService class.
 */
@DataMongoTest
public class UserServiceTest {

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private DailyUsageRepository dailyUsageRepository;

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    /**
     * Set up the test environment.
     */
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        cycleRepository.deleteAll();
        dailyUsageRepository.deleteAll();
        userService = new UserService(cycleRepository, dailyUsageRepository, userRepository);
    }

    /**
     * Tear down the test environment.
     */
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        cycleRepository.deleteAll();
        dailyUsageRepository.deleteAll();
    }

    /**
     * Test creating a user.
     * Expect the user to be created successfully.
     */
    @Test
    void testCreateUser() {
        User user = new User("user-id-1", "2024600871", "John", "Doe", "john@doe.com", "password");

        UserDTO createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("john@doe.com", createdUser.getEmail());
    }

    /**
     * Test creating a user when the email already exists.
     * Expect the user to not be created.
     */
    @Test
    void testCreateUser_WhenEmailAlreadyExists() {
        User existingUser = new User("userId1", "2024600871", "John", "Doe", "john@doe.com", "password");
        userRepository.save(existingUser);

        User newUser = new User("user-id-1", "2024600872", "Jane", "Doe", "john@doe.com", "password");
        UserDTO createdUser = userService.createUser(newUser);

        assertNull(createdUser);
    }

    /**
     * Test deleting a user.
     * Expect the user to be deleted successfully.
     */
    @Test
    void testDeleteUser() {
        User user = new User("userId2", "2024600872", "Jane", "Doe", "jane@doe.com", "password");
        userRepository.save(user);

        boolean result = userService.deleteUser("userId2");

        assertTrue(result);
        assertFalse(userRepository.findById("userId2").isPresent());
    }

    /**
     * Test deleting a user when the user is not found.
     * Expect the user to not be deleted.
     */
    @Test
    void testDeleteUser_WhenUserNotFound() {
        boolean result = userService.deleteUser("nonexistent-user-id");
        assertFalse(result);
    }

    /**
     * Test getting all users.
     * Expect all users to be returned.
     */
    @Test
    void testGetAllUsers() {
        userRepository.save(new User("userId5", "2024600874", "Charlie", "Brown", "charlie@example.com", "password"));

        List<UserDTO> allUsers = userService.getAllUsers();

        assertEquals(1, allUsers.size());
        assertEquals("2024600874", allUsers.getFirst().getMdn());
    }

    /**
     * Test getting a user by email.
     * Expect the correct user to be returned.
     */
    @Test
    void testGetUserByEmail() {
        User user = new User("userId6", "2024600875", "Eve", "Davis", "eve@example.com", "password");
        userRepository.save(user);

        Optional<UserDTO> foundUser = userService.getUserByEmail("eve@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("2024600875", foundUser.get().getMdn());
    }

    /**
     * Test getting a user by email when the user is not found.
     * Expect an empty optional.
     */
    @Test
    void testGetUserByEmail_WhenEmailNotFound() {
        Optional<UserDTO> foundUser = userService.getUserByEmail("nonexistent@example.com");
        assertFalse(foundUser.isPresent());
    }

    /**
     * Test transferring MDN from one user to another.
     * Expect the MDN to be transferred successfully.
     */
    @Test
    void testTransferMDN() {
        User userA = new User("userId3", "2024600873", "Alice", "Smith", "alice@example.com", "passwordA");
        User userB = new User("userId4", "", "Bob", "Johnson", "bob@example.com", "passwordB");
        userRepository.save(userA);
        userRepository.save(userB);

        List<UserDTO> updatedUsers = userService.transferMDN("userId4", "userId3");

        assertNotNull(updatedUsers);
        assertEquals(2, updatedUsers.size());
        assertEquals("2024600873", updatedUsers.getFirst().getMdn());
        assertEquals("", updatedUsers.get(1).getMdn());
    }

    /**
     * Test transferring MDN when the user is not found.
     * Expect null.
     */
    @Test
    void testTransferMDN_WhenUserNotFound() {
        List<UserDTO> updatedUsers = userService.transferMDN("nonexistent-user-id", "another-nonexistent-user-id");
        assertNull(updatedUsers);
    }

    /**
     * Test updating a user.
     * Expect the user to be updated successfully.
     */
    @Test
    void testUpdateUser() {
        User user = new User("userId7", "2024600876", "Frank", "Evans", "frank@example.com", "password");
        userRepository.save(user);

        User updatedUser = new User("userId7", "2024600876", "Franklin", "Evans", "franklin@example.com", "newPassword");

        Optional<UserDTO> updatedUserDTO = userService.updateUser("userId7", updatedUser);

        assertTrue(updatedUserDTO.isPresent());
        assertEquals("Franklin", updatedUserDTO.get().getFirstName());
        assertEquals("franklin@example.com", updatedUserDTO.get().getEmail());
    }

    /**
     * Test updating a user when the email already exists.
     * Expect the user to not be updated.
     */
    @Test
    void testUpdateUser_WhenEmailAlreadyExists() {
        User existingUser1 = new User("userId1", "2024600871", "John", "Doe", "john@doe.com", "password");
        userRepository.save(existingUser1);

        User existingUser2 = new User("userId2", "2024600872", "Jane", "Doe", "jane@doe.com", "password");
        userRepository.save(existingUser2);

        User newUser = new User("userId3", "2024600873", "Alice", "Smith", "alice@example.com", "password");
        userRepository.save(newUser);

        User updatedUser = new User("userId3", "2024600873", "Alice", "Smith", "jane@doe.com", "newPassword");

        Optional<UserDTO> updatedUserDTO = userService.updateUser("userId3", updatedUser);

        assertFalse(updatedUserDTO.isPresent());
    }

    /**
     * Test updating a user when the user is not found.
     * Expect an empty optional.
     */
    @Test
    void testUpdateUser_WhenUserNotFound() {
        User updatedUser = new User("nonexistent-user-id", "2024600876", "Franklin", "Evans", "franklin@example.com", "newPassword");

        Optional<UserDTO> updatedUserDTO = userService.updateUser("nonexistent-user-id", updatedUser);

        assertFalse(updatedUserDTO.isPresent());
    }
}
