package com.uni.kitcheniq.mapper;

import com.uni.kitcheniq.dto.SupplierDTO;
import com.uni.kitcheniq.models.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public SupplierDTO toSupplierDTO(Supplier supplier) {
        return SupplierDTO.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .contactInfo(supplier.getContactInfo())
                .build();
    }

}
