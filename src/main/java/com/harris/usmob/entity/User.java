package com.harris.usmob.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * Entity for User
 */
@AllArgsConstructor
@Data
@Document(collection = "user")
public class User {
    /**
     * User ID - Primary Key
     */
    @MongoId
    private String id;
    /**
     * MDN (Phone number)
     */
    private String mdn;
    /**
     * First Name of user
     */
    private String firstName;
    /**
     * Last Name of user
     */
    private String lastName;
    /**
     * Email of user
     */
    @Indexed(unique = true) // indexed as query email frequently
    private String email;
    /**
     * P