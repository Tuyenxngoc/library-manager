package com.example.librarymanager.service.impl;

import com.example.librarymanager.domain.entity.Reader;
import com.example.librarymanager.domain.entity.User;
import com.example.librarymanager.repository.ReaderRepository;
import com.example.librarymanager.repository.UserRepository;
import com.example.librarymanager.security.CustomUserDetails;
import com.example.librarymanager.service.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {

    UserRepository userRepository;

    ReaderRepository readerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrCardNumber) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(usernameOrCardNumber).orElse(null);
        if (user != null) {
            return CustomUserDetails.create(user);
        } else {
            Reader reader = readerRepository.findByCardNumber(usernameOrCardNumber)
                    .orElseThrow(() -> new UsernameNotFoundException("Reader not found"));

            return CustomUserDetails.create(reader);
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return CustomUserDetails.create(user);
    }

    @Override
    public UserDetails loadUserByCardNumber(String cardNumber) throws UsernameNotFoundException {
        Reader reader = readerRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Reader not found"));

        return CustomUserDetails.create(reader);
    }

}