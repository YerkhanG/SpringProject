package com.example.project.controller;

import com.example.project.model.Product;
import com.example.project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "oauth2",
        type = SecuritySchemeType.OAUTH2
)
@Tag(name = "Product Management", description = "Endpoints for managing products")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create a Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product request",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        productService.createProduct(product);
        return ResponseEntity.ok("Product created: " + product.getProductId());
    }

    @Operation(summary = "Get Product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content)
    })
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable String productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Get All Products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "204", description = "No products found",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Update Product Quantity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product quantity updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content)
    })
    @PutMapping("/{productId}/quantity")
    public ResponseEntity<String> updateProductQuantity(
            @PathVariable String productId,
            @RequestParam int quantityChange) {
        boolean updated = productService.updateProductQuantity(productId, quantityChange);
        if (updated) {
            return ResponseEntity.ok("Product quantity updated for ID: " + productId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found for ID: " + productId);
        }
    }
}

