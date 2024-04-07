package com.hampcode.bookstoreapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hampcode.bookstoreapi.model.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long idNot);

    Optional<Book> findBySlug(String slug);

    List<Book> findTop6ByOrderByCreatedAtDesc();

}
