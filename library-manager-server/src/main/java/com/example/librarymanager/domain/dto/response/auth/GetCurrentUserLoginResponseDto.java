package com.example.librarymanager.domain.dto.response.auth;

import com.example.librarymanager.constant.RoleConstant;
import com.example.librarymanager.domain.entity.Reader;
import com.example.librarymanager.domain.entity.User;
import com.example.librarymanager.domain.entity.UserGroupRole;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCurrentUserLoginResponseDto {

    private String name;

    private Set<String> roleNames;

    public static GetCurrentUserLoginResponseDto create(User user) {
        GetCurrentUserLoginResponseDto responseDto = GetCurrentUserLoginResponseDto.builder()
                .name(user.getFullName())
                .roleNames(new HashSet<>())
                .build();

        Set<UserGroupRole> roles = user.getUserGroup().getUserGroupRoles();
        for (UserGroupRole role : roles) {
            responseDto.getRoleNames().add(role.getRole().getName());
        }

        return responseDto;
    }

    public static GetCurrentUserLoginResponseDto create(Reader reader) {
        GetCurrentUserLoginResponseDto responseDto = GetCurrentUserLoginResponseDto.builder()
                .name(reader.getFullName())
                .roleNames(new HashSet<>())
                .build();
        responseDto.getRoleNames().add(RoleConstant.ROLE_READER.name());

        return responseDto;
    }

}
