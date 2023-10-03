package com.clearsolutions.users.user.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserUpdateRequest(
        String email,
        String firstName,
        String lastName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDate birthday,
        String country,
        String city,
        String street,
        Long houseNumber,
        Long apartmentNumber,
        String phone

){
}
