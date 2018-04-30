package com.example.sentiment.services;

import com.example.sentiment.entities.SentUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SentUserService extends UserDetailsService {

    SentUser findByEmail(String email);

    SentUser save(UserRegistrationDto registration);

}