package com.kpit.SpringShoppingCart.service.impl;

import com.kpit.SpringShoppingCart.Repository.UserRepository;
import com.kpit.SpringShoppingCart.entity.User;
import com.kpit.SpringShoppingCart.entity.UserModel;
import com.kpit.SpringShoppingCart.exceptions.ItemAlreadyExistsException;
import com.kpit.SpringShoppingCart.exceptions.ItemNotFoundException;
import com.kpit.SpringShoppingCart.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User createUser(UserModel user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new ItemAlreadyExistsException("User already exists. Try with another email!!");
        }
        User newUser = new User();
        BeanUtils.copyProperties(user,newUser);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public User readUser() {
        Long id = getLoggedInUser().getId();
        return userRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("User not foundfor the id : "+id));
    }

    @Override
    public User updateUser(UserModel user) {
        User existingUser = readUser();

        existingUser.setName(user.getName() == null ? existingUser.getName() : user.getName());
        existingUser.setEmail(user.getEmail() == null ? existingUser.getEmail() : user.getEmail());
        existingUser.setPassword(user.getPassword() == null ? existingUser.getPassword() : passwordEncoder.encode(user.getPassword()));

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser() {
        User user = readUser();
        userRepository.delete(user);
    }

    @Override
    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("loggedInUser : "+auth);
        String userName = auth.getName();

        User user = userRepository.findByEmail(userName).orElseThrow(() ->new UsernameNotFoundException("User not found with email "+userName));
        return user;
    }

    @Override
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
            }
        };
    }
}
