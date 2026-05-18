package com.smartlibrary.smart_library_api.dto.request;

import com.smartlibrary.smart_library_api.entity.BookType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String isbn;

    @NotNull
    private BookType bookType;

    private Boolean available;

    private String shelfLocation;

    private String fileUrl;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public BookType getBookType() {
        return bookType;
    }

    public Boolean getAvailable() {
        return available;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}