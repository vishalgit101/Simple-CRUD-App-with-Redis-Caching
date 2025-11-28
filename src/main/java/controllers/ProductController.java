package controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.ProductDto;
import jakarta.validation.Valid;
import services.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {
	// DI and class fields
	private final ProductService productService;

	public ProductController(ProductService productService) {
		super();
		this.productService = productService;
	}
	
	@PostMapping
	public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductDto productDto){
		ProductDto prod = this.productService.createProduct(productDto);
		
		return ResponseEntity.ok().body(prod);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId){
		ProductDto prod = this.productService.getProduct(productId);
		
		return ResponseEntity.ok().body(prod);
	}
	
	@PutMapping
	public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid ProductDto productDto){
		ProductDto prod = this.productService.updateProduct(productDto);
		return ResponseEntity.ok().body(prod);
	}
	
	@DeleteMapping("/{productId}")
	public void deleteProduct(@PathVariable Long productId){
		this.productService.deleteProduct(productId);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<ProductDto>> getAllProducts(){
		List<ProductDto> prodList = this.productService.getAll();
		return ResponseEntity.ok().body(prodList);
	}
	
}
