package com.uni.kitcheniq.controller;

import com.uni.kitcheniq.models.MenuComponent;
import com.uni.kitcheniq.models.MenuProduct;
import com.uni.kitcheniq.repository.MenuProductRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("kitcheniq/api/v1/menu")
public class MenuController {

    @Autowired
    private MenuProductRepository menuProductRepository;

    @PostMapping("/product")
    public ResponseEntity<MenuProduct> createProduct(@RequestBody MenuProductDTO productDTO) {
        // Crear MenuComponent
        MenuComponent component = new MenuProduct();

        // Crear MenuProduct
        MenuProduct product = new MenuProduct();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setMenuComponent(component);

        // Guardar
        MenuProduct saved = menuProductRepository.save(product);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/products")
    public ResponseEntity<List<MenuProduct>> getAllProducts() {
        return ResponseEntity.ok(menuProductRepository.findAll());
    }

    // DTO para la creación
    @Data
    @NoArgsConstructor
    public static class MenuProductDTO {
        private String name;
        private Double price;
    }
}
