package com.hampcode.bookstoreapi.controller;

import com.hampcode.bookstoreapi.model.dto.BookFormDTO;
import com.hampcode.bookstoreapi.model.entity.Book;
import com.hampcode.bookstoreapi.service.AdminBookService;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/books")
@AllArgsConstructor
@RestController
public class AdminBookController {

    private AdminBookService adminBookService;

    @GetMapping
    public List<Book> list() {
        return adminBookService.findAll();
    }

    @GetMapping("/page")
    public Page<Book> paginate(@PageableDefault(size = 5, sort = "title") Pageable pageable) {
        return adminBookService.paginate(pageable);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Book create(@RequestBody @Validated BookFormDTO bookFormDTO) {
        return adminBookService.create(bookFormDTO);
    }

    @GetMapping("/{id}")
    public Book get(@PathVariable Long id) {
        return adminBookService.findById(id);
    }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody @Validated BookFormDTO bookFormDTO) {
        return adminBookService.update(id, bookFormDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        adminBookService.delete(id);
    }

}
