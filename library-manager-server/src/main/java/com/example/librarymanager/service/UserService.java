package com.example.librarymanager.service;

import com.example.librarymanager.config.properties.AdminInfo;
import com.example.librarymanager.domain.entity.User;

public interface UserService {

    void initAdmin(AdminInfo adminInfo);

    User getUserById(String userId);

    Object getCurrentUser(String userId);

}
