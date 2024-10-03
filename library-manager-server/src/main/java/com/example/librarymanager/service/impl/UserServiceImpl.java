package com.example.librarymanager.service.impl;

import com.example.librarymanager.config.properties.AdminInfo;
import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.UserRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.auth.GetCurrentUserLoginResponseDto;
import com.example.librarymanager.domain.entity.Reader;
import com.example.librarymanager.domain.entity.User;
import com.example.librarymanager.domain.entity.UserGroup;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.ReaderRepository;
import com.example.librarymanager.repository.UserGroupRepository;
import com.example.librarymanager.repository.UserRepository;
import com.example.librarymanager.security.CustomUserDetails;
import com.example.librarymanager.service.RoleService;
import com.example.librarymanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final MessageSource messageSource;

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final UserGroupRepository userGroupRepository;

    private final PasswordEncoder passwordEncoder;

    private final ReaderRepository readerRepository;

    @Override
    public void initAdmin(AdminInfo adminInfo, UserGroup userGroup) {
        if (userRepository.count() == 0) {
            try {
                User user = new User();
                user.setUsername(adminInfo.getUsername());
                user.setEmail(adminInfo.getEmail());
                user.setPhoneNumber(adminInfo.getPhoneNumber());
                user.setFullName(adminInfo.getName());
                user.setPassword(passwordEncoder.encode(adminInfo.getPassword()));
                user.setUserGroup(userGroup);
                user.setIsEnabled(true);
                userRepository.save(user);

                log.info("Create admin user successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public GetCurrentUserLoginResponseDto getCurrentUser(CustomUserDetails userDetails) {
        if (userDetails.getUserId() != null) {
            User user = userRepository.findById(userDetails.getUserId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, userDetails.getUserId()));

            return GetCurrentUserLoginResponseDto.create(user);
        } else if (userDetails.getCardNumber() != null) {
            Reader reader = readerRepository.findByCardNumber(userDetails.getCardNumber())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Reader.ERR_NOT_FOUND_CARD_NUMBER, userDetails.getCardNumber()));

            return GetCurrentUserLoginResponseDto.create(reader);
        }
        return null;
    }

    @Override
    public CommonResponseDto save(UserRequestDto requestDto, String userId) {
        return null;
    }

    @Override
    public CommonResponseDto update(String id, UserRequestDto requestDto, String userId) {
        return null;
    }

    @Override
    public CommonResponseDto delete(String id, String userId) {
        return null;
    }

    @Override
    public PaginationResponseDto<User> findAll(PaginationFullRequestDto requestDto) {
        return null;
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, id));
    }
}
