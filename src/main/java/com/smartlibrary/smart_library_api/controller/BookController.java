package com.smartlibrary.smart_library_api.controller;

import com.smartlibrary.smart_library_api.dto.request.BookRequest;
import com.smartlibrary.smart_library_api.dto.response.ApiResponse;
import com.smartlibrary.smart_library_api.dto.response.BookResponse;
import com.smartlibrary.smart_library_api.service.BookService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin("*")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ApiResponse<List<BookResponse>> getAllBooks() {
        return ApiResponse.success(200, bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ApiResponse<BookResponse> getBookById(@PathVariable Long id) {
        return ApiResponse.success(200, bookService.getBookById(id));
    }

    @PostMapping
    public ApiResponse<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        return ApiResponse.success(200, bookService.createBook(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request
    ) {
        return ApiResponse.success(200, bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ApiResponse.success(200, "Book deleted successfully");
    }

    @GetMapping("/search")
    public ApiResponse<List<BookResponse>> searchBooks(@RequestParam String keyword) {
        return ApiResponse.success(200, bookService.searchBooks(keyword));
    }

    @GetMapping("/search/title")
    public ApiResponse<List<BookResponse>> searchByTitle(@RequestParam String keyword) {
        return ApiResponse.success(200, bookService.searchByTitle(keyword));
    }

    @GetMapping("/search/author")
    public ApiResponse<List<BookResponse>> searchByAuthor(@RequestParam String keyword) {
        return ApiResponse.success(200, bookService.searchByAuthor(keyword));
    }

    @GetMapping("/search/isbn")
    public ApiResponse<List<BookResponse>> searchByIsbn(@RequestParam String keyword) {
        return ApiResponse.success(200, bookService.searchByIsbn(keyword));
    }
}