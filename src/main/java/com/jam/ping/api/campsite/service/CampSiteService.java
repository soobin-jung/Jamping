package com.jam.ping.api.campsite.service;

import com.jam.ping.api.campsite.domain.CampSite;
import com.jam.ping.api.campsite.dto.CampSiteDto;
import com.jam.ping.api.campsite.repository.CampSiteRepository;
import com.jam.ping.api.regeion.district.code.DistrictCode;
import com.jam.ping.api.regeion.region.code.RegionCode;
import java.time.LocalTime;
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
public class CampSiteService {

    private final CampSiteRepository campSiteRepository;

    public Page<CampSiteDto> getCampSites(String keyword, RegionCode regionCode, DistrictCode districtCode, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String kw = keyword == null ? "" : keyword.trim();

        if (districtCode != null) {
            return kw.isBlank()
                    ? campSiteRepository.findByDistrictCodeOrderByIdDesc(districtCode, pageable).map(CampSiteDto::from)
                    : campSiteRepository.findByDistrictCodeAndNameContainingIgnoreCaseOrderByIdDesc(districtCode, kw, pageable).map(CampSiteDto::from);
        }

        if (regionCode != null) {
            return kw.isBlank()
                    ? campSiteRepository.findByRegionCodeOrderByIdDesc(regionCode, pageable).map(CampSiteDto::from)
                    : campSiteRepository.findByRegionCodeAndNameContainingIgnoreCaseOrderByIdDesc(regionCode, kw, pageable).map(CampSiteDto::from);
        }

        return kw.isBlank()
                ? campSiteRepository.findAllByOrderByIdDesc(pageable).map(CampSiteDto::from)
                : campSiteRepository.findByNameContainingIgnoreCaseOrderByIdDesc(kw, pageable).map(CampSiteDto::from);
    }

    public CampSiteDto getCampSite(Long campSiteId) {
        return CampSiteDto.from(findCampSite(campSiteId));
    }

    @Transactional
    public CampSiteDto createCampSite(
            String name,
            String link,
            RegionCode regionCode,
            DistrictCode districtCode,
            LocalTime checkInTime,
            LocalTime checkOutTime
    ) {
        String normalizedName = normalizeName(name);
        validateDuplicate(normalizedName, null);
        validateLocation(regionCode, districtCode);

        CampSite campSite = CampSite.create(normalizedName, normalizeLink(link), regionCode, districtCode, checkInTime, checkOutTime);

        return CampSiteDto.from(campSiteRepository.save(campSite));
    }

    @Transactional
    public CampSiteDto updateCampSite(
            Long campSiteId,
            String name,
            String link,
            RegionCode regionCode,
            DistrictCode districtCode,
            LocalTime checkInTime,
            LocalTime checkOutTime
    ) {
        CampSite campSite = findCampSite(campSiteId);
        String normalizedName = normalizeName(name);
        validateDuplicate(normalizedName, campSiteId);
        validateLocation(regionCode, districtCode);

        campSite.update(normalizedName, normalizeLink(link), regionCode, districtCode, checkInTime, checkOutTime);
        return CampSiteDto.from(campSite);
    }

    @Transactional
    public void deleteCampSite(Long campSiteId) {
        CampSite campSite = findCampSite(campSiteId);
        campSiteRepository.delete(campSite);
    }

    private CampSite findCampSite(Long campSiteId) {
        return campSiteRepository.findById(campSiteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "캠핑장을 찾을 수 없습니다."));
    }

    private void validateDuplicate(String name, Long campSiteId) {
        boolean duplicated = campSiteId == null
                ? campSiteRepository.existsByNameIgnoreCase(name)
                : campSiteRepository.existsByNameIgnoreCaseAndIdNot(name, campSiteId);

        if (duplicated) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 등록된 캠핑장 이름입니다.");
        }
    }

    private void validateLocation(RegionCode regionCode, DistrictCode districtCode) {
        if (regionCode == null || districtCode == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지역구와 자치구는 필수입니다.");
        }

        if (districtCode.getRegionCode() != regionCode) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자치구가 선택한 지역구에 속하지 않습니다.");
        }
    }

    private String normalizeName(String name) {
        return name == null ? null : name.trim();
    }

    private String normalizeLink(String link) {
        if (link == null) {
            return null;
        }

        String normalizedLink = link.trim();
        return normalizedLink.isBlank() ? null : normalizedLink;
    }
}
