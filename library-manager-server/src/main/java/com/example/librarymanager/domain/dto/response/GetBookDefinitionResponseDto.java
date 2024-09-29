package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.domain.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetBookDefinitionResponseDto {

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private String createdBy;

    private String lastModifiedBy;

    private Boolean activeFlag;

    private long id;

    private String title;

    private Double price;

    private String isbn;

    private String publishingYear;

    private String edition;

    private Double referencePrice;

    private String publicationPlace;

    private String bookCode;

    private Integer pageCount;

    private String bookSize;

    private String parallelTitle;

    private String summary;

    private String subtitle;

    private String additionalMaterial;

    private String keywords;

    private String language;

    private String imageUrl;

    private String series;

    private String additionalInfo;

    private List<BaseEntityDto> authors = new ArrayList<>();

    private BaseEntityDto publisher;

    private BaseEntityDto bookSet;

    private BaseEntityDto category;

    private BaseEntityDto classificationSymbol;

    public GetBookDefinitionResponseDto(BookDefinition bookDefinition) {
        this.createdDate = bookDefinition.getCreatedDate();
        this.lastModifiedDate = bookDefinition.getLastModifiedDate();
        this.createdBy = bookDefinition.getCreatedBy();
        this.lastModifiedBy = bookDefinition.getLastModifiedBy();
        this.activeFlag = bookDefinition.getActiveFlag();

        this.id = bookDefinition.getId();
        this.title = bookDefinition.getTitle();
        this.price = bookDefinition.getPrice();
        this.isbn = bookDefinition.getIsbn();
        this.publishingYear = bookDefinition.getPublishingYear();
        this.edition = bookDefinition.getEdition();
        this.referencePrice = bookDefinition.getReferencePrice();
        this.publicationPlace = bookDefinition.getPublicationPlace();
        this.bookCode = bookDefinition.getBookCode();
        this.pageCount = bookDefinition.getPageCount();
        this.bookSize = bookDefinition.getBookSize();
        this.parallelTitle = bookDefinition.getParallelTitle();
        this.summary = bookDefinition.getSummary();
        this.subtitle = bookDefinition.getSubtitle();
        this.additionalMaterial = bookDefinition.getAdditionalMaterial();
        this.keywords = bookDefinition.getKeywords();
        this.language = bookDefinition.getLanguage();
        this.imageUrl = bookDefinition.getImageUrl();
        this.series = bookDefinition.getSeries();
        this.additionalInfo = bookDefinition.getAdditionalInfo();

        // Set category
        Category c = bookDefinition.getCategory();
        this.category = c != null ? new BaseEntityDto(c.getId(), c.getCategoryName()) : null;

        // Set authors
        List<BookAuthor> au = bookDefinition.getBookAuthors();
        if (au != null) {
            this.authors.addAll(au.stream()
                    .map(BookAuthor::getAuthor)
                    .map(author -> new BaseEntityDto(author.getId(), author.getFullName()))
                    .toList());
        }

        // Set publisher
        Publisher p = bookDefinition.getPublisher();
        this.publisher = p != null ? new BaseEntityDto(p.getId(), p.getName()) : null;

        // Set bookSet
        BookSet b = bookDefinition.getBookSet();
        this.bookSet = b != null ? new BaseEntityDto(b.getId(), b.getName()) : null;

        // Set classificationSymbol
        ClassificationSymbol cl = bookDefinition.getClassificationSymbol();
        if (cl != null) {
            this.classificationSymbol = new BaseEntityDto(cl.getId(), cl.getName());
        }
    }
}
