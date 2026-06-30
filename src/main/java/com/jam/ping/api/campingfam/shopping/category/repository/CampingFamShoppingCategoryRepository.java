package com.jam.ping.api.campingfam.shopping.category.repository;

import com.jam.ping.api.campingfam.shopping.category.domain.CampingFamShoppingCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampingFamShoppingCategoryRepository extends JpaRepository<CampingFamShoppingCategory, Long> {

    List<CampingFamShoppingCategory> findByCampingFamId(Long campingFamId);

    void deleteByCampingFamId(Long campingFamId);
}
