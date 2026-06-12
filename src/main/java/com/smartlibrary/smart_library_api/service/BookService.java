package com.smartlibrary.smart_library_api.service;

import com.smartlibrary.smart_library_api.dto.request.BookRequest;
import com.smartlibrary.smart_library_api.dto.response.BookResponse;
import com.smartlibrary.smart_library_api.entity.Book;
import com.smartlibrary.smart_library_api.entity.BookType;
import com.smartlibrary.smart_library_api.entity.DigitalBook;
import com.smartlibrary.smart_library_api.entity.PhysicalBook;
import com.smartlibrary.smart_library_api.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponse> getAllBooks() {
        return toBookResponseList(bookRepository.findAll());
    }

    public BookResponse getBookById(Long id) {
        Book book = findBookById(id);
        return toBookResponse(book);
    }

    public BookResponse createBook(BookRequest request) {
        validateBookRequest(request);

        Book book;

        if (request.getBookType() == BookType.PHYSICAL) {
            PhysicalBook physicalBook = new PhysicalBook();
            physicalBook.setShelfLocation(request.getShelfLocation());
            book = physicalBook;
        } else if (request.getBookType() == BookType.DIGITAL) {
            DigitalBook digitalBook = new DigitalBook();
            digitalBook.setFileUrl(request.getFileUrl());
            book = digitalBook;
        } else {
            throw new RuntimeException("Invalid book type");
        }

        setBookData(book, request);

        return toBookResponse(bookRepository.save(book));
    }

    public BookResponse updateBook(Long id, BookRequest request) {
        validateBookRequest(request);

        Book existingBook = findBookById(id);

        if (existingBook.getBookType() != request.getBookType()) {
            bookRepository.delete(existingBook);

            Book newBook;

            if (request.getBookType() == BookType.PHYSICAL) {
                PhysicalBook physicalBook = new PhysicalBook();
                physicalBook.setShelfLocation(request.getShelfLocation());
                newBook = physicalBook;
            } else if (request.getBookType() == BookType.DIGITAL) {
                DigitalBook digitalBook = new DigitalBook();
                digitalBook.setFileUrl(request.getFileUrl());
                newBook = digitalBook;
            } else {
                throw new RuntimeException("Invalid book type");
            }

            setBookData(newBook, request);

            return toBookResponse(bookRepository.save(newBook));
        }

        setBookData(existingBook, request);

        if (existingBook instanceof PhysicalBook physicalBook) {
            physicalBook.setShelfLocation(request.getShelfLocation());
        }

        if (existingBook instanceof DigitalBook digitalBook) {
            digitalBook.setFileUrl(request.getFileUrl());
        }

        return toBookResponse(bookRepository.save(existingBook));
    }

    public void deleteBook(Long id) {
        Book book = findBookById(id);
        bookRepository.delete(book);
    }

    public List<BookResponse> searchBooks(String keyword) {
        return toBookResponseList(
                bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbnContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword
                )
        );
    }

    public List<BookResponse> searchByTitle(String keyword) {
        return toBookResponseList(bookRepository.findByTitleContainingIgnoreCase(keyword));
    }

    public List<BookResponse> searchByAuthor(String keyword) {
        return toBookResponseList(bookRepository.findByAuthorContainingIgnoreCase(keyword));
    }

    public List<BookResponse> searchByIsbn(String keyword) {
        return toBookResponseList(bookRepository.findByIsbnContainingIgnoreCase(keyword));
    }

    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    private void setBookData(Book book, BookRequest request) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setBookType(request.getBookType());

        if (request.getAvailable() == null) {
            book.setAvailable(true);
        } else {
            book.setAvailable(request.getAvailable());
        }
    }

    private void validateBookRequest(BookRequest request) {
        if (request.getBookType() == BookType.PHYSICAL) {
            if (request.getShelfLocation() == null || request.getShelfLocation().isBlank()) {
                throw new RuntimeException("shelfLocation empty!");
            }
        }

        if (request.getBookType() == BookType.DIGITAL) {
            if (request.getFileUrl() == null || request.getFileUrl().isBlank()) {
                throw new RuntimeException("fileUrl empty!");
            }
        }
    }

    private BookResponse toBookResponse(Book book) {
        return new BookResponse(book);
    }

    private List<BookResponse> toBookResponseList(List<Book> books) {
        return books.stream()
                .map(BookResponse::new)
                .toList();
    }
}
