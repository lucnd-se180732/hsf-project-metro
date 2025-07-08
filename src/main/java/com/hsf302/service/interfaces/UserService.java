package com.hsf302.service.interfaces;

import com.hsf302.dto.UserDTO;
import com.hsf302.pojo.User;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();

    void toggleUserStatus(Long userId);

    User getRequiredByEmail(String email);
}
