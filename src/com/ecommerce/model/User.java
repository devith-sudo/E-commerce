// User.java
package com.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String userName;
    private String email;
    private String password;
    private String role;
    private Boolean isDeleted;
    private String uUuid;
}