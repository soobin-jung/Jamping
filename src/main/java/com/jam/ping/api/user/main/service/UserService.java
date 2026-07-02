package com.jam.ping.api.user.main.service;

import com.jam.ping.api.user.main.code.UserRole;
import com.jam.ping.api.user.main.domain.User;
import com.jam.ping.api.user.main.dto.UserDto;
import com.jam.ping.api.user.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public Page<UserDto> getUsers(String keyword, UserRole role, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        if (role == null && normalizedKeyword.isBlank()) {
            return userRepository.findAllByOrderByIdDesc(pageable).map(UserDto::from);
        }
        if (role != null && normalizedKeyword.isBlank()) {
            return userRepository.findByRoleOrderByIdDesc(role, pageable).map(UserDto::from);
        }
        if (role == null) {
            return userRepository.findByNicknameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByIdDesc(
                    normalizedKeyword, normalizedKeyword, pageable
            ).map(UserDto::from);
        }

        return userRepository.findByRoleAndNicknameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCaseOrderByIdDesc(
                role, normalizedKeyword, role, normalizedKeyword, pageable
        ).map(UserDto::from);
    }

    public UserDto getUser(Long userId) {
        return UserDto.from(findUser(userId));
    }

    public User getActorUser(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증된 사용자 정보를 찾을 수 없습니다.");
        }
        return findUser(userId);
    }

    @Transactional
    public UserDto updateUserRole(Long userId, UserRole role) {
        User user = findUser(userId);

        if (user.getRole() == role) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 동일한 권한을 가진 사용자입니다.");
        }

        user.changeRole(role);
        return UserDto.from(user);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}
