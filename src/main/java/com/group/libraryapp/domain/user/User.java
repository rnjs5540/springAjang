package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.buyhistory.UserBuyHistory;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;

@Entity
@Getter
public class User {
     @Id  // 프라이머리 키
     @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동생성(auto-increased)
    private Long id = null;

    @Column(nullable = false, length = 20)  // name varchar(20)
    // 객체의 name과 테이블의 name을 매핑
    private String name;
    private Integer age;
    private Integer money;
    private String email;
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    // 주인은 userLoanHistory -> user쪽에 mappedBy를 붙여줌. userLoanHistory의 user필드에 묶여있다
    private List<UserLoanHistory> userLoanHistories = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserBuyHistory> userBuyHistories = new ArrayList<>();

    private boolean isValidEmail(String email) {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    protected User() {}

    public User(UserCreateRequest request) {
        String name = request.getName();
        Integer age = request.getAge();
        String email = request.getEmail();
        String password = request.getPassword();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다.", name));
        }
        if (age == null || age < 0) {
            throw new IllegalArgumentException(String.format("잘못된 age(%d)가 들어왔습니다.", age));
        }
        if (email == null || !this.isValidEmail(email)) {
            throw new IllegalArgumentException(String.format("잘못된 email(%s)이 들어왔습니다.", email));
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException(String.format("잘못된 password(%s)가 들어왔습니다.", password));
        }
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
        this.money = 0;
    }

    public void chargeMoney(Integer money) {
        this.money += money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

//    public void loanBook(String bookName) {
//        this.userLoanHistories.add(new UserLoanHistory(this, bookName));
//    }

//    public void returnBook(String bookName) {
//        UserLoanHistory targetHistory = this.userLoanHistories.stream()
//                .filter(history -> history.getBookName().equals(bookName))
//                .findFirst()
//                .orElseThrow(IllegalArgumentException::new);
//        targetHistory.doReturn();
//    }
}
