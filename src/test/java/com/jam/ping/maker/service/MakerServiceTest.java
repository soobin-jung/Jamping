package com.jam.ping.maker.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jam.ping.maker.domain.Maker;
import com.jam.ping.maker.repository.MakerRepository;
import com.jam.ping.user.domain.User;
import com.jam.ping.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class MakerServiceTest {

    @Mock
    private MakerRepository makerRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MakerService makerService;

    @Test
    void createMakerThrowsWhenNameIsDuplicated() {
        when(makerRepository.existsByNameIgnoreCase("Snow Peak")).thenReturn(true);

        assertThrows(
                ResponseStatusException.class,
                () -> makerService.createMaker("Snow Peak", "snowpeak", "https://example.com", 1L)
        );

        verify(userService, never()).getActorUser(1L);
        verify(makerRepository, never()).save(org.mockito.ArgumentMatchers.any(Maker.class));
    }

    @Test
    void updateMakerThrowsWhenNameEngIsDuplicated() {
        User actor = org.mockito.Mockito.mock(User.class);
        Maker maker = org.mockito.Mockito.mock(Maker.class);

        when(makerRepository.existsByNameIgnoreCaseAndIdNot("Snow Peak", 10L)).thenReturn(false);
        when(makerRepository.existsByNameEngIgnoreCaseAndIdNot("snowpeak", 10L)).thenReturn(true);
        when(makerRepository.findWithUsersById(10L)).thenReturn(Optional.of(maker));
        when(userService.getActorUser(1L)).thenReturn(actor);

        assertThrows(
                ResponseStatusException.class,
                () -> makerService.updateMaker(10L, "Snow Peak", "snowpeak", "https://example.com", 1L)
        );

        verify(maker, never()).update("Snow Peak", "snowpeak", "https://example.com", actor);
    }
}
