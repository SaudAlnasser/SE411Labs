package psu.lab13.controllers;
import jakarta.validation.constraints.Positive;
import psu.lab13.model.Product;
import psu.lab13.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }
    @GetMapping
    public ResponseEntity<Iterable<Product>> findAll() { return ResponseEntity.ok(service.findAll()); }
    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") @Positive Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
