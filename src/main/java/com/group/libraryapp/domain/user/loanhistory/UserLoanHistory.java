package com.group.libraryapp.domain.user.loanhistory;

import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.user.User;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity
public class UserLoanHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id = null;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Enumerated(EnumType.STRING)
    private Type type;
    private LocalDate created_date;

    public enum Type {
        LOANED, RETURNED
    }

//    private boolean isReturn;

    protected UserLoanHistory(){}
    public UserLoanHistory(User user, Book book, LocalDate created_date) {
        this.user = user;
        this.book = book;
        this.type = Type.LOANED;
        if (created_date != null) {
            this.created_date = created_date;
        } else {
            this.created_date = LocalDate.now();
        }
    }

    public void doReturn() {
        this.type = Type.RETURNED;
    }


}
