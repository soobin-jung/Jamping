package com.jam.ping.user.repository;

import com.jam.ping.user.code.AuthProvider;
import com.jam.ping.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId);
}
