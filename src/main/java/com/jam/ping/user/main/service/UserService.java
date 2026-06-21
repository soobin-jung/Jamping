package com.jam.ping.user.main.service;

import com.jam.ping.user.main.code.UserRole;
import com.jam.ping.user.main.domain.User;
import com.jam.ping.user.main.repository.UserRepository;
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

    /**
     * 관리자 화면에서 사용자 목록을 검색 조건과 페이지 정보로 조회합니다.
     */
    public Page<User> getUsers(String keyword, UserRole role, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        if (role == null && normalizedKeyword.isBlank()) {
            return userRepository.findAllByOrderByIdDesc(pageable);
        }

        if (role != null && normalizedKeyword.isBlank()) {
            return userRepository.findByRoleOrderByIdDesc(role, pageable);
        }

        if (role == null) {
            return userRepository.findByNicknameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByIdDesc(
                    normalizedKeyword,
                    normalizedKeyword,
                    pageable
            );
        }

        return userRepository.findByRoleAndNicknameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCaseOrderByIdDesc(
                role,
                normalizedKeyword,
                role,
                normalizedKeyword,
                pageable
        );
    }

    /**
     * 관리자 화면에서 단건 사용자 정보를 조회합니다.
     */
    public User getUser(Long userId) {
        return findUser(userId);
    }

    /**
     * 감사 이력에 사용할 사용자 정보를 조회합니다.
     */
    public User getActorUser(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증된 사용자 정보를 찾을 수 없습니다.");
        }

        return findUser(userId);
    }

    /**
     * 관리자 화면에서 사용자 권한을 변경합니다.
     */
    @Transactional
    public User updateUserRole(Long userId, UserRole role) {
        User user = findUser(userId);

        if (user.getRole() == role) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 동일한 권한을 가진 사용자입니다.");
        }

        user.changeRole(role);
        return user;
    }

    /**
     * 관리자/공통 로직에서 사용할 사용자 단건 조회 메서드입니다.
     */
    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}
