package com.katash.grocery.service;

import com.katash.grocery.model.Products;
import com.katash.grocery.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

//    List<Products> products = new ArrayList<>(Arrays.asList(
//            new Products( 101,
//                    "Wireless Mouse",
//                    "Ergonomic wireless mouse with adjustable DPI.",
//                    new BigDecimal("15.99"),
//                    50,
//                    new BigDecimal("2.00"),
//                    "https://example.com/images/mouse.jpg",
//                    1,
//                    101,
//                    LocalDateTime.of(2025, 1, 15, 10, 30),
//                    4)
//    ));

    public List<Products> getAllProducts() {
        return repo.findAll();
    }

    public Products getProductById(int proId) {
        return repo.findById(proId).orElse(null);
    }

    public Products addProduct(Products product) throws OptimisticLockingFailureException {
        product.setProductId(0);
        product.setCreatedAt(LocalDateTime.now());
        return repo.save(product);
    }

    public ResponseEntity<Object> updateProduct(Products product) {
        Products originalProduct = repo.findById(product.getProductId()).orElse(null);
        if (originalProduct!=null){
            Products updatedProduct  = mergeProducts(product, originalProduct);
            return new ResponseEntity<>(repo.save(updatedProduct),HttpStatus.OK);
        }
        return new ResponseEntity<>("Cannot find product to update.",HttpStatus.NOT_FOUND);
    }


    private Products mergeProducts(Products incoming, Products original){
        return Products.builder()
                .productId(original.getProductId())
                .name(Optional.ofNullable(incoming.getName()).orElse(original.getName()))
                .description(Optional.ofNullable(incoming.getDescription()).orElse(original.getDescription()))
                .price(Optional.ofNullable(incoming.getPrice()).orElse(original.getPrice()))
                .discount(Optional.ofNullable(incoming.getDiscount()).orElse(original.getDiscount()))
                .imageUrl(Optional.ofNullable(incoming.getImageUrl()).orElse(original.getImageUrl()))
                .categoryId(incoming.getCategoryId() != 0 ? incoming.getCategoryId() : original.getCategoryId())
                .vendorId(getVendorIdFromJWT()) // Securely capture Vendor ID from JWT
                .createdAt(original.getCreatedAt())
                .customerRating(original.getCustomerRating())
                .stock(incoming.getStock() != null ? incoming.getStock() : original.getStock()) // Handle stock explicitly
                .isAvailable(incoming.getIsAvailable() != null ? incoming.getIsAvailable() : original.getIsAvailable()) // Handle isAvailable explicitly
                .build();
    }

    private int getVendorIdFromJWT() {
        return 0;
    }

    public ResponseEntity<Object> deleteProduct(int prodId) {
        if (repo.existsById(prodId)) {
            repo.deleteById(prodId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }
}
