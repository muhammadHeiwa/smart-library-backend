package com.smartlibrary.smart_library_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_books")
@Inheritance(strategy = InheritanceType.JOINED)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private String isbn;

    private Boolean available = true;

    @Enumerated(EnumType.STRING)
    private BookType bookType;

    public String getInfo() {
        return title + " - " + author + " - " + isbn;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void changeStatus(Boolean status) {
        this.available = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public BookType getBookType() {
        return bookType;
    }

    public void setBookType(BookType bookType) {
        this.bookType = bookType;
    }
}