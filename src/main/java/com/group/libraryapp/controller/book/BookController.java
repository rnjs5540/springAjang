package com.group.libraryapp.controller.book;

import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.dto.book.request.BookCreateRequest;
import com.group.libraryapp.dto.book.request.BookUpdateRequest;
import com.group.libraryapp.dto.book.request.StockUpdateRequest;
import com.group.libraryapp.dto.book.respond.BookResponse;
import com.group.libraryapp.service.book.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/book")
    public ResponseEntity<Long> saveBook(@RequestBody BookCreateRequest request) {
        Long bookId = bookService.saveBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookId);
    }

    @GetMapping("/book")
    public BookResponse<Book> getPaginatedBooks(
            @RequestParam("page") Integer page,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) Book.Category category,
            @RequestParam(value = "writer", required = false) String writer,
            @RequestParam(value = "sortCriteria", required = false) BookService.SortCriteria sortCriteria) {
        Integer pageSize = 10;
        return bookService.getBookList(page, pageSize, name, category, writer, sortCriteria);
    }

    @PutMapping("/book/{bookId}")
    public void updateBook(@PathVariable Long bookId,
                           @RequestBody BookUpdateRequest request) {
        bookService.updateBook(bookId, request);
    }

    @DeleteMapping("book/{bookId}")
    public void deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
    }

    @PatchMapping("book/{bookId}/stock")
    public void updateStock(@PathVariable Long bookId,
                            @RequestBody StockUpdateRequest request) {
        bookService.updateStock(bookId, request);
    }

    @PostMapping("/book/{bookId}/loan")
    public void loanBook(@PathVariable Long bookId,
                         @RequestHeader("Authorization") Long userId) {
        bookService.loanBook(bookId, userId);
    }

    @PostMapping("/book/return/{loanId}")
    public void returnBook(@PathVariable Long loanId,
                           @RequestHeader("Authorization") Long userId) {
        bookService.returnBook(loanId, userId);
    }

    @PostMapping("book/{bookId}/buy")
    public void buyBook(@PathVariable Long bookId,
                         @RequestHeader("Authorization") Long userId) {
        bookService.buyBook(bookId, userId);
    }
}
