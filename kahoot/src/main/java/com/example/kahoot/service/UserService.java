package com.example.kahoot.service;

import com.example.kahoot.entity.User;
import com.example.kahoot.model.LoginModel;
import com.example.kahoot.model.RegisterModel;
import com.example.kahoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User saveUser(RegisterModel registerModel) {
        var user = new User();
        user.setUsername(registerModel.getLogin());
        user.setEmail(registerModel.getEmail());
        user.setPassword(passwordEncoder.encode(registerModel.getPassword()));
        user.setAvatarUrl(registerModel.getAvatar());
        return userRepository.save(user);
    }

    public boolean validatePassword(LoginModel loginModel) {
        var user = userRepository.findByUsername(loginModel.getLogin());
        return user != null && passwordEncoder.matches(loginModel.getPassword(), user.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}