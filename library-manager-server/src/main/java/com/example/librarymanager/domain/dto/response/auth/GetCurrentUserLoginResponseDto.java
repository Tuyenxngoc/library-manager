package com.example.librarymanager.domain.dto.response.auth;

import com.example.librarymanager.constant.RoleConstant;
import com.example.librarymanager.domain.entity.Reader;
import com.example.librarymanager.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCurrentUserLoginResponseDto {

    private String roleName;

    public static GetCurrentUserLoginResponseDto create(User user) {
        return new GetCurrentUserLoginResponseDto(user.getRole().getName());
    }

    public static GetCurrentUserLoginResponseDto create(Reader reader) {
        return new GetCurrentUserLoginResponseDto(RoleConstant.ROLE_READER.name());
    }

}
