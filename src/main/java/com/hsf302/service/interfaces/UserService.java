package com.hsf302.service.interfaces;

import com.hsf302.pojo.User;

public interface UserService {
    User findByEmail(String email);
}
