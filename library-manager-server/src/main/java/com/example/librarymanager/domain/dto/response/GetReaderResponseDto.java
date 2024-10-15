package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.constant.CardStatus;
import com.example.librarymanager.constant.CardType;
import com.example.librarymanager.constant.Gender;
import com.example.librarymanager.domain.dto.common.DateAuditingDto;
import com.example.librarymanager.domain.entity.Reader;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetReaderResponseDto extends DateAuditingDto {

    private final long id;

    private final CardType cardType;

    private final String fullName;

    private final LocalDate dateOfBirth;

    private final Gender gender;

    private final String avatar;

    private final String address;

    private final String phoneNumber;

    private final String cardNumber;

    private final LocalDate expiryDate;

    private final CardStatus status;

    private final long currentBorrowedBooks;

    private final long libraryVisitCount;

    public GetReaderResponseDto(Reader reader) {
        this.createdDate = reader.getCreatedDate();
        this.lastModifiedDate = reader.getLastModifiedDate();
        this.id = reader.getId();
        this.cardType = reader.getCardType();
        this.fullName = reader.getFullName();
        this.dateOfBirth = reader.getDateOfBirth();
        this.gender = reader.getGender();
        this.avatar = reader.getAvatar();
        this.address = reader.getAddress();
        this.phoneNumber = reader.getPhoneNumber();
        this.cardNumber = reader.getCardNumber();
        this.expiryDate = reader.getExpiryDate();
        this.status = reader.getStatus();

        // Tính số sách đang mượn (số lượng BorrowReceipts chưa được trả)
        this.currentBorrowedBooks = reader.getBorrowReceipts().stream()
                .flatMap(receipt -> receipt.getBookBorrows().stream()) // Lấy tất cả các sách từ các phiếu mượn
                .filter(bookBorrow -> !bookBorrow.isReturned()) // Chỉ lấy những sách chưa trả
                .count(); // Đếm số lượng sách chưa trả

        // Tính số lượt vào thư viện
        this.libraryVisitCount = reader.getLibraryVisits().size();
    }

}
