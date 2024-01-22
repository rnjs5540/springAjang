package com.group.libraryapp.service.user;

import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.respond.UserResponse;
import com.group.libraryapp.repository.user.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(UserCreateRequest request) {
        userRepository.saveUser(request.getName(), request.getAge());
    }

    public List<UserResponse> getUsers() {
        return userRepository.getUsers();
    }

    public void updateUser(UserUpdateRequest request) {
    // 컨트롤러가 받은걸 그냥 넘겨받을거기 때문에 @RequestBody 필요없음
        boolean isUserNotExist = userRepository.isUserNotExist(request.getId());
        if (isUserNotExist) {
            throw new IllegalArgumentException();
        }

        userRepository.updateUserName(request.getName(), request.getId());
    }

    public void deleteUser(String name) {
        boolean isUserNotExist = userRepository.isUserNotExist(name);
        if (isUserNotExist) {
            throw new IllegalArgumentException();
        }

        userRepository.deleteUser(name);
    }
}
