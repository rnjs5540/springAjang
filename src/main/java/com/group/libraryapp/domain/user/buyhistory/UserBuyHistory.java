package com.group.libraryapp.domain.user.buyhistory;

import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.user.User;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserBuyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "created_date")
    private LocalDate createdDate;

    protected UserBuyHistory(){}
    public UserBuyHistory(User user, Book book, LocalDate created_date) {
        this.user = user;
        this.book = book;
        if (created_date != null) {
            this.createdDate = created_date;
        } else {
            this.createdDate = LocalDate.now();
        }
    }
}
