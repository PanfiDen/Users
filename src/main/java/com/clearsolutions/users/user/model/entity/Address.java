package com.clearsolutions.users.user.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String country;
    private String city;
    private String street;
    private Long houseNumber;
    private Long apartmentNumber;
}
