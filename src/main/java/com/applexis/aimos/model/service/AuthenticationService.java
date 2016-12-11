package com.applexis.aimos.model.service;

import com.applexis.aimos.model.ProtectedUser;
import com.applexis.aimos.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthenticationService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public AuthenticationService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByLogin(username);
        if (user == null) {
            return null;
        }
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        return new ProtectedUser(user.getLogin(),
                user.getPassword(), Collections.singletonList(authority), user.getToken());
    }

    public UserDetails loadUserByToken(String token) throws UsernameNotFoundException {
        User user = userService.getByToken(token);
        if (user == null) {
            return null;
        }
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        return new ProtectedUser(user.getLogin(),
                user.getPassword(), Collections.singletonList(authority), user.getToken());
    }
}
