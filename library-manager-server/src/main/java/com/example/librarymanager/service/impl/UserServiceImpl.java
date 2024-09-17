package com.example.librarymanager.service.impl;

import com.example.librarymanager.config.properties.AdminInfo;
import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.RoleConstant;
import com.example.librarymanager.domain.entity.User;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.UserRepository;
import com.example.librarymanager.service.JwtTokenService;
import com.example.librarymanager.service.RoleService;
import com.example.librarymanager.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    @Value("${user.change-username.price:200}")
    int changeUsernamePrice;

    final MessageSource messageSource;

    final UserRepository userRepository;

    final RoleService roleService;

    final PasswordEncoder passwordEncoder;

    final JwtTokenService jwtTokenService;

    @Override
    public void initAdmin(AdminInfo adminInfo) {
        if (userRepository.count() == 0) {
            try {
                User user = new User();
                user.setUsername(adminInfo.getUsername());
                user.setEmail(adminInfo.getEmail());
                user.setPhoneNumber(adminInfo.getPhoneNumber());
                user.setFullName(adminInfo.getName());
                user.setPassword(passwordEncoder.encode(adminInfo.getPassword()));
                user.setRole(roleService.getRole(RoleConstant.ROLE_ADMIN.name()));
                user.setIsEnabled(true);
                user.setIsLocked(false);
                userRepository.save(user);

                log.info("Create admin user successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, userId));
    }

    @Override
    public Object getCurrentUser(String userId) {
        return null;
    }
}
