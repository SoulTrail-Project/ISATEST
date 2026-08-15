package com.sixth.soul_trail.service;

import com.sixth.soul_trail.pojo.User;

public interface UserService {

    boolean register(User user);

    User findByUserName(String username);
}
