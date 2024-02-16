package com.group.libraryapp.controller.user;

import com.group.libraryapp.domain.user.buyhistory.UserBuyHistory;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import com.group.libraryapp.dto.user.request.ChargeMoneyRequest;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.respond.UserResponse;
import com.group.libraryapp.service.user.UserServiceV2;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserServiceV2 userService;

    public UserController(UserServiceV2 userService) {
        this.userService = userService;
    }

    /*
    * User CRUD
    */
    @PostMapping("/user")
    public ResponseEntity<Long> saveUser(@RequestBody UserCreateRequest request) {
        Long id = userService.saveUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @GetMapping("/user")
    public UserResponse getUser(@RequestHeader("Authorization") Long userId) {
        return userService.getUser(userId);
    }

//    @PutMapping("/user")
//    public void updateUser(@RequestBody UserUpdateRequest request) {
//        userService.updateUser(request);
//    }

    @DeleteMapping("/user")
    public void deleteUser(@RequestHeader("Authorization") Long userId) {
        System.out.println("딜리트");
        userService.deleteUser(userId);
    }


    @PostMapping("/user/money")
    public void chargeMoney(@RequestHeader("Authorization") Long userId, @RequestBody ChargeMoneyRequest request) {
        userService.chargeMoney(userId, request.getMoney());
    }

    @GetMapping("/user/book/loan")
    public Page<UserLoanHistory> getPaginatedLoanHistory(
            @RequestHeader("Authorization") Long userId, @RequestParam("page") Integer page) {
        Integer pageSize = 10; // 일단 페이지 size는 10으로 하드코딩
        return userService.getPaginatedLoanHistory(page, pageSize, userId);
    }

    @GetMapping("/user/book/buy")
    public Page<UserBuyHistory> getPaginatedBuyHistory(
            @RequestHeader("Authorization") Long userId, @RequestParam("page") Integer page) {
        Integer pageSize = 10; // 일단 페이지 size는 10으로 하드코딩
        return userService.getPaginatedBuyHistory(page, pageSize, userId);
    }

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<String> handle(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
