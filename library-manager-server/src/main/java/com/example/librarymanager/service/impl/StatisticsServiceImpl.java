package com.example.librarymanager.service.impl;

import com.example.librarymanager.domain.dto.response.statistics.BorrowStatisticsResponseDto;
import com.example.librarymanager.domain.dto.response.statistics.LibraryStatisticsResponseDto;
import com.example.librarymanager.repository.*;
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

    private final CartDetailRepository cartDetailRepository;

    private final BorrowReceiptRepository borrowReceiptRepository;

    @Override
    public LibraryStatisticsResponseDto getLibraryStatistics() {
        long publications = bookRepository.count();
        long authors = authorRepository.count();
        long publishers = publisherRepository.count();
        long readers = readerRepository.count();

        return new LibraryStatisticsResponseDto(publications, authors, publishers, readers);
    }

    @Override
    public BorrowStatisticsResponseDto getBorrowStatistics() {
        int borrowRequests = cartDetailRepository.countBorrowRequests();
        int currentlyBorrowed = borrowReceiptRepository.countCurrentlyBorrowed();
        int dueToday = borrowReceiptRepository.countDueToday();
        int overdue = borrowReceiptRepository.countOverdue();

        return new BorrowStatisticsResponseDto(borrowRequests, currentlyBorrowed, dueToday, overdue);
    }

}
