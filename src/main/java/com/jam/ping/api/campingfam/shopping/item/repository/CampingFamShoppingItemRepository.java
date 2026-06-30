package com.jam.ping.api.campingfam.shopping.item.repository;

import com.jam.ping.api.campingfam.shopping.item.domain.CampingFamShoppingItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampingFamShoppingItemRepository extends JpaRepository<CampingFamShoppingItem, Long> {

    List<CampingFamShoppingItem> findByCategoryId(Long categoryId);

    void deleteByCategoryId(Long categoryId);
}
