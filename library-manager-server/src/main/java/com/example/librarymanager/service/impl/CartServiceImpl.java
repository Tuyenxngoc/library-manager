package com.example.librarymanager.service.impl;

import com.example.librarymanager.constant.ErrorMessage;
import com.example.librarymanager.constant.SuccessMessage;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetCartDetailResponseDto;
import com.example.librarymanager.domain.entity.*;
import com.example.librarymanager.exception.BadRequestException;
import com.example.librarymanager.exception.NotFoundException;
import com.example.librarymanager.repository.BookDefinitionRepository;
import com.example.librarymanager.repository.CartDetailRepository;
import com.example.librarymanager.repository.CartRepository;
import com.example.librarymanager.repository.ReaderRepository;
import com.example.librarymanager.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    @Value("${cartDetail.borrowTimeHours}")
    private int borrowTimeHours;

    @Value("${cartDetail.maxBooks}")
    private int maxBooksInCart;

    private final CartRepository cartRepository;

    private final CartDetailRepository cartDetailRepository;

    private final ReaderRepository readerRepository;

    private final BookDefinitionRepository bookDefinitionRepository;

    private final MessageSource messageSource;

    private Cart getEntity(String cardNumber) {
        return cartRepository.findByReaderCardNumber(cardNumber).orElseGet(() -> {
            Cart cart = new Cart();
            Reader reader = readerRepository.findByCardNumber(cardNumber)
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Reader.ERR_NOT_FOUND_CARD_NUMBER, cardNumber));
            cart.setReader(reader);
            return cartRepository.save(cart);
        });
    }

    @Override
    public List<GetCartDetailResponseDto> getCartDetails(String cardNumber) {
        Cart cart = getEntity(cardNumber);
        return cartDetailRepository.getAllByCartId(cart.getId());
    }

    @Override
    public CommonResponseDto addToCart(String cardNumber, Long bookId) {
        Cart cart = getEntity(cardNumber);
        LocalDateTime now = LocalDateTime.now();

        long count = cart.getCartDetails().stream()
                .filter(cartDetail -> cartDetail.getBorrowTo().isAfter(now))
                .count();

        if (count >= maxBooksInCart) {
            throw new BadRequestException(ErrorMessage.Cart.ERR_MAX_BOOKS_IN_CART);
        }

        BookDefinition bookDefinition = bookDefinitionRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BookDefinition.ERR_NOT_FOUND_ID, bookId));
        Book book = null;

        for (Book bookCandidate : bookDefinition.getBooks()) {
            // Kiểm tra nếu sách có phiếu xuất (ExportReceipt), nếu có thì bỏ qua sách này
            if (bookCandidate.getExportReceipt() != null) {
                continue;
            }

            // Kiểm tra nếu sách có phiếu mượn chưa trả
            List<BookBorrow> bookBorrows = bookCandidate.getBookBorrows();
            boolean hasUnreturnedBorrow = bookBorrows.stream().anyMatch(bookBorrow -> !bookBorrow.isReturned());
            if (hasUnreturnedBorrow) {
                continue;
            }

            // Kiểm tra xem sách đã được giữ chỗ (CartDetail) trong khoảng thời gian hiện tại
            List<CartDetail> cartDetails = bookCandidate.getCartDetails();
            boolean isBookReserved = cartDetails.stream().anyMatch(cartDetail -> cartDetail.getBorrowTo().isAfter(now));
            if (isBookReserved) {
                continue;
            }

            // Gán sách khả dụng cho biến `book` và thoát vòng lặp
            book = bookCandidate;
            break;
        }

        if (book == null) {
            throw new NotFoundException(ErrorMessage.BookDefinition.ERR_NOT_FOUND_ID, bookId);
        }

        CartDetail cartDetail = new CartDetail();
        cartDetail.setBook(book);
        cartDetail.setCart(cart);

        cartDetail.setBorrowFrom(now);
        cartDetail.setBorrowTo(now.plusHours(borrowTimeHours));

        cartDetailRepository.save(cartDetail);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto removeFromCart(String cardNumber, Long cartDetailId) {
        Cart cart = getEntity(cardNumber);

        // Tìm chi tiết giỏ hàng và xóa
        cart.getCartDetails().removeIf(detail -> detail.getId().equals(cartDetailId));
        cartRepository.save(cart);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto clearCart(String cardNumber) {
        Cart cart = getEntity(cardNumber);

        // Xóa tất cả chi tiết giỏ hàng
        cart.getCartDetails().clear();

        // Lưu lại giỏ hàng trống vào cơ sở dữ liệua
        cartRepository.save(cart);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

}