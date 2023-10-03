package com.clearsolutions.users.user.service;

import com.clearsolutions.users.advice.exceptions.BadRequestException;
import com.clearsolutions.users.advice.exceptions.UserNotFoundException;
import com.clearsolutions.users.user.model.entity.Address;
import com.clearsolutions.users.user.model.entity.User;
import com.clearsolutions.users.user.model.properties.UserProperties;
import com.clearsolutions.users.user.model.request.UserCreateRequest;
import com.clearsolutions.users.user.model.request.UserUpdateRequest;
import com.clearsolutions.users.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private UserProperties userProperties;

    public String createUser(UserCreateRequest userCreateRequest) {

        if (userCreateRequest == null)
            throw new BadRequestException("User cannot be null");

        userRepository.save(getValidateUser(userCreateRequest));

        return "User was created successfully";
    }

    public String updateUser(Long userId, UserUpdateRequest userUpdateRequest) {

        if (userUpdateRequest == null){
            throw new BadRequestException("userUpdateRequest is null");
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);



        userRepository.save(getValidateUser(user, userUpdateRequest));

        return "User was updated successfully";
    }

    public String deleteUser(Long userId) {

        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        userRepository.deleteById(userId);

        return "User was deleted successfully";
    }

    public List<User> getUsersByAgeRange(int from, int to) {
        if (from > to){
            throw new BadRequestException("“From” must be less than “To”!");
        }

        List<User> users = userRepository.findAll();

        List<User> usersInAgeRange = new ArrayList<>();

        for (User user : users) {
            int age = Period.between(user.getBirthday(), LocalDate.now()).getYears();
            if (age >= from && age <= to) {
                usersInAgeRange.add(user);
            }
        }

        return usersInAgeRange.stream().sorted(Comparator.comparing(User::getBirthday)).toList();
    }

    private User getValidateUser(UserCreateRequest request){
        User user = new User();

        return validateUser(user,
                request.email(), request.firstName(), request.lastName(), request.birthday(), request.country(),
                request.city(), request.street(), request.houseNumber(), request.apartmentNumber(), request.phone());
    }

    private User getValidateUser(User user, UserUpdateRequest request){
        return validateUser(user,
                request.email(), request.firstName(), request.lastName(), request.birthday(), request.country(),
                request.city(), request.street(), request.houseNumber(), request.apartmentNumber(), request.phone());
    }

    private User validateUser(User user,
                              String email, String firstName, String lastName, LocalDate birthday, String country,
                              String city, String street, Long houseNumber, Long apartmentNumber, String phone) {

        if (email.isBlank() || !Pattern.compile(userProperties.getUserEmailRegex()).matcher(email).matches() || userRepository.existsByEmail(email)){
            throw new BadRequestException("Invalid email!");
        }else {
            user.setEmail(email);
        }

        if (firstName == null || firstName.isEmpty()){
            throw new BadRequestException("First name cannot be null!");
        }else {
            user.setFirstName(firstName);
        }

        if (lastName == null || lastName.isEmpty()){
            throw new BadRequestException("Last name cannot be null!");
        }else {
            user.setLastName(lastName);
        }

        if (birthday ==  null){
            throw new BadRequestException("Invalid birthday!");
        }else if (birthday.isAfter(LocalDate.now().minusYears(userProperties.getUserMinAge()))){
            throw new BadRequestException("User must be 18!");
        }else {
            user.setBirthday(birthday);
        }

        Address address = Address.builder()
                .country(country)
                .city(city)
                .street(street)
                .houseNumber(houseNumber)
                .apartmentNumber(apartmentNumber)
                .build();
        user.setAddress(address);

        user.setPhone(phone);
        return user;
    }
}
