package com.group.libraryapp.domain.user.loanhistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoanHistoryRepository extends JpaRepository<UserLoanHistory, Long> {

    // select * from user_loan_history where
//    boolean existsByBookNameAndIsReturn(String name, boolean isReturn);
    Page<UserLoanHistory> findAllByUserId(Long userId, Pageable pageable);
}
