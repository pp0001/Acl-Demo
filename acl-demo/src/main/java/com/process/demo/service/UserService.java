package com.process.demo.service;

import com.process.demo.exception.EmailExistsException;
import com.process.demo.model.User;

public interface UserService {

    User registerNewUser(User user) throws EmailExistsException;

    User findUserByEmail(String email);

}
