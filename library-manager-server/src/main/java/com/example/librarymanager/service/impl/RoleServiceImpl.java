package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.RoleConstant;
import com.example.librarymanager.domain.entity.Role;
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
    public void initRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(RoleConstant.ROLE_MANAGE_AUTHOR.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_MANAGE_BOOK.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_MANAGE_BOOK_DEFINITION.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_MANAGE_BOOK_SET.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_MANAGE_CATEGORY.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_MANAGE_CATEGORY_GROUP.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_MANAGE_CLASSIFICATION_SYMBOL.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_MANAGE_IMPORT_RECEIPT.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_MANAGE_LOG.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_MANAGE_NEWS_ARTICLE.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_MANAGE_PUBLISHER.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_MANAGE_ROLE.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_MANAGE_USER.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_READER.name()));

            log.info("Initializing roles");
        }
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

}
