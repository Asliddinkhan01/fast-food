package com.fastfood.authService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotNull(message = "Field is required")
    @NotBlank(message = "Full Name cannot be blank.")
    private String fullName;

    @NotNull(message = "Field is required")
    @NotBlank(message = "Phone Number cannot be blank.")
    private String phoneNumber;

    @NotNull(message = "Field is required")
    @NotBlank(message = "SMS code cannot be blank.")
    private String smsCode;

}
