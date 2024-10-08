package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.common.DataMailDto;
import com.example.librarymanager.domain.dto.request.auth.*;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.auth.LoginResponseDto;
import com.example.librarymanager.domain.dto.response.auth.TokenRefreshResponseDto;
import com.example.librarymanager.domain.entity.Reader;
import com.example.librarymanager.domain.entity.User;
import com.example.librarymanager.exception.BadRequestException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.exception.UnauthorizedException;
import com.example.librarymanager.repository.ReaderRepository;
import com.example.librarymanager.repository.UserRepository;
import com.example.librarymanager.security.CustomUserDetails;
import com.example.librarymanager.security.jwt.JwtTokenProvider;
import com.example.librarymanager.service.AuthService;
import com.example.librarymanager.service.EmailRateLimiterService;
import com.example.librarymanager.service.JwtTokenService;
import com.example.librarymanager.util.RandomPasswordUtil;
import com.example.librarymanager.util.SendMailUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    AuthenticationManager authenticationManager;

    JwtTokenProvider jwtTokenProvider;

    JwtTokenService jwtTokenService;

    EmailRateLimiterService emailRateLimiterService;

    MessageSource messageSource;

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    SendMailUtil sendMailUtil;

    ReaderRepository readerRepository;

    @Override
    public LoginResponseDto readerLogin(ReaderLoginRequestDto request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getCardNumber(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            if (customUserDetails.getCardNumber() == null) {
                throw new UnauthorizedException();
            }

            String accessToken = jwtTokenProvider.generateToken(customUserDetails, Boolean.FALSE);
            String refreshToken = jwtTokenProvider.generateToken(customUserDetails, Boolean.TRUE);

            jwtTokenService.saveAccessToken(accessToken, customUserDetails.getCardNumber());
            jwtTokenService.saveRefreshToken(refreshToken, customUserDetails.getCardNumber());

            return new LoginResponseDto(
                    accessToken,
                    refreshToken
            );
        } catch (AuthenticationException | UnauthorizedException e) {
            throw new UnauthorizedException(ErrorMessage.Auth.ERR_INCORRECT_USERNAME_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessage.ERR_EXCEPTION_GENERAL);
        }
    }

    @Override
    public LoginResponseDto adminLogin(AdminLoginRequestDto request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            if (customUserDetails.getUserId() == null) {
                throw new UnauthorizedException();
            }

            String accessToken = jwtTokenProvider.generateToken(customUserDetails, Boolean.FALSE);
            String refreshToken = jwtTokenProvider.generateToken(customUserDetails, Boolean.TRUE);

            jwtTokenService.saveAccessToken(accessToken, customUserDetails.getUserId());
            jwtTokenService.saveRefreshToken(refreshToken, customUserDetails.getUserId());

            return new LoginResponseDto(
                    accessToken,
                    refreshToken
            );
        } catch (AuthenticationException | UnauthorizedException e) {
            throw new UnauthorizedException(ErrorMessage.Auth.ERR_INCORRECT_USERNAME_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessage.ERR_EXCEPTION_GENERAL);
        }
    }

    @Override
    public CommonResponseDto logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        if (authentication != null && authentication.getPrincipal() != null) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            if (customUserDetails.getUserId() != null) {
                jwtTokenService.deleteTokens(customUserDetails.getUserId());
            }
            if (customUserDetails.getCardNumber() != null) {
                jwtTokenService.deleteTokens(customUserDetails.getCardNumber());
            }
        }

        SecurityContextLogoutHandler logout = new SecurityContextLogoutHandler();
        logout.logout(request, response, authentication);

        String message = messageSource.getMessage(SuccessMessage.Auth.LOGOUT, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public TokenRefreshResponseDto refresh(TokenRefreshRequestDto request) {
        String refreshToken = request.getRefreshToken();

        if (jwtTokenProvider.validateToken(refreshToken)) {
            String userId = jwtTokenProvider.extractSubjectFromJwt(refreshToken);

            if (userId != null && jwtTokenService.isRefreshTokenExists(refreshToken, userId)) {//Kiểm tra nếu có userId
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new BadRequestException(ErrorMessage.Auth.ERR_INVALID_REFRESH_TOKEN));
                CustomUserDetails userDetails = CustomUserDetails.create(user);

                String newAccessToken = jwtTokenProvider.generateToken(userDetails, Boolean.FALSE);
                String newRefreshToken = jwtTokenProvider.generateToken(userDetails, Boolean.TRUE);

                jwtTokenService.saveAccessToken(newAccessToken, user.getId());
                jwtTokenService.saveRefreshToken(newRefreshToken, user.getId());

                return new TokenRefreshResponseDto(newAccessToken, newRefreshToken);
            } else {//Nếu không kiểm tra cardNumber
                String cardNumber = jwtTokenProvider.extractClaimCardNumber(refreshToken);

                if (cardNumber != null && jwtTokenService.isRefreshTokenExists(refreshToken, cardNumber)) {
                    Reader reader = readerRepository.findByCardNumber(cardNumber)
                            .orElseThrow(() -> new BadRequestException(ErrorMessage.Auth.ERR_INVALID_REFRESH_TOKEN));

                    CustomUserDetails userDetails = CustomUserDetails.create(reader);

                    String newAccessToken = jwtTokenProvider.generateToken(userDetails, Boolean.FALSE);
                    String newRefreshToken = jwtTokenProvider.generateToken(userDetails, Boolean.TRUE);

                    jwtTokenService.saveAccessToken(newAccessToken, cardNumber);
                    jwtTokenService.saveRefreshToken(newRefreshToken, cardNumber);

                    return new TokenRefreshResponseDto(newAccessToken, newRefreshToken);

                }

            }

        }

        //Trả về lỗi refresh token không hợp lệ
        throw new BadRequestException(ErrorMessage.Auth.ERR_INVALID_REFRESH_TOKEN);
    }

    @Override
    public CommonResponseDto forgetPassword(ForgetPasswordRequestDto requestDto) {
        User user = userRepository.findByUsernameAndEmail(requestDto.getUsername(), requestDto.getEmail())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ACCOUNT));

        // Kiểm tra giới hạn thời gian gửi email
        if (emailRateLimiterService.isMailLimited(requestDto.getEmail())) {
            String message = messageSource.getMessage(ErrorMessage.User.RATE_LIMIT, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(message);
        }

        String newPassword = RandomPasswordUtil.random();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", requestDto.getUsername());
        properties.put("newPassword", newPassword);
        sendEmail(user.getEmail(), "Lấy lại mật khẩu", properties, "forgetPassword.html");

        emailRateLimiterService.setMailLimit(requestDto.getEmail(), 1, TimeUnit.MINUTES);

        String message = messageSource.getMessage(SuccessMessage.User.FORGET_PASSWORD, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto changePassword(ChangePasswordRequestDto requestDto, String username) {
        if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
            throw new BadRequestException(ErrorMessage.INVALID_REPEAT_PASSWORD);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME, username));

        boolean isCorrectPassword = passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword());
        if (!isCorrectPassword) {
            throw new BadRequestException(ErrorMessage.Auth.ERR_INCORRECT_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userRepository.save(user);

        Map<String, Object> properties = new HashMap<>();
        properties.put("currentTime", new Date());
        sendEmail(user.getEmail(), "Đổi mật khẩu thành công", properties, "changePassword.html");

        String message = messageSource.getMessage(SuccessMessage.User.CHANGE_PASSWORD, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    private void sendEmail(String to, String subject, Map<String, Object> properties, String templateName) {
        // Tạo DataMailDto
        DataMailDto mailDto = new DataMailDto();
        mailDto.setTo(to);
        mailDto.setSubject(subject);
        mailDto.setProperties(properties);

        // Gửi email bất đồng bộ
        CompletableFuture.runAsync(() -> {
            try {
                sendMailUtil.sendEmailWithHTML(mailDto, templateName);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }

}
