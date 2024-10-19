package com.example.librarymanager.service;

import com.example.librarymanager.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanager.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanager.domain.dto.request.NewsArticleRequestDto;
import com.example.librarymanager.domain.dto.response.CommonResponseDto;
import com.example.librarymanager.domain.dto.response.GetNewsArticleResponseDto;
import com.example.librarymanager.domain.entity.NewsArticle;
import org.springframework.web.multipart.MultipartFile;

public interface NewsArticleService {
    CommonResponseDto save(NewsArticleRequestDto requestDto, MultipartFile imageFile, String userId);

    CommonResponseDto update(Long id, NewsArticleRequestDto requestDto, MultipartFile imageFile, String userId);

    CommonResponseDto delete(Long id, String userId);

    PaginationResponseDto<NewsArticle> findAll(PaginationFullRequestDto requestDto);

    NewsArticle findById(Long id);

    CommonResponseDto toggleActiveStatus(Long id, String userId);

    PaginationResponseDto<GetNewsArticleResponseDto> getNewsArticles(PaginationFullRequestDto requestDto);

    NewsArticle getNewsArticleByTitleSlug(String titleSlug);
}
