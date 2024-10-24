package com.example.librarymanager.service.impl;

import com.example.librarymanager.domain.dto.response.statistics.LibraryStatisticsResponseDto;
import com.example.librarymanager.repository.AuthorRepository;
import com.example.librarymanager.repository.BookRepository;
import com.example.librarymanager.repository.PublisherRepository;
import com.example.librarymanager.repository.ReaderRepository;
import com.example.librarymanager.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final PublisherRepository publisherRepository;

    private final ReaderRepository readerRepository;

    @Override
    public LibraryStatisticsResponseDto getLibraryStatistics() {
        long publications = bookRepository.count();
        long authors = authorRepository.count();
        long publishers = publisherRepository.count();
        long readers = readerRepository.count();

        return new LibraryStatisticsResponseDto(publications, authors, publishers, readers);
    }

}
