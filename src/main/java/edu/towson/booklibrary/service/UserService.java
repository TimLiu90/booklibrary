package edu.towson.booklibrary.service;


import edu.towson.booklibrary.domain.User;

import java.util.List;

public interface UserService {

    public User saveUser(User user);
    public User findById(String id);
    public User findByUsername(String username);
}
