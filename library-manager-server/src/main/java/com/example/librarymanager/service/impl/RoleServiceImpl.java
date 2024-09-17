package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.RoleConstant;
import com.example.librarymanager.domain.entity.Role;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.RoleRepository;
import com.example.librarymanager.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;

    @Override
    public Role getRole(byte roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Role.ERR_NOT_FOUND_ID, roleId));
    }

    @Override
    public Role getRole(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Role.ERR_NOT_FOUND_NAME, name));
    }

    @Override
    public void initRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(RoleConstant.ROLE_SUPER_ADMIN.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_ADMIN.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_USER.name()));

            log.info("Initializing roles: SUPER ADMIN, ADMIN, USER");
        }
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

}
