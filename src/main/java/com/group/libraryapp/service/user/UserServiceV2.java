package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.domain.user.buyhistory.UserBuyHistory;
import com.group.libraryapp.domain.user.buyhistory.UserBuyHistoryRepository;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.respond.UserResponse;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserServiceV2 {
    private final UserRepository userRepository;
    private final UserLoanHistoryRepository userLoanHistoryRepository;
    private final UserBuyHistoryRepository userBuyHistoryRepository;

    public UserServiceV2(UserRepository userRepository,
                         UserLoanHistoryRepository userLoanHistoryRepository,
                         UserBuyHistoryRepository userBuyHistoryRepository) {
        this.userRepository = userRepository;
        this.userLoanHistoryRepository = userLoanHistoryRepository;
        this.userBuyHistoryRepository = userBuyHistoryRepository;
    }

    public Long saveUser(UserCreateRequest request) {
        User user = userRepository.save(new User(request));
        return user.getId();
    }

    public UserResponse getUser(Long userId) {
        // userId의 유저 정보 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 userId입니다: " + userId));
        return new UserResponse(user);
    }

//    public void updateUser(UserUpdateRequest request) {
//        // select * from user where id = ?
//        // Optional<User>
//        User user = userRepository.findById(request.getId())
//                .orElseThrow(IllegalArgumentException::new);
//
//        user.updateName(request.getName());
//        userRepository.save(user);
//    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 userId입니다: " + userId));
        userRepository.delete(user);
    }

    public void chargeMoney(Long userId, Integer money) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 userId입니다: " + userId));
        user.chargeMoney(money);
        userRepository.save(user);
    }

    public Page<UserLoanHistory> getPaginatedLoanHistory(Integer page, Integer size, Long userId) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userLoanHistoryRepository.findAllByUserId(userId, pageRequest);
    }

    public Page<UserBuyHistory> getPaginatedBuyHistory(Integer page, Integer size, Long userId) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userBuyHistoryRepository.findAllByUserId(userId, pageRequest);
    }
}
