package com.gitintren.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {

    private Long companyId;

    @NotEmpty(message = "Please enter valid email.")
    @Email
    private String email;

    @NotEmpty
    private String name;

    private String password;

    private String companyLink;

    private String phoneNumber;

    private String country;

    private  String city;
}
