package com.hampcode.bookstoreapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hampcode.bookstoreapi.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
