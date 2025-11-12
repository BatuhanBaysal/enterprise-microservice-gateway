package com.batuhan.emg_service_product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity extends BaseEntity {

    private String name;
    private String description;
    private Double price;
    private Integer stock;
}