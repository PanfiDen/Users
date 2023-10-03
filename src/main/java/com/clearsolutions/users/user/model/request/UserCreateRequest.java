package com.clearsolutions.users.user.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserCreateRequest(
        @NotBlank
        String email,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate birthday,
        String country,
        String city,
        String street,
        Long houseNumber,
        Long apartmentNumber,
        String phone

){}
