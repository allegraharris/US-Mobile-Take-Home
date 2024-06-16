package com.harris.usmob.service;

import com.harris.usmob.dto.UserDTO;
import com.harris.usmob.entity.User;
import com.harris.usmob.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserDTO createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return null;
        }
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmail());
    }

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


            return Optional.of(new UserDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmail()));
        }
        return Optional.empty();
    }

    public List<UserDTO> getAllUsers() {
        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .map(user -> new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()));
    }
}
