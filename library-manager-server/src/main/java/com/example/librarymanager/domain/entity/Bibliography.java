package com.example.librarymanager.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bibliographies")
public class Bibliography {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Mã biên mục (ID)

    @Column(name = "title", nullable = false)
    private String title;  // Tiêu đề của biên mục

    @Column(name = "author", nullable = false)
    private String author;  // Tác giả

    @Column(name = "publisher")
    private String publisher;  // Nhà xuất bản

    @Column(name = "publish_date")
    private LocalDate publishDate;  // Ngày xuất bản

    @Column(name = "description", length = 2000)
    private String description;  // Mô tả về biên mục (không bắt buộc)

    private String parallelTitle; // Nhan đề song song

    private String subtitle; // Phụ đề

    private String coAuthors; // Đồng tác giả

    private String publicationPlace; // Nơi xuất bản

    private String classificationSign; // Ký hiệu phân loại

    private String authorSign; // Ký hiệu tác giả

    private String publishingYear; // Năm xuất bản

    private String edition; // Lần xuất bản

    private Integer pageCount; // Số trang

    private Double price; // Giá bán

    private Double referencePrice; // Giá tham khảo

    private String bookSize; // Khổ sách (cm)

    private String bookGroup; // Nhóm loại sách

    private String isbn; // Mã ISBN

    @Lob
    private String summary; // Tóm tắt

    private String keywords; // Từ khóa tìm kiếm

    private String language; // Ngôn ngữ

    private String series; // Tùng thư

    private String additionalMaterial; // Tài liệu đi kèm

    private String additionalInfo; // Thông tin khác

}
