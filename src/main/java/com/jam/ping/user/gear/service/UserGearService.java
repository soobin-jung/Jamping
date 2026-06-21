package com.jam.ping.user.gear.service;

import com.jam.ping.category.domain.Category;
import com.jam.ping.category.repository.CategoryRepository;
import com.jam.ping.gear.domain.Gear;
import com.jam.ping.gear.repository.GearRepository;
import com.jam.ping.global.security.AuthUtils;
import com.jam.ping.maker.domain.Maker;
import com.jam.ping.maker.repository.MakerRepository;
import com.jam.ping.user.gear.domain.UserGear;
import com.jam.ping.user.gear.repository.UserGearRepository;
import com.jam.ping.user.main.domain.User;
import com.jam.ping.user.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserGearService {

    private final UserGearRepository userGearRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MakerRepository makerRepository;
    private final GearRepository gearRepository;

    public Page<UserGear> getUserGears(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        return userGearRepository.findByUserIdOrderByIdDesc(AuthUtils.getCurrentUserId(), pageable);
    }

    public Page<UserGear> getAllUserGearsForAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 50));
        return userGearRepository.findAllByOrderByIdDesc(pageable);
    }

    public UserGear getUserGear(Long userGearId) {
        return findUserGear(userGearId);
    }

    @Transactional
    public UserGear createUserGear(Long categoryId, Long makerId, Long gearId, String name, String memo) {
        User user = findUser(AuthUtils.getCurrentUserId());
        Category category = findCategory(categoryId);
        Maker maker = makerId == null ? null : findMaker(makerId);
        Gear gear = gearId == null ? null : findGear(gearId);

        String resolvedName = name == null ? null : name.trim();
        if (gear == null && (resolvedName == null || resolvedName.isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "기어를 선택하지 않은 경우 장비명은 필수입니다.");
        }

        return userGearRepository.save(UserGear.builder()
                .user(user)
                .category(category)
                .maker(maker)
                .gear(gear)
                .name(resolvedName)
                .memo(memo)
                .build());
    }

    @Transactional
    public UserGear updateUserGear(Long userGearId, Long categoryId, Long makerId, Long gearId, String name, String memo) {
        UserGear userGear = findUserGear(userGearId);
        checkOwnership(userGear);

        Category category = findCategory(categoryId);
        Maker maker = makerId == null ? null : findMaker(makerId);
        Gear gear = gearId == null ? null : findGear(gearId);

        String resolvedName = name == null ? null : name.trim();
        if (gear == null && (resolvedName == null || resolvedName.isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "기어를 선택하지 않은 경우 장비명은 필수입니다.");
        }

        userGear.update(category, maker, gear, resolvedName, memo);
        return userGear;
    }

    @Transactional
    public void deleteUserGear(Long userGearId) {
        UserGear userGear = findUserGear(userGearId);
        checkOwnership(userGear);
        userGearRepository.delete(userGear);
    }

    @Transactional
    public void deleteUserGearByAdmin(Long userGearId) {
        userGearRepository.delete(findUserGear(userGearId));
    }

    private UserGear findUserGear(Long userGearId) {
        return userGearRepository.findById(userGearId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장비 기록을 찾을 수 없습니다."));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    private Category findCategory(Long categoryId) {
        if (categoryId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리는 필수입니다.");
        }
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."));
    }

    private Maker findMaker(Long makerId) {
        return makerRepository.findById(makerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메이커를 찾을 수 없습니다."));
    }

    private Gear findGear(Long gearId) {
        return gearRepository.findById(gearId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "용품을 찾을 수 없습니다."));
    }

    private void checkOwnership(UserGear userGear) {
        if (!userGear.getUser().getId().equals(AuthUtils.getCurrentUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인의 장비 기록만 수정/삭제할 수 있습니다.");
        }
    }
}
