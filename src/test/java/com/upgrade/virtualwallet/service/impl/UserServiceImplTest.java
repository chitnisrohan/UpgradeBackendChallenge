package com.upgrade.virtualwallet.service.impl;

import com.upgrade.virtualwallet.models.User;
import com.upgrade.virtualwallet.repository.UserRepository;
import com.upgrade.virtualwallet.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createAndFindUser() {
        User user = new User("John", "Doe", "121 Main St.", "8889991111");
        userService.createUser(user);
        when(userRepository.findById(0l)).thenReturn(Optional.of(user));
        assertEquals("John", userService.findUser(0).getFirstName());
    }

}