package com.smartlibrary.smart_library_api.dto.response;

import com.smartlibrary.smart_library_api.entity.Book;
import com.smartlibrary.smart_library_api.entity.BookType;
import com.smartlibrary.smart_library_api.entity.DigitalBook;
import com.smartlibrary.smart_library_api.entity.PhysicalBook;

public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Boolean available;
    private BookType bookType;
    private String info;
    private String shelfLocation;
    private String fileUrl;

    public BookResponse(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
        this.available = book.getAvailable();
        this.bookType = book.getBookType();
        this.info = book.getInfo();

        if (book instanceof PhysicalBook) {
            this.shelfLocation = ((PhysicalBook) book).getShelfLocation();
        }

        if (book instanceof DigitalBook) {
            this.fileUrl = ((DigitalBook) book).getFileUrl();
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public Boolean getAvailable() {
        return available;
    }

    public BookType getBookType() {
        return bookType;
    }

    public String getInfo() {
        return info;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}