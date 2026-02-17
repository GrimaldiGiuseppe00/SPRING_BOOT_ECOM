package com.ecommerce.Ecom.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;
@Data
public class SignUpRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 5, max = 40)
    private String password;
}
