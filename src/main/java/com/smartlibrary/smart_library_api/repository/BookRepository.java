package com.smartlibrary.smart_library_api.repository;

import com.smartlibrary.smart_library_api.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String keyword);

    List<Book> findByAuthorContainingIgnoreCase(String keyword);

    List<Book> findByIsbnContainingIgnoreCase(String keyword);

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbnContainingIgnoreCase(
            String title,
            String author,
            String isbn
    );
}