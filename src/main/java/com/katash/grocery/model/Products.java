package com.katash.grocery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;   // Auto Increnment no need to add this value
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    @JsonProperty("isAvailable")
    private Boolean isAvailable;
    private BigDecimal discount;
    private String imageUrl;
    private int categoryId;
    private int vendorId;
    private LocalDateTime createdAt;
    private int customerRating;
}
