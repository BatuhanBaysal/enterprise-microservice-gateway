package com.batuhan.emg_service_product.service;

import com.batuhan.emg_service_product.dto.ProductCreateRequest;
import com.batuhan.emg_service_product.dto.ProductResponse;
import com.batuhan.emg_service_product.dto.StockUpdateRequest;
import com.batuhan.emg_service_product.entity.ProductEntity;
import com.batuhan.emg_service_product.exception.InsufficientStockException;
import com.batuhan.emg_service_product.exception.ProductAlreadyExistsException;
import com.batuhan.emg_service_product.exception.ProductNotFoundException;
import com.batuhan.emg_service_product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private ProductEntity findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " could not be found."));
    }

    public ProductResponse createProduct(ProductCreateRequest request) {
        productRepository.findByNameIgnoreCase(request.name())
                .ifPresent(p -> {
                    throw new ProductAlreadyExistsException("The product name is already in use: " + request.name());
                });

        ProductEntity newProduct = ProductEntity.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .stock(request.stock())
                .build();

        ProductEntity savedProduct = productRepository.save(newProduct);
        return ProductResponse.fromEntity(savedProduct);
    }

    public ProductResponse getProductById(Long id) {
        ProductEntity product = findProductOrThrow(id);
        return ProductResponse.fromEntity(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    public ProductResponse updateStock(Long id, StockUpdateRequest request) {
        Integer quantityChange = request.quantityChange();

        ProductEntity product = findProductOrThrow(id);

        int currentStock = product.getStock();
        int newStock = currentStock + quantityChange;

        if (newStock < 0) {
            throw new InsufficientStockException("There is insufficient product in stock. Current Stock: " + currentStock);
        }

        product.setStock(newStock);
        ProductEntity updatedProduct = productRepository.save(product);

        return ProductResponse.fromEntity(updatedProduct);
    }

    public void deleteProduct(Long id) {
        ProductEntity productToDelete = findProductOrThrow(id);
        productRepository.delete(productToDelete);
    }
}