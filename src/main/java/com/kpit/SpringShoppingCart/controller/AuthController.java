package com.kpit.SpringShoppingCart.controller;

import com.kpit.SpringShoppingCart.entity.AuthModel;
import com.kpit.SpringShoppingCart.entity.JWTResponse;
import com.kpit.SpringShoppingCart.entity.User;
import com.kpit.SpringShoppingCart.service.UserService;
import com.kpit.SpringShoppingCart.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody AuthModel authModel) throws Exception{
        //authenticate(authModel.getEmail(),authModel.getPassword());
        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authModel.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return new ResponseEntity<>(new JWTResponse(token), HttpStatus.OK);
    }

    private void authenticate(String email, String password){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        } catch (DisabledException e){
            throw new RuntimeException("User Diabled");
        } catch (BadCredentialsException e){
            throw new RuntimeException("Bad Credentials");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
