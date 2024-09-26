package com.example.librarymanager.domain.dto.response.auth;

import com.example.librarymanager.constant.RoleConstant;
import com.example.librarymanager.domain.entity.Reader;
import com.example.librarymanager.domain.entity.User;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCurrentUserLoginResponseDto {

    private String name;

    private String roleName;

    public static GetCurrentUserLoginResponseDto create(User user) {
        return GetCurrentUserLoginResponseDto.builder()
                .name(user.getFullName())
                .roleName(user.getRole().getName())
                .build();
    }

    public static GetCurrentUserLoginResponseDto create(Reader reader) {
        return GetCurrentUserLoginResponseDto.builder()
                .name(reader.getFullName())
                .roleName(RoleConstant.ROLE_READER.name())
                .build();
    }

}
