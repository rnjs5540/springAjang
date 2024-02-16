package com.group.libraryapp.domain.book;

import com.group.libraryapp.dto.book.request.BookCreateRequest;
import java.sql.Blob;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Getter;

@Getter
@Entity
public class Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id = null;
    @Column (nullable = false)
    private String name;
    private String writer;
    @Lob
    private String description;
    @Enumerated(EnumType.STRING)
    private Category category;
    private int price;
    private int stock;
    private LocalDate published_date;
    public enum Category {
        FANTASY, ROMANCE, HORROR
    }

    protected Book() {}
    public Book(BookCreateRequest request) {
        String name = request.getName();
        String writer = request.getWriter();
        String description = request.getDescription();
        Category category = request.getCategory();
        Integer price = request.getPrice();
        LocalDate published_date = request.getPublishedDate();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다.", name));
        }
        if (writer == null || writer.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 writer(%s)이 들어왔습니다.", writer));
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 description(%s)이 들어왔습니다.", description));
        }
        if (category == null) {
            throw new IllegalArgumentException("잘못된 category가 들어왔습니다.");
        }
        if (price == null || price < 0) {

            System.out.println(price);
            if (price == null)
                System.out.println("@@@@@@@@@@@@@@@@@@@@@");
            throw new IllegalArgumentException(String.format("잘못된 price(%s)가 들어왔습니다.", price));
        }
        if (published_date == null || published_date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(String.format("잘못된 publishedDate(%s)가 들어왔습니다.", price));
        }
        this.name = name;
        this.writer = writer;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = 1;
        this.published_date = published_date;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
