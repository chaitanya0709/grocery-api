package com.katash.grocery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private Users user;  // Only userId will be stored in the database

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    private Products product;

    private Integer quantity;

}


//CREATE TABLE cart (
//        cart_id BIGINT AUTO_INCREMENT PRIMARY KEY,
//        user_id INT(11) NOT NULL,  -- Foreign key to Users table
//product_id INT(11) NOT NULL,  -- Foreign key to Products table
//quantity INT NOT NULL,
//added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//total_price DECIMAL(10, 2),  -- Optional field to store calculated price
//FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
//FOREIGN KEY (product_id) REFERENCES products(productid) ON DELETE CASCADE,
//CONSTRAINT UNIQUE_CART UNIQUE(user_id, product_id)  -- Ensure one entry per product per user
//);



