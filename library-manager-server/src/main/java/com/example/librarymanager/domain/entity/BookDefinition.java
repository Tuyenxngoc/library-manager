package com.example.librarymanager.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_definitions")
public class BookDefinition {//Biên mục

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_definition_id")
    private Long id;  // Mã biên mục (ID)

    @Column(name = "title", nullable = false)
    private String title;  // Nhan đề của biên mục

    @OneToMany(mappedBy = "bookDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BookAuthor> bookAuthors;  // Tác giả

    //Kí hiệu phân loại...todo

    private String publishingYear; // Năm xuất bản

    private Double price; // Giá bán

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", foreignKey = @ForeignKey(name = "FK_BOOK_DEFINITION_PUBLISHER_ID"), referencedColumnName = "publisher_id", nullable = false)
    @JsonIgnore
    private Publisher publisher;  // Nhà xuất bản

    //Kí hiệu tên sách

    @Column(name = "edition")
    private String edition; // Lần xuất bản

    @Column(name = "reference_price")
    private Double referencePrice; // Giá tham khảo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_group_id", foreignKey = @ForeignKey(name = "FK_BOOK_DEFINITION_CATEGORY_GROUP_ID"), referencedColumnName = "category_group_id", nullable = false)
    @JsonIgnore
    private CategoryGroup categoryGroup; // Nhóm loại sách

    @Column(name = "publication_place")
    private String publicationPlace; // Nơi xuất bản Hà Nội, vv

    @Column(name = "author_sign")
    private String authorSign; // Ký hiệu tác giả

    @Column(name = "page_count")
    private Integer pageCount; // Số trang

    @Column(name = "book_size")
    private String bookSize; // Khổ sách (cm)

    @Column(name = "parallel_title")
    private String parallelTitle; // Nhan đề song song

    //Đồng tác giả...todo

    @Lob
    @Column(name = "summary")
    private String summary; // Tóm tắt

    @Column(name = "subtitle")
    private String subtitle; // Phụ đề

    @Column(name = "additional_material")
    private String additionalMaterial; // Tài liệu đi kèm

    @Column(name = "keywords")
    private String keywords; // Từ khóa tìm kiếm

    @Column(name = "isbn")
    private String isbn; // Mã ISBN

    @Column(name = "language")
    private String language; // Ngôn ngữ

    @Column(name = "series")
    private String series; // Tùng thư

    @Column(name = "additional_info")
    private String additionalInfo; // Thông tin khác

    @OneToMany(mappedBy = "bookDefinition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Book> books = new ArrayList<>();

}
