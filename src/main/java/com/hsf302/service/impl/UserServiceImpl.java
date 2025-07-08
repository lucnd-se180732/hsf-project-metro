package com.hsf302.service.impl;

import com.hsf302.dto.UserDTO;
import com.hsf302.enums.Role;
import com.hsf302.pojo.User;
import com.hsf302.repository.UserRepository;
import com.hsf302.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAllByRole(Role.CUSTOMER).stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getFullName(),
                        user.getRole().name(),
                        user.isStudent(),
                        user.isActive()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void toggleUserStatus(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setActive(!user.isActive());
            userRepository.save(user);
        });
    }

    @Override
    public User getRequiredByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));
    }

}

