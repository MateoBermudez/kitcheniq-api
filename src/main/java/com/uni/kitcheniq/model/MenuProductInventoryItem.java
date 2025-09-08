package com.uni.kitcheniq.model;

import jakarta.persistence.*;

@Entity
@Table(name = "MenuProductInventoryItem")
public class MenuProductInventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MenuProductId")
    private MenuProduct menuProduct;

    @ManyToOne
    @JoinColumn(name = "InventoryItemId")
    private InventoryItem inventoryItem;

    @Column(name = "Quantity", nullable = false)
    private int quantity;

    public MenuProductInventoryItem() {
        // Default constructor
    }

    public MenuProductInventoryItem(MenuProduct menuProduct, InventoryItem inventoryItem, int quantity) {
        this.menuProduct = menuProduct;
        this.inventoryItem = inventoryItem;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public MenuProduct getMenuProduct() {
        return menuProduct;
    }

    public void setMenuProduct(MenuProduct menuProduct) {
        this.menuProduct = menuProduct;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
