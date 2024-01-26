package com.group.libraryapp.domain.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { // user의 키는 Long

    Optional<User> findByName(String name);
}
