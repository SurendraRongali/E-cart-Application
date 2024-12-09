package com.kpit.SpringShoppingCart.entity;

public class JWTResponse {

    private final String jwtToken;

    public JWTResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public JWTResponse(){this.jwtToken = null;}

    public String getJwtToken() {
        return jwtToken;
    }
}
