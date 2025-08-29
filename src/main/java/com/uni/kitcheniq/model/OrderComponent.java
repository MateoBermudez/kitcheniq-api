package com.uni.kitcheniq.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Product.class, name = "PRODUCT"),
        @JsonSubTypes.Type(value = Combo.class, name = "COMBO")
})
@Entity(name = "order_component")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "component_type", discriminatorType = DiscriminatorType.STRING)
public abstract class OrderComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public abstract double getPrice();
    public abstract String getDetails();
    public abstract Set<ProductInventory> getRequiredIngredients();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
