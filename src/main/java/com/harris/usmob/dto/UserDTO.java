package com.harris.usmob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDTO {
    private String id;
    private String mdn;
    private String firstName;
    private String lastName;
    private String email;
}
