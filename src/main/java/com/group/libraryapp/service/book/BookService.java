package com.group.libraryapp.service.book;

import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.book.Book.Category;
import com.group.libraryapp.domain.book.BookRepository;
import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.domain.user.buyhistory.UserBuyHistory;
import com.group.libraryapp.domain.user.buyhistory.UserBuyHistoryRepository;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import com.group.libraryapp.dto.book.request.BookCreateRequest;
import com.group.libraryapp.dto.book.request.BookUpdateRequest;
import com.group.libraryapp.dto.book.request.StockUpdateRequest;
import com.group.libraryapp.dto.book.respond.BookResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserLoanHistoryRepository userLoanHistoryRepository;
    private final UserBuyHistoryRepository userBuyHistoryRepository;
    public enum SortCriteria {
        STOCK_ASC, STOCK_DESC, PRICE_ASC, PRICE_DESC,
        DATE_ASC, DATE_DESC, NAME_ASC, NAME_DESC
    }

    public BookService(
            BookRepository bookRepository,
            UserLoanHistoryRepository userLoanHistoryRepository,
            UserRepository userRepository,
            UserBuyHistoryRepository userBuyHistoryRepository){
        this.bookRepository = bookRepository;
        this.userLoanHistoryRepository = userLoanHistoryRepository;
        this.userRepository = userRepository;
        this.userBuyHistoryRepository = userBuyHistoryRepository;
    }

    @Transactional
    public Long saveBook(BookCreateRequest request) {
        Optional<Book> book = bookRepository.findByName(request.getName());

        if (book.isPresent())
            throw new IllegalArgumentException("이미 존재하는 책입니다.");
        else {
            Book newBook = new Book(request);
            bookRepository.save(new Book(request));
            return newBook.getId();
        }
    }

    public BookResponse<Book> getBookList(
            int page, int pageSize,
            String name, Category category, String writer, SortCriteria sortCriteria) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        Specification<Book> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(criteriaBuilder.equal(root.get("name"), name));
            }
            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }
            if (writer != null) {
                predicates.add(criteriaBuilder.equal(root.get("writer"), writer));
            }
            if (sortCriteria != null) {
                predicates.add(criteriaBuilder.equal(root.get("sortCriteria"), sortCriteria));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Book> result = bookRepository.findAll(spec, pageRequest);

        return new BookResponse<Book>(result);
    }

    @Transactional
    public void updateBook(Long bookId, BookUpdateRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 bookId입니다."));

        String name = request.getName();
        String writer = request.getWriter();
        String description = request.getDescription().toString();
        Category category = request.getCategory();
        Integer price = request.getPrice();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다.", name));
        }
        if (writer == null || writer.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 writer(%s)이 들어왔습니다.", writer));
        }
        if (description == null || description.toString().isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 description(%s)이 들어왔습니다.", description));
        }
        if (category == null) {
            throw new IllegalArgumentException("잘못된 category가 들어왔습니다.");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException(String.format("잘못된 price(%s)가 들어왔습니다.", price));
        }

        book.setName(name);
        book.setWriter(writer);
        book.setDescription(description);
        book.setCategory(category);
        book.setPrice(price);

        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 bookId입니다."));

        bookRepository.delete(book);
    }

    @Transactional
    public void updateStock(Long bookId, StockUpdateRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 bookId입니다."));

        Integer stock = request.getStock();
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException(String.format("잘못된 stock입니다.(%d)", stock));
        }

        book.setStock(stock);
    }

    @Transactional
    public void loanBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        if (book.getStock() < 1) {
            throw new IllegalArgumentException("책의 재고가 부족합니다.");
        }

        // user에서 하게 하지말고 Service에서 하는게 더 깔끔하지 않음?
        UserLoanHistory userLoanHistory = new UserLoanHistory(user, book, null);
        book.setStock(book.getStock() - 1);
        userLoanHistoryRepository.save(userLoanHistory);
    }

    @Transactional
    public void returnBook(Long loanId, Long userId) {
        UserLoanHistory userLoanHistory = userLoanHistoryRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대출기록입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 이것두
        Book book = userLoanHistory.getBook();
        book.setStock(book.getStock() + 1);
        userLoanHistory.doReturn();
    }

    @Transactional
    public void buyBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        if (book.getStock() < 1) {
            throw new IllegalArgumentException(String.format("책의 재고가 부족합니다.(남은재고: %d개)", book.getStock()));
        }
        if (user.getMoney() < book.getPrice()) {
            throw new IllegalArgumentException(String.format("잔액이 부족합니다.(잔액: %d원)", user.getMoney()));
        }

        // 이것두
        UserBuyHistory userBuyHistory = new UserBuyHistory(user, book, null);
        book.setStock(book.getStock() - 1);
        user.setMoney(user.getMoney() - book.getPrice());
        userBuyHistoryRepository.save(userBuyHistory);
    }
}
