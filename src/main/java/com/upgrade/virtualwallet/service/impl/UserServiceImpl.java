package com.upgrade.virtualwallet.service.impl;

import com.upgrade.virtualwallet.models.User;
import com.upgrade.virtualwallet.repository.UserRepository;
import com.upgrade.virtualwallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User findUser(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent() ? user.get() : null;
    }
}
