package com.uni.kitcheniq.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ComboMenuProductId implements Serializable {
    private static final long serialVersionUID = -5945419735466001686L;
    @Column(name = "combo_id", nullable = false)
    private Long comboId;

    @Column(name = "menu_product_id", nullable = false)
    private Long menuProductId;

    public Long getComboId() {
        return comboId;
    }

    public void setComboId(Long comboId) {
        this.comboId = comboId;
    }

    public Long getMenuProductId() {
        return menuProductId;
    }

    public void setMenuProductId(Long menuProductId) {
        this.menuProductId = menuProductId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ComboMenuProductId entity = (ComboMenuProductId) o;
        return Objects.equals(this.comboId, entity.comboId) &&
                Objects.equals(this.menuProductId, entity.menuProductId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comboId, menuProductId);
    }

}