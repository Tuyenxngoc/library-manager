package com.example.librarymanager.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "publishers")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id")
    private Long id;  // Mã nhà xuất bản

    @Column(name = "name", nullable = false)
    private String name;  // Tên nhà xuất bản

    @Column(name = "address")
    private String address;  // Địa chỉ nhà xuất bản

    @Column(name = "city")
    private String city;  // Tỉnh/Thành phố

    @Column(name = "notes")
    private String notes;  // Ghi chú

}
