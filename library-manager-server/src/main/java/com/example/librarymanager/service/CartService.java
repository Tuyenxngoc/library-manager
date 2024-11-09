package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetCartDetailResponseDto;

import java.util.List;

public interface CartService {

    List<GetCartDetailResponseDto> getCartDetails(String cardNumber);

    CommonResponseDto addToCart(String cardNumber, Long bookId);

    CommonResponseDto removeFromCart(String cardNumber, Long cartDetailId);

    CommonResponseDto clearCart(String cardNumber);

}
