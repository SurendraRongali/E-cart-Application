package com.kpit.SpringShoppingCart.service;

import com.kpit.SpringShoppingCart.entity.User;
import com.kpit.SpringShoppingCart.entity.UserModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User createUser(UserModel model);

    User readUser();

    User updateUser(UserModel user);

    void deleteUser();

    User getLoggedInUser();

    UserDetailsService userDetailsService();
}
