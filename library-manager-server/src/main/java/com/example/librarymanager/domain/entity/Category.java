package com.example.librarymanager.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_group_id", foreignKey = @ForeignKey(name = "FK_CATEGORY_CATEGORY_GROUP_ID"), referencedColumnName = "category_group_id", nullable = false)
    @JsonIgnore
    private CategoryGroup categoryGroup;

}
