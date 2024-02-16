package com.group.libraryapp.dto.book.request;

import com.group.libraryapp.domain.book.Book.Category;
import java.sql.Blob;
import java.time.LocalDate;
import javax.persistence.Lob;
import lombok.Getter;

@Getter
public class BookUpdateRequest {
    private String name;
    private String writer;
    @Lob
    private Blob description;
    private Category category;
    private Integer price;

}
