package com.hampcode.bookstoreapi.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hampcode.bookstoreapi.exception.BadRequestException;
import com.hampcode.bookstoreapi.exception.ResourceNotFoundException;
import com.hampcode.bookstoreapi.model.dto.BookFormDTO;
import com.hampcode.bookstoreapi.model.entity.Book;
import com.hampcode.bookstoreapi.repository.BookRepository;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class AdminBookService {

    private BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Page<Book> paginate(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book create(BookFormDTO bookFormDTO) {
        String slug = bookFormDTO.getSlug();
        boolean slugAlreadyExists = bookRepository.existsBySlug(slug);

        if (slugAlreadyExists) {
            throw new BadRequestException("El slug ya está siendo usado por otro libro.");
        }
        Book book = new Book();

        book.setTitle(bookFormDTO.getTitle());
        book.setDescription(bookFormDTO.getDescription());
        book.setPrice(bookFormDTO.getPrice());
        book.setSlug(bookFormDTO.getSlug());
        book.setCoverPath(bookFormDTO.getCoverPath());
        book.setFilePath(bookFormDTO.getFilePath());
        book.setCreatedAt(LocalDateTime.now());

        return bookRepository.save(book);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public Book update(Long id, BookFormDTO bookFormDTO) {
        Book bookFromDb = findById(id);

        String slug = bookFormDTO.getSlug();
        boolean slugAlreadyExists = bookRepository.existsBySlugAndIdNot(slug, id);

        if (slugAlreadyExists) {
            throw new BadRequestException("El slug ya está siendo usado por otro libro.");
        }

        bookFromDb.setTitle(bookFormDTO.getTitle());
        bookFromDb.setDescription(bookFormDTO.getDescription());
        bookFromDb.setPrice(bookFormDTO.getPrice());
        bookFromDb.setSlug(bookFormDTO.getSlug());
        bookFromDb.setCoverPath(bookFormDTO.getCoverPath());
        bookFromDb.setFilePath(bookFormDTO.getFilePath());

        bookFromDb.setUpdatedAt(LocalDateTime.now());

        return bookRepository.save(bookFromDb);
    }

    public void delete(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
    }

}
