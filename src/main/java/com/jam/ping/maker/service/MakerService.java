package com.jam.ping.maker.service;

import com.jam.ping.maker.domain.Maker;
import com.jam.ping.maker.repository.MakerRepository;
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
public class MakerService {

    private final MakerRepository makerRepository;

    /**
     * 관리자 화면에서 사용하는 메이커 목록을 검색 조건과 페이지 범위로 조회합니다.
     */
    public Page<Maker> getMakers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        if (normalizedKeyword.isBlank()) {
            return makerRepository.findAll(pageable);
        }

        return makerRepository.findByNameContainingIgnoreCaseOrNameEngContainingIgnoreCase(
                normalizedKeyword,
                normalizedKeyword,
                pageable
        );
    }

    /**
     * 요청한 ID의 메이커를 조회합니다.
     */
    public Maker getMaker(Long makerId) {
        return findMaker(makerId);
    }

    /**
     * 새로운 메이커를 생성합니다.
     */
    @Transactional
    public Maker createMaker(String name, String nameEng, String homepageUrl) {
        validateDuplicate(name, nameEng, null);

        Maker maker = Maker.builder()
                .name(name)
                .nameEng(nameEng)
                .homepageUrl(homepageUrl)
                .build();

        return makerRepository.save(maker);
    }

    /**
     * 기존 메이커 정보를 수정합니다.
     */
    @Transactional
    public Maker updateMaker(Long makerId, String name, String nameEng, String homepageUrl) {
        validateDuplicate(name, nameEng, makerId);

        Maker maker = findMaker(makerId);
        maker.update(name, nameEng, homepageUrl);
        return maker;
    }

    /**
     * 선택한 메이커를 삭제합니다.
     */
    @Transactional
    public void deleteMaker(Long makerId) {
        Maker maker = findMaker(makerId);
        makerRepository.delete(maker);
    }

    /**
     * 공통 조회 메서드로 메이커를 찾고, 없으면 404 예외를 발생시킵니다.
     */
    private Maker findMaker(Long makerId) {
        return makerRepository.findById(makerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메이커를 찾을 수 없습니다."));
    }

    private void validateDuplicate(String name, String nameEng, Long makerId) {
        String normalizedName = name == null ? "" : name.trim();
        String normalizedNameEng = nameEng == null ? "" : nameEng.trim();

        boolean duplicatedName = makerId == null
                ? makerRepository.existsByNameIgnoreCase(normalizedName)
                : makerRepository.existsByNameIgnoreCaseAndIdNot(normalizedName, makerId);

        if (duplicatedName) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 등록된 메이커명입니다.");
        }

        if (normalizedNameEng.isBlank()) {
            return;
        }

        boolean duplicatedNameEng = makerId == null
                ? makerRepository.existsByNameEngIgnoreCase(normalizedNameEng)
                : makerRepository.existsByNameEngIgnoreCaseAndIdNot(normalizedNameEng, makerId);

        if (duplicatedNameEng) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 등록된 영문 메이커명입니다.");
        }
    }
}
