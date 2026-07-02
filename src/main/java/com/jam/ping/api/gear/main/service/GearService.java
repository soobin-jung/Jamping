package com.jam.ping.api.gear.main.service;

import com.jam.ping.api.gear.category.domain.Category;
import com.jam.ping.api.gear.category.repository.CategoryRepository;
import com.jam.ping.api.gear.main.domain.Gear;
import com.jam.ping.api.gear.main.dto.GearDto;
import com.jam.ping.api.gear.main.repository.GearRepository;
import com.jam.ping.api.gear.maker.domain.Maker;
import com.jam.ping.api.gear.maker.repository.MakerRepository;
import com.jam.ping.global.exception.BadRequestException;
import com.jam.ping.global.exception.ConflictException;
import com.jam.ping.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GearService {

    private final GearRepository gearRepository;
    private final CategoryRepository categoryRepository;
    private final MakerRepository makerRepository;

    public Page<GearDto> getGears(Long categoryId, Long makerId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        if (categoryId == null && makerId == null && normalizedKeyword.isBlank()) {
            return gearRepository.findAll(pageable).map(GearDto::from);
        }
        if (categoryId == null && makerId == null) {
            return gearRepository.findByNameContainingIgnoreCase(normalizedKeyword, pageable).map(GearDto::from);
        }
        if (categoryId != null && makerId == null && normalizedKeyword.isBlank()) {
            return gearRepository.findByCategoryId(categoryId, pageable).map(GearDto::from);
        }
        if (categoryId == null && makerId != null && normalizedKeyword.isBlank()) {
            return gearRepository.findByMakerId(makerId, pageable).map(GearDto::from);
        }
        if (categoryId != null && makerId != null && normalizedKeyword.isBlank()) {
            return gearRepository.findByCategoryIdAndMakerId(categoryId, makerId, pageable).map(GearDto::from);
        }
        if (categoryId != null && makerId == null) {
            return gearRepository.findByCategoryIdAndNameContainingIgnoreCase(categoryId, normalizedKeyword, pageable).map(GearDto::from);
        }
        if (categoryId == null) {
            return gearRepository.findByMakerIdAndNameContainingIgnoreCase(makerId, normalizedKeyword, pageable).map(GearDto::from);
        }

        return gearRepository.findByCategoryIdAndMakerIdAndNameContainingIgnoreCase(
                categoryId, makerId, normalizedKeyword, pageable
        ).map(GearDto::from);
    }

    public GearDto getGear(Long gearId) {
        return GearDto.from(findGear(gearId));
    }

    @Transactional
    public GearDto createGear(String name, String link, String imageUrl, Long categoryId, Long makerId, String memo) {
        Category category = findCategory(categoryId);
        Maker maker = findMaker(makerId);
        validateDuplicate(category.getId(), maker.getId(), name, null);

        return GearDto.from(gearRepository.save(Gear.create(name, link, imageUrl, category, maker, memo)));
    }

    @Transactional
    public GearDto updateGear(Long gearId, String name, String link, String imageUrl, Long categoryId, Long makerId, String memo) {
        Gear gear = findGear(gearId);
        Category category = findCategory(categoryId);
        Maker maker = findMaker(makerId);
        validateDuplicate(category.getId(), maker.getId(), name, gearId);

        gear.update(name, link, imageUrl, category, maker, memo);
        return GearDto.from(gear);
    }

    @Transactional
    public void deleteGear(Long gearId) {
        gearRepository.delete(findGear(gearId));
    }

    private Gear findGear(Long gearId) {
        return gearRepository.findWithDetailsById(gearId)
                .orElseThrow(() -> new NotFoundException("용품을 찾을 수 없습니다."));
    }

    private Category findCategory(Long categoryId) {
        if (categoryId == null) {
            throw new BadRequestException("카테고리는 필수입니다.");
        }
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다."));
    }

    private Maker findMaker(Long makerId) {
        if (makerId == null) {
            throw new BadRequestException("메이커는 필수입니다.");
        }
        return makerRepository.findById(makerId)
                .orElseThrow(() -> new NotFoundException("메이커를 찾을 수 없습니다."));
    }

    private void validateDuplicate(Long categoryId, Long makerId, String name, Long gearId) {
        String normalizedName = name == null ? "" : name.trim();

        boolean duplicated = gearId == null
                ? gearRepository.existsByCategoryIdAndMakerIdAndNameIgnoreCase(categoryId, makerId, normalizedName)
                : gearRepository.existsByCategoryIdAndMakerIdAndNameIgnoreCaseAndIdNot(categoryId, makerId, normalizedName, gearId);

        if (duplicated) {
            throw new ConflictException("이미 등록된 장비입니다.");
        }
    }
}
