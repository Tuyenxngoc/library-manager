package com.example.librarymanager.domain.entity;

import com.example.librarymanager.constant.BookCondition;
import com.example.librarymanager.domain.entity.common.DateAuditing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books",
        uniqueConstraints = @UniqueConstraint(name = "UN_BOOK_BOOK_CODE", columnNames = "book_code"))
public class Book extends DateAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "book_code")
    private String bookCode; // Số đăng ký cá biệt sách

    @Enumerated(EnumType.STRING)
    @Column(name = "book_condition")
    private BookCondition bookCondition; // Tình trạng sách

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private BookBorrow bookBorrow;// Phiếu mượn sách chi tiết

    @Column(name = "delete_flag", nullable = false)
    private Boolean deleteFlag = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_definition_id", foreignKey = @ForeignKey(name = "FK_BOOK_BOOK_DEFINITION_ID"), referencedColumnName = "book_definition_id", nullable = false)
    @JsonIgnore
    private BookDefinition bookDefinition;// Biên mục sách

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "import_receipt_id", foreignKey = @ForeignKey(name = "FK_BOOK_IMPORT_RECEIPT_ID"), referencedColumnName = "import_receipt_id", nullable = false)
    @JsonIgnore
    private ImportReceipt importReceipt;// Phiếu nhập

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "export_receipt_id", foreignKey = @ForeignKey(name = "FK_BOOK_EXPORT_RECEIPT_ID"), referencedColumnName = "export_receipt_id")
    @JsonIgnore
    private ExportReceipt exportReceipt;// Phiếu xuất
}
