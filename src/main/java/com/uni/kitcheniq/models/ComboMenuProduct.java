package com.uni.kitcheniq.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}