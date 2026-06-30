package com.jam.ping.api.user.main.repository;

import com.jam.ping.api.user.main.code.AuthProvider;
import com.jam.ping.api.user.main.code.UserRole;
import com.jam.ping.api.user.main.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId);

    Optional<User> findByEmail(String email);

    Page<User> findAllByOrderByIdDesc(Pageable pageable);

    Page<User> findByRoleOrderByIdDesc(UserRole role, Pageable pageable);

    Page<User> findByNicknameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByIdDesc(
            String nicknameKeyword,
            String emailKeyword,
            Pageable pageable
    );

    Page<User> findByRoleAndNicknameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCaseOrderByIdDesc(
            UserRole nicknameRole,
            String nicknameKeyword,
            UserRole emailRole,
            String emailKeyword,
            Pageable pageable
    );
}
