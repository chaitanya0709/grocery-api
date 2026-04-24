package com.katash.grocery.controller;

import com.katash.grocery.service.ProductService;
import com.katash.grocery.model.Products;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService service;

    @GetMapping("/")
    public String greet(){
        return "Welcome to, katash.";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Products>> getAllProducts(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{proId}")
    public ResponseEntity<Products> getProductById(@PathVariable int proId){
        Products product = service.getProductById(proId);

        if (product != null){
            return new ResponseEntity<>(product,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/products")
    public ResponseEntity<Object> addProduct(@RequestBody Products product){
        try {
            return new ResponseEntity<>(service.addProduct(product), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) { // Example: Invalid arguments passed
            return new ResponseEntity<>("Invalid input provided.", HttpStatus.BAD_REQUEST);
        } catch (OptimisticLockingFailureException ex){
            return new ResponseEntity<>("Invalid input provided.", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) { // Catch-all for unexpected exceptions
            return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/products")
    public ResponseEntity<Object> updateProduct(@RequestBody Products product){
        try {
            return service.updateProduct(product);
        } catch (IllegalArgumentException ex) { // Example: Invalid arguments passed
            return new ResponseEntity<>("Invalid input provided.", HttpStatus.BAD_REQUEST);
        } catch (OptimisticLockingFailureException ex){
            return new ResponseEntity<>("Invalid input provided.", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) { // Catch-all for unexpected exceptions
            return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/products/{prodId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable int prodId){
        return service.deleteProduct(prodId);
    }
}