package com.studentProject.services;

import com.studentProject.entities.User;
import com.studentProject.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    KafkaProducerService kafkaProducerService;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;


    @Test
    public void shouldReturnUserByName() {

        userService.loadUserByUsername("test");

        verify(userRepository).findByUsername(Mockito.anyString());
    }

    @Test
    public void shouldSaveUser() {

        userService.saveUser("test", "test", passwordEncoder);

        verify(userRepository).save(any(User.class));
    }

}