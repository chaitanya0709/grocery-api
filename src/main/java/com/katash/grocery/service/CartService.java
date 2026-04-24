package com.katash.grocery.service;

import com.katash.grocery.model.Cart;
import com.katash.grocery.model.Products;
import com.katash.grocery.model.Users;
import com.katash.grocery.repository.CartRepo;
import com.katash.grocery.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    CartRepo cartRepo;

    public ResponseEntity<Object> addToCart(Users user, int productId, int quantity) {
        Optional<Products> productOptional = productRepo.findById(productId);
        if (productOptional.isEmpty()){
            return new ResponseEntity<>("Product not found.",HttpStatus.NOT_FOUND);
        }

        Products product = productOptional.get();

        if (!product.getIsAvailable() || product.getStock() < quantity) {
            return new ResponseEntity<>("Product is out of stock or unavailable.",HttpStatus.CONFLICT);
        }

        Optional<Cart> existingCartItem = cartRepo.findByUserUserIdAndProductProductId(user.getUserId(), productId);
        if (existingCartItem.isPresent()) {
            Cart cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartRepo.save(cartItem);
            return new ResponseEntity<>("Product quantity updated in cart.",HttpStatus.OK);
        }

        // Add new product to cart
        Cart cartItem = new Cart();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartRepo.save(cartItem);

        return new ResponseEntity<>("Product added to cart.", HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getUserCart(int id) {

        List<Cart> cartItems = cartRepo.findByUserUserId(id);

        Map<String, Object> response = cartItems.stream()
                .collect(Collectors.groupingBy(
                        cart -> cart.getUser().getUserId(), // Group by userId
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    Map<String, Object> userCart = new HashMap<>();
                                    userCart.put("userId", list.getFirst().getUser().getUserId());
                                    userCart.put("products", list.stream().map(cart -> {
                                        Map<String, Object> productDetails = new HashMap<>();
                                        Products product = cart.getProduct();
                                        productDetails.put("productId", product.getProductId());
                                        productDetails.put("name", product.getName());
                                        productDetails.put("description", product.getDescription());
                                        productDetails.put("price", product.getPrice());
                                        productDetails.put("stock", product.getStock());
                                        productDetails.put("discount", product.getDiscount());
                                        productDetails.put("imageUrl", product.getImageUrl());
                                        productDetails.put("categoryId", product.getCategoryId());
                                        productDetails.put("vendorId", product.getVendorId());
                                        productDetails.put("createdAt", product.getCreatedAt());
                                        productDetails.put("customerRating", product.getCustomerRating());
                                        productDetails.put("isAvailable", product.getIsAvailable());
                                        productDetails.put("quantity", cart.getQuantity());
                                        return productDetails;
                                    }).collect(Collectors.toList()));
                                    return userCart;
                                }
                        )
                ))
                .values()
                .stream()
                .findFirst() // The result for a single user
                .orElse(null);

        return new ResponseEntity<>(response, HttpStatus.OK);

//        return new ResponseEntity<>(cartRepo.findByUserUserId(id),HttpStatus.OK);
    }
}