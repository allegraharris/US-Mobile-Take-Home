package com.harris.usmob.repository;

import com.harris.usmob.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    void deleteByMdn(String mdn);

    User findByEmail(String email);

    User findByMdn(String mdn);
}
