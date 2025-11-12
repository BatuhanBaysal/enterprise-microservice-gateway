package com.batuhan.emg_service_product.dto;

import com.batuhan.emg_service_product.entity.ProductEntity;

import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price,
        Integer stock,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ProductResponse fromEntity(ProductEntity entity) {
        return new ProductResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStock(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}