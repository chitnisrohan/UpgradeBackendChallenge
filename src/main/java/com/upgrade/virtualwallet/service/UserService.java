package com.upgrade.virtualwallet.service;

import com.upgrade.virtualwallet.models.User;

public interface UserService {

    /**
     * Creates new user in database (account holder)
     * @param user user information
     */
    void createUser(User user);

    /**
     * finds user with given id
     * @param id
     * @return User with given id
     */
    User findUser(long id);
}
