package com.example.librarymanager.service;

import com.example.librarymanager.domain.entity.Role;

import java.util.List;

public interface RoleService {

    Role getRole(byte roleId);

    Role getRole(String name);

    void initRoles();

    List<Role> getRoles();

}