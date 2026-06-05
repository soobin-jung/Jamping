package com.jam.ping.gear.service;

import com.jam.ping.category.domain.Category;
import com.jam.ping.category.repository.CategoryRepository;
import com.jam.ping.gear.domain.Gear;
import com.jam.ping.gear.repository.GearRepository;
import com.jam.ping.maker.domain.Maker;
import com.jam.ping.maker.repository.MakerRepository;
import com.jam.ping.user.domain.User;
import com.jam.ping.user.service.UserService;
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
public class GearService {

    private final GearRepository gearRepository;
    private final CategoryRepository categoryRepository;
    private final MakerRepository makerRepository;
    private final UserService userService;

    /**
     * 관리자 화면에서 사용하는 용품 목록을 카테고리, 메이커, 검색어, 페이지 조건으로 조회합니다.
     */
    public Page<Gear> getGears(Long categoryId, Long makerId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        if (categoryId == null && makerId == null && normalizedKeyword.isBlank()) {
            return gearRepository.findAll(pageable);
        }

        if (categoryId == null && makerId == null) {
            return gearRepository.findByNameContainingIgnoreCase(normalizedKeyword, pageable);
        }

        if (categoryId != null && makerId == null && normalizedKeyword.isBlank()) {
            return gearRepository.findByCategoryId(categoryId, pageable);
        }

        if (categoryId == null && makerId != null && normalizedKeyword.isBlank()) {
            return gearRepository.findByMakerId(makerId, pageable);
        }

        if (categoryId != null && makerId != null && normalizedKeyword.isBlank()) {
            return gearRepository.findByCategoryIdAndMakerId(categoryId, makerId, pageable);
        }

        if (categoryId != null && makerId == null) {
            return gearRepository.findByCategoryIdAndNameContainingIgnoreCase(categoryId, normalizedKeyword, pageable);
        }

        if (categoryId == null) {
            return gearRepository.findByMakerIdAndNameContainingIgnoreCase(makerId, normalizedKeyword, pageable);
        }

        return gearRepository.findByCategoryIdAndMakerIdAndNameContainingIgnoreCase(
                categoryId,
                makerId,
                normalizedKeyword,
                pageable
        );
    }

    /**
     * 요청한 ID의 용품을 조회합니다.
     */
    public Gear getGear(Long gearId) {
        return findGear(gearId);
    }

    /**
     * 새로운 용품을 생성합니다.
     */
    @Transactional
    public Gear createGear(
            String name,
            String link,
            String imageUrl,
            Long categoryId,
            Long makerId,
            String memo,
            Long actorUserId
    ) {
        Category category = findCategory(categoryId);
        Maker maker = findMaker(makerId);
        validateDuplicate(category.getId(), maker.getId(), name, null);
        User actorUser = userService.getActorUser(actorUserId);

        Gear gear = Gear.builder()
                .name(name)
                .link(link)
                .imageUrl(imageUrl)
                .category(category)
                .maker(maker)
                .createdBy(actorUser)
                .updatedBy(actorUser)
                .memo(memo)
                .build();

        return gearRepository.save(gear);
    }

    /**
     * 기존 용품 정보를 수정합니다.
     */
    @Transactional
    public Gear updateGear(
            Long gearId,
            String name,
            String link,
            String imageUrl,
            Long categoryId,
            Long makerId,
            String memo,
            Long actorUserId
    ) {
        Gear gear = findGear(gearId);
        Category category = findCategory(categoryId);
        Maker maker = findMaker(makerId);
        validateDuplicate(category.getId(), maker.getId(), name, gearId);
        User actorUser = userService.getActorUser(actorUserId);

        gear.update(name, link, imageUrl, category, maker, actorUser, memo);
        return gear;
    }

    /**
     * 선택한 용품을 삭제합니다.
     */
    @Transactional
    public void deleteGear(Long gearId) {
        Gear gear = findGear(gearId);
        gearRepository.delete(gear);
    }

    /**
     * 공통 조회 메서드로 용품을 찾고, 없으면 404 예외를 발생시킵니다.
     */
    private Gear findGear(Long gearId) {
        return gearRepository.findWithDetailsById(gearId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "용품을 찾을 수 없습니다."));
    }

    /**
     * 생성 또는 수정에 사용할 카테고리를 조회합니다.
     */
    private Category findCategory(Long categoryId) {
        if (categoryId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리는 필수입니다.");
        }

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."));
    }

    /**
     * 생성 또는 수정에 사용할 메이커를 조회합니다.
     */
    private Maker findMaker(Long makerId) {
        if (makerId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "메이커는 필수입니다.");
        }

        return makerRepository.findById(makerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메이커를 찾을 수 없습니다."));
    }
    private void validateDuplicate(Long categoryId, Long makerId, String name, Long gearId) {
        String normalizedName = name == null ? "" : name.trim();

        boolean duplicated = gearId == null
                ? gearRepository.existsByCategoryIdAndMakerIdAndNameIgnoreCase(categoryId, makerId, normalizedName)
                : gearRepository.existsByCategoryIdAndMakerIdAndNameIgnoreCaseAndIdNot(
                        categoryId,
                        makerId,
                        normalizedName,
                        gearId
                );

        if (duplicated) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 등록된 장비입니다.");
        }
    }
}
