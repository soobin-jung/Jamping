package com.jam.ping.gear.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jam.ping.category.domain.Category;
import com.jam.ping.category.repository.CategoryRepository;
import com.jam.ping.gear.domain.Gear;
import com.jam.ping.gear.repository.GearRepository;
import com.jam.ping.maker.domain.Maker;
import com.jam.ping.maker.repository.MakerRepository;
import com.jam.ping.user.domain.User;
import com.jam.ping.user.service.UserService;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class GearServiceTest {

    @Mock
    private GearRepository gearRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MakerRepository makerRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private GearService gearService;

    @Test
    void createGearThrowsWhenCompositeKeyIsDuplicated() {
        Category category = categoryWithId(3L);
        Maker maker = makerWithId(7L);

        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));
        when(makerRepository.findById(7L)).thenReturn(Optional.of(maker));
        when(gearRepository.existsByCategoryIdAndMakerIdAndNameIgnoreCase(3L, 7L, "Alpha Tent")).thenReturn(true);

        assertThrows(
                ResponseStatusException.class,
                () -> gearService.createGear("Alpha Tent", null, null, 3L, 7L, null, 1L)
        );

        verify(userService, never()).getActorUser(1L);
        verify(gearRepository, never()).save(org.mockito.ArgumentMatchers.any(Gear.class));
    }

    @Test
    void updateGearThrowsWhenCompositeKeyIsDuplicated() {
        Category category = categoryWithId(3L);
        Maker maker = makerWithId(7L);
        Gear gear = org.mockito.Mockito.mock(Gear.class);
        User actor = org.mockito.Mockito.mock(User.class);

        when(gearRepository.findWithDetailsById(11L)).thenReturn(Optional.of(gear));
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));
        when(makerRepository.findById(7L)).thenReturn(Optional.of(maker));
        when(gearRepository.existsByCategoryIdAndMakerIdAndNameIgnoreCaseAndIdNot(3L, 7L, "Alpha Tent", 11L))
                .thenReturn(true);
        when(userService.getActorUser(1L)).thenReturn(actor);

        assertThrows(
                ResponseStatusException.class,
                () -> gearService.updateGear(11L, "Alpha Tent", null, null, 3L, 7L, null, 1L)
        );

        verify(gear, never()).update("Alpha Tent", null, null, category, maker, actor, null);
    }

    private static Category categoryWithId(Long id) {
        Category category = Category.builder()
                .name("Tent")
                .build();
        setId(category, id);
        return category;
    }

    private static Maker makerWithId(Long id) {
        Maker maker = Maker.builder()
                .name("Snow Peak")
                .build();
        setId(maker, id);
        return maker;
    }

    private static void setId(Object target, Long id) {
        try {
            Field field = target.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(target, id);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
