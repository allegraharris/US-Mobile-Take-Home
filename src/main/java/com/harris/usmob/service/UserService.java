package com.harris.usmob.service;

import com.harris.usmob.dto.UserDTO;
import com.harris.usmob.entity.User;
import com.harris.usmob.repository.CycleRepository;
import com.harris.usmob.repository.DailyUsageRepository;
import com.harris.usmob.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for User
 */
@AllArgsConstructor
@Service
public class UserService {
    /**
     * Cycle Repository
     */
    private final CycleRepository cycleRepository;
    /**
     * Daily Usage Repository
     */
    private final DailyUsageRepository dailyUsageRepository;
    /**
     * User Repository
     */
    private final UserRepository userRepository;

    /**
     * Creates a new user in the collection
     *
     * @param user User
     * @return UserDTO object
     */
    public UserDTO createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return null;
        }
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser.getId(), savedUser.getMdn(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmail());
    }

    /**
     * Deletes a user from the collection
     *
     * @param id User ID
     * @return Boolean
     */
    public Boolean deleteUser(String id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return false;
        }

        userRepository.deleteById(id);

        // Delete references to this user in other tables
        cycleRepository.deleteByUserId(id);
        dailyUsageRepository.deleteByUserId(id);

        return true;
    }

    /**
     * Gets All Users
     *
     * @return List of UserDTO objects
     */
    public List<UserDTO> getAllUsers() {
        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .map(user -> new UserDTO(user.getId(), user.getMdn(), user.getFirstName(), user.getLastName(), user.getEmail()))
                .collect(Collectors.toList());
    }

    /**
     * Gets a User By Email
     *
     * @param email User email
     * @return UserDTO object
     */
    public Optional<UserDTO> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(new UserDTO(user.getId(), user.getMdn(), user.getFirstName(), user.getLastName(), user.getEmail()));
    }

    /**
     * Transfers MDN from User B to User A
     *
     * @param userIdA User ID A
     * @param userIdB User ID B
     * @return List of UserDTO objects
     */
    public List<UserDTO> transferMDN(String userIdA, String userIdB) {
        User a = userRepository.findById(userIdA).orElse(null);
        User b = userRepository.findById(userIdB).orElse(null);

        if (a == null || b == null || b.getMdn().isEmpty()) {
            return null;
        }

        a.setMdn(b.getMdn());
        userRepository.save(a);
        b.setMdn("");
        userRepository.save(b);

        List<UserDTO> updatedUsers = new ArrayList<>();
        updatedUsers.add(new UserDTO(a.getId(), a.getMdn(), a.getFirstName(), a.getLastName(), a.getEmail()));
        updatedUsers.add(new UserDTO(b.getId(), b.getMdn(), b.getFirstName(), b.getLastName(), b.getEmail()));
        return updatedUsers;
    }

    /**
     * Updates a User
     *
     * @param userId User ID
     * @param user   User
     * @return UserDTO object
     */
    public Optional<UserDTO> updateUser(String userId, User user) {
        Optional<User> u = userRepository.findById(userId);
        if (u.isPresent()) {

            User userToUpdate = u.get();

            User existingEmail = userRepository.findByEmail(user.getEmail());
            if (existingEmail != null && !existingEmail.getId().equals(userId)) {
                return Optional.empty();
            }

            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setPassword(user.getPassword());

            User savedUser = userRepository.save(userToUpdate);


            return Optional.of(new UserDTO(savedUser.getId(), savedUser.getMdn(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmail()));
        }
        return Optional.empty();
    }
}
