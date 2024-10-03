package com.example.librarymanager.repository;

import com.example.librarymanager.domain.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRoleRepository extends JpaRepository<UserGroup, Long> {
}
