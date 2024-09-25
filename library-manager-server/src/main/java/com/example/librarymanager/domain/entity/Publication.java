package com.example.librarymanager.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "publications")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Mã ấn phẩm (ID)

    @Column(name = "title", nullable = false)
    private String title;  // Tên ấn phẩm

    @Column(name = "release_date")
    private LocalDate releaseDate;  // Ngày phát hành

    @Column(name = "publisher")
    private String publisher;  // Nhà xuất bản

    @Column(name = "description", length = 2000)
    private String description;  // Mô tả về ấn phẩm (có thể không bắt buộc)

}
