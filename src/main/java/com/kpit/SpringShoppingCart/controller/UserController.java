package com.kpit.SpringShoppingCart.controller;

import com.kpit.SpringShoppingCart.entity.User;
import com.kpit.SpringShoppingCart.entity.UserModel;
import com.kpit.SpringShoppingCart.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity<User> save(@Valid @RequestBody UserModel user){
        return new ResponseEntity<User>(userService.createUser(user),HttpStatus.CREATED);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<User> readUser(){
        return new ResponseEntity<>(userService.readUser(),HttpStatus.OK);
    }

    @PutMapping("/user/profile")
    public ResponseEntity<User> updateUser(@RequestBody UserModel user){
        return new ResponseEntity<>(userService.updateUser(user),HttpStatus.OK);
    }

    @DeleteMapping("/user")
    public ResponseEntity<HttpStatus> deleteUser(){
        userService.deleteUser();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
