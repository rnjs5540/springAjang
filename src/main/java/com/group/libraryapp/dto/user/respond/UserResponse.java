package com.group.libraryapp.dto.user.respond;

import com.group.libraryapp.domain.user.User;
import java.util.Optional;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String name;
    private final Integer age;
    private final String email;
    private final Integer money;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.age = user.getAge();
        this.email = user.getEmail();
        this.money = user.getMoney();
    }

}
