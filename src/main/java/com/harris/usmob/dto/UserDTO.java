package com.harris.usmob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object for User
 */
@AllArgsConstructor
@Data
public class UserDTO {
    /**
     * User ID
     */
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
    private String email;
}
