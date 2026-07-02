package com.jam.ping.api.gear.maker.service;

import com.jam.ping.api.gear.maker.domain.Maker;
import com.jam.ping.api.gear.maker.dto.MakerDto;
import com.jam.ping.api.gear.maker.repository.MakerRepository;
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

    public Page<MakerDto> getMakers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        if (normalizedKeyword.isBlank()) {
            return makerRepository.findAll(pageable).map(MakerDto::from);
        }

        return makerRepository.findByNameContainingIgnoreCaseOrNameEngContainingIgnoreCase(
                normalizedKeyword,
                normalizedKeyword,
                pageable
        ).map(MakerDto::from);
    }

    public MakerDto getMaker(Long makerId) {
        return MakerDto.from(findMaker(makerId));
    }

    @Transactional
    public MakerDto createMaker(String name, String nameEng, String homepageUrl) {
        validateDuplicate(name, nameEng, null);
        return MakerDto.from(makerRepository.save(Maker.create(name, nameEng, homepageUrl)));
    }

    @Transactional
    public MakerDto updateMaker(Long makerId, String name, String nameEng, String homepageUrl) {
        validateDuplicate(name, nameEng, makerId);
        Maker maker = findMaker(makerId);
        maker.update(name, nameEng, homepageUrl);
        return MakerDto.from(maker);
    }

    @Transactional
    public void deleteMaker(Long makerId) {
        makerRepository.delete(findMaker(makerId));
    }

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
