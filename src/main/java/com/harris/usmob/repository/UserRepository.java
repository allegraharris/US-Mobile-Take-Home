package com.harris.usmob.repository;

import com.harris.usmob.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for User
 */
public interface UserRepository extends MongoRepository<User, String> {
    /**
     * Find by Email
     * @param email Email
     * @return User object
     */
    User findByEmail(String email);
}
