package com.uni.kitcheniq.models;

import jakarta.persistence.*;

@Entity
@Table(name = "combo_menu_product")
public class ComboMenuProduct {
    @EmbeddedId
    private ComboMenuProductId id;

    @MapsId("comboId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "combo_id", nullable = false)
    private Combo combo;

    @MapsId("menuProductId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_product_id", nullable = false)
    private MenuProduct menuProduct;

    public ComboMenuProductId getId() {
        return id;
    }

    public void setId(ComboMenuProductId id) {
        this.id = id;
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }

    public MenuProduct getMenuProduct() {
        return menuProduct;
    }

    public void setMenuProduct(MenuProduct menuProduct) {
        this.menuProduct = menuProduct;
    }

}