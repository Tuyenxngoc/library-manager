package com.example.librarymanager.domain.entity;

import com.example.librarymanager.constant.CardType;
import com.example.librarymanager.constant.Gender;
import com.example.librarymanager.domain.entity.common.UserDateAuditing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "readers",
        uniqueConstraints = @UniqueConstraint(name = "UN_READER_CARD_NUMBER", columnNames = "card_number"))
public class Reader extends UserDateAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reader_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false, length = 20)
    private CardType cardType; // Loại thẻ

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName; // Họ tên

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth; // Ngày sinh

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 6)
    private Gender gender; // Giới tính (Nam, Nữ)

    @Column(name = "avatar")
    private String avatar; // Ảnh đại diện (URL hoặc file path)

    @Column(name = "address")
    private String address; // Địa chỉ

    @Column(name = "phone_number", length = 15)
    private String phoneNumber; // Số điện thoại

    @Column(name = "card_number", nullable = false, unique = true, length = 20)
    private String cardNumber; // Số thẻ

    @Column(name = "password", nullable = false)
    private String password; // Mật khẩu

    @Column(name = "expiry_date")
    private LocalDate expiryDate; // Ngày hết hạn

    @Column(name = "active_flag", nullable = false)
    private Boolean activeFlag = Boolean.TRUE;

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BorrowReceipt> borrowReceipts = new ArrayList<>();
}
