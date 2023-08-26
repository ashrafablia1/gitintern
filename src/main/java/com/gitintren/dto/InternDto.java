package com.gitintren.dto;

import com.gitintren.model.Resume;
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
public class InternDto {

    private Long internId;

    @NotEmpty(message = "Please enter valid email.")
    @Email
    private String email;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private String password;


    private String phoneNumber;

    private String country;

    private  String city;

    private Resume resume;

}
