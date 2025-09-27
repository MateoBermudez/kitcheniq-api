package com.uni.kitcheniq.repository;

import com.uni.kitcheniq.models.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
