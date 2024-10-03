package com.example.librarymanager.service;

import com.example.librarymanager.config.properties.AdminInfo;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.UserGroupRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetUserGroupResponseDto;
import com.example.librarymanager.domain.entity.UserGroup;

public interface UserGroupService {
    UserGroup initUserGroup(AdminInfo adminInfo);

    CommonResponseDto save(UserGroupRequestDto requestDto, String userId);

    CommonResponseDto update(Long id, UserGroupRequestDto requestDto, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<GetUserGroupResponseDto> findAll(PaginationFullRequestDto requestDto);

    GetUserGroupResponseDto findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id, String userId);
}
