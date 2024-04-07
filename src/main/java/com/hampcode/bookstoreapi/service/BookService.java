package com.hampcode.bookstoreapi.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hampcode.bookstoreapi.exception.ResourceNotFoundException;
import com.hampcode.bookstoreapi.model.entity.Book;
import com.hampcode.bookstoreapi.repository.BookRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class BookService {

    private BookRepository bookRepository;

    public List<Book> findLast6Books() {
        return bookRepository.findTop6ByOrderByCreatedAtDesc();
    }

    public Book findBySlug(String slug) {
        return bookRepository.findBySlug(slug)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public Page<Book> paginate(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

}
