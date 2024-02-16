package com.group.libraryapp.domain.user.buyhistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBuyHistoryRepository extends JpaRepository<UserBuyHistory, Long> {
    Page<UserBuyHistory> findAllByUserId(Long userId, Pageable pageable);

}
