package com.group.libraryapp.dto.book.respond;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;

public class BookResponse<Book> {
    private int totalPage;
    private List<Book> data;

    public BookResponse(Page<Book> p) {
        this.totalPage = p.getTotalPages();
        this.data = p.getContent();
    }
}
