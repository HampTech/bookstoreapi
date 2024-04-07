package com.hampcode.bookstoreapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.hampcode.bookstoreapi.model.Book;
import com.hampcode.bookstoreapi.repository.BookRepository;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/books")
@AllArgsConstructor
public class AdminBookController {

    private final BookRepository bookRepository;
    

    @GetMapping
    public List<Book> list() {
        return bookRepository.findAll();
    }

    @GetMapping("/page")
    public Page<Book> paginate(@PageableDefault(size = 5, sort = "title") Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Book create(@RequestBody Book bookFormDTO) {
        return bookRepository.save(bookFormDTO);
    }

    @GetMapping("/{id}")
    public Book get(@PathVariable Long id) {
        Optional<Book> existingBook = bookRepository.findById(id);
        return existingBook.orElseThrow(null);
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book bookFormDTO) {
        Optional<Book> existingBook = bookRepository.findById(id);
        return existingBook.map(book -> {
            book.setTitle(bookFormDTO.getTitle());
            book.setDescription(bookFormDTO.getDescription());
            book.setPrice(bookFormDTO.getPrice());
            book.setSlug(bookFormDTO.getSlug());
            book.setCoverPath(bookFormDTO.getCoverPath());
            book.setFilePath(bookFormDTO.getFilePath());
            book.setCreatedAt(LocalDateTime.now());
            return bookRepository.save(book);
        }).orElseThrow(null);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
