package com.clearsolutions.users.user.controller;

import com.clearsolutions.users.user.model.entity.User;
import com.clearsolutions.users.user.model.request.UserCreateRequest;
import com.clearsolutions.users.user.model.request.UserUpdateRequest;
import com.clearsolutions.users.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping
    public String createUser(@RequestBody UserCreateRequest userCreateRequest){
        return userService.createUser(userCreateRequest);
    }

    @PatchMapping("/{user-id}")
    public String updateUser(@PathVariable("user-id") Long userId,
                             @RequestBody(required = false) UserUpdateRequest userUpdateRequest){
        return userService.updateUser(userId, userUpdateRequest);
    }

    @DeleteMapping("/{user-id}")
    public String deleteUser(@PathVariable("user-id") Long userId){
        return userService.deleteUser(userId);
    }

    @GetMapping//("/{from}-{to}")
    public List<User> getUsersByAgeRange(@RequestParam(defaultValue = "18") int from,
                                         @RequestParam(defaultValue = "122") int to){
        return userService.getUsersByAgeRange(from, to);
    }
}
