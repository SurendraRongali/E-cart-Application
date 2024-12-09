package com.kpit.SpringShoppingCart.security;

import com.kpit.SpringShoppingCart.service.UserService;
import com.kpit.SpringShoppingCart.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestHeader = request.getHeader("Authorization");
        System.out.println("request : "+requestHeader+" , "+request.toString());
        if(StringUtils.isEmpty(requestHeader) || !requestHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
        }

        String jwtToken = null;
        String userName = null;
        try {
            jwtToken = requestHeader.substring(7);
            userName = jwtTokenUtil.getUserName(jwtToken);
        } catch (IllegalArgumentException ex){
            throw new RuntimeException("Unable to get token");
        } catch (ExpiredJwtException ex){
            throw new RuntimeException("Token is expired");
        } catch (NullPointerException ex){
            throw new RuntimeException("Request Header is null");
        }

        if(StringUtils.isEmpty(userName) || SecurityContextHolder.getContext().getAuthentication() ==null){
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userName);
            if(jwtTokenUtil.validateToken(jwtToken,userDetails)){
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities()
                );

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
                System.out.println("The security context is : "+SecurityContextHolder.getContext().getAuthentication());
            }
        }
        filterChain.doFilter(request,response);
    }
}
