package com.studentProject.services;

import com.studentProject.entities.Role;
import com.studentProject.entities.User;
import com.studentProject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    KafkaProducerService kafkaProducerService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public User findByUsername(String username) {
        User dbUser = userRepository.findByUsername(username);
        return dbUser;
    }

    public void saveUser(String password, String username, PasswordEncoder passwordEncoder) {
        String password1 = passwordEncoder.encode(password);
        User newUser = new User(username, password1, true, Role.USER);
        kafkaProducerService.sendUser(newUser);
        userRepository.save(newUser);
    }
}
