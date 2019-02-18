package com.app.controllers;

import com.app.dto.ProductDto;
import com.app.service.MyServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private MyServiceImpl service;

    public ProductController(MyServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getOneProduct(@PathVariable Long id) {
        return new ResponseEntity<>(service.getProductById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(RequestEntity<ProductDto> request) {
        ProductDto productDto = service.addProduct(request.getBody());
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(RequestEntity<ProductDto> request) {
        ProductDto productDto = service.updateProduct(request.getBody());
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }
}
