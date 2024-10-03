package com.example.librarymanager.service.impl;

import com.example.librarymanager.config.properties.AdminInfo;
import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.RoleConstant;
import com.example.librarymanager.constant.SortByDataConstant;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.pagination.PagingMeta;
import com.example.librarymanager.domain.dto.request.UserGroupRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetUserGroupResponseDto;
import com.example.librarymanager.domain.entity.Role;
import com.example.librarymanager.domain.entity.UserGroup;
import com.example.librarymanager.domain.entity.UserGroupRole;
import com.example.librarymanager.domain.mapper.UserGroupMapper;
import com.example.librarymanager.domain.specification.EntitySpecification;
import com.example.librarymanager.exception.BadRequestException;
import com.example.librarymanager.exception.ConflictException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.RoleRepository;
import com.example.librarymanager.repository.UserGroupRepository;
import com.example.librarymanager.service.LogService;
import com.example.librarymanager.service.UserGroupService;
import com.example.librarymanager.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {

    private static final String TAG = "Quản lý nhóm người dùng";

    private final UserGroupRepository userGroupRepository;

    private final RoleRepository roleRepository;

    private final MessageSource messageSource;

    private final UserGroupMapper userGroupMapper;

    private final LogService logService;

    private UserGroup getEntity(Long id) {
        return userGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.UserGroup.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public UserGroup initUserGroup(AdminInfo adminInfo) {
        if (userGroupRepository.count() == 0) {
            UserGroup userGroup = new UserGroup();
            userGroup.setName("Quản trị viên");
            userGroup.setCode("ADMIN");
            userGroup.setCreatedBy(adminInfo.getUsername());
            userGroup.setLastModifiedBy(adminInfo.getUsername());
            userGroup.getUserGroupRoles().addAll(
                    roleRepository.findAll().stream()
                            .filter(role -> !role.getName().equals(RoleConstant.ROLE_READER.name()))
                            .map(role -> new UserGroupRole(role, userGroup))
                            .collect(Collectors.toSet())
            );

            userGroupRepository.save(userGroup);
            System.out.println("Initializing user groups: Admin");

            return userGroup;
        }
        return null;
    }

    @Override
    public CommonResponseDto save(UserGroupRequestDto requestDto, String userId) {
        if (userGroupRepository.existsByCode(requestDto.getCode())) {
            throw new ConflictException(ErrorMessage.UserGroup.ERR_DUPLICATE_CODE);
        }

        UserGroup userGroup = userGroupMapper.toUserGroup(requestDto);

        for (byte roleId : requestDto.getRoleIds()) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Role.ERR_NOT_FOUND_ID, roleId));

            UserGroupRole userGroupRole = new UserGroupRole(role, userGroup);
            userGroup.getUserGroupRoles().add(userGroupRole);
        }

        userGroup.setActiveFlag(true);
        userGroupRepository.save(userGroup);

        logService.createLog(TAG, "Thêm", "Tạo nhóm người dùng mới: " + userGroup.getName(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new GetUserGroupResponseDto(userGroup));
    }

    @Override
    public CommonResponseDto update(Long id, UserGroupRequestDto requestDto, String userId) {
        UserGroup userGroup = getEntity(id);

        if (!Objects.equals(userGroup.getCode(), requestDto.getCode()) && userGroupRepository.existsByCode(requestDto.getCode())) {
            throw new ConflictException(ErrorMessage.UserGroup.ERR_DUPLICATE_CODE);
        }

        userGroup.setCode(requestDto.getCode());
        userGroup.setName(requestDto.getName());
        userGroup.setNotes(requestDto.getNotes());
        userGroupRepository.save(userGroup);

        logService.createLog(TAG, "Sửa", "Cập nhật nhóm người dùng id: " + userGroup.getId() + ", tên mới: " + userGroup.getName(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, new GetUserGroupResponseDto(userGroup));
    }

    @Override
    public CommonResponseDto delete(Long id, String userId) {
        UserGroup userGroup = getEntity(id);

        // Nếu nhóm người dùng có người dùng liên kết, không cho phép xóa
        if (!userGroup.getUsers().isEmpty()) {
            throw new BadRequestException(ErrorMessage.UserGroup.ERR_HAS_LINKED_USERS);
        }

        userGroupRepository.delete(userGroup);

        logService.createLog(TAG, "Xóa", "Xóa nhóm người dùng: " + userGroup.getName(), userId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<GetUserGroupResponseDto> findAll(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.USER_GROUP);

        Page<UserGroup> page = userGroupRepository.findAll(
                EntitySpecification.filterUserGroups(requestDto.getKeyword(), requestDto.getSearchBy(), requestDto.getActiveFlag()),
                pageable);

        List<GetUserGroupResponseDto> items = page.getContent().stream()
                .map(GetUserGroupResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.USER_GROUP, page);

        PaginationResponseDto<GetUserGroupResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public GetUserGroupResponseDto findById(Long id) {
        UserGroup userGroup = getEntity(id);
        return new GetUserGroupResponseDto(userGroup);
    }

    @Override
    public CommonResponseDto toggleActiveStatus(Long id, String userId) {
        UserGroup userGroup = getEntity(id);

        userGroup.setActiveFlag(!userGroup.getActiveFlag());
        userGroupRepository.save(userGroup);

        logService.createLog(TAG, "Sửa", "Thay đổi trạng thái nhóm người dùng: " + userGroup.getName(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, userGroup.getActiveFlag());
    }

}
