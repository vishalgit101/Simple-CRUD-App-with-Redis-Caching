package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import dto.ProductDto;
import entity.Product;
import repos.ProductRepo;

@Service
public class ProductService {
	// DI and class fields
	private final ProductRepo productRepo;
	
	@Autowired
	public ProductService(ProductRepo productRepo) {
		super();
		this.productRepo = productRepo;
	}
	
	// placeholder constructor to practice with CacheManger Spring bean;
	private CacheManager cacheManager;
	public ProductService(ProductRepo productRepo, CacheManager cacheManager) {
		super();
		this.productRepo = productRepo;
		this.cacheManager = cacheManager;
	}

	// ===========================With annotations================================================
	@CachePut(value = "PRODUCT_CACHE", key = "#result.id")
	public ProductDto createProduct(ProductDto productDto) {
		Product product = new Product();
		product.setName(productDto.getName());
		product.setPrice(productDto.getPrice());
		this.productRepo.save(product);
		
		return new ProductDto(product.getId(), product.getName(), product.getPrice());
	}

	@Cacheable(value = "PRODUCT_CACHE", key = "#productId")
	public ProductDto getProduct(Long productId) {
		Product product = this.productRepo.findById(productId).orElseThrow(()-> new IllegalArgumentException("Cannot find product with product id: " + productId));
		
		return new ProductDto(product.getId(), product.getName(), product.getPrice());
	}

	@CachePut(value = "PRODUCT_CACHE", key = "#result.id")
	public ProductDto updateProduct(ProductDto productDto) {
		Long productId = productDto.getId();
		Product product = this.productRepo.findById(productId).orElseThrow(()-> new IllegalArgumentException("Cannot find product with product id: " + productId));
		product.setName(productDto.getName());
		product.setPrice(productDto.getPrice());
		
		Product updatedProduct = this.productRepo.save(product);
		return new ProductDto(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getPrice());
	}

	@CacheEvict(value = "PRODUCT_CACHE", key="#productId")
	public void deleteProduct(Long productId) {
		this.productRepo.deleteById(productId);
	}
	
	// ================================== With CacheManager ============================================
	
	public ProductDto createProduct2(ProductDto productDto) {
		Product product = new Product();
		product.setName(productDto.getName());
		product.setPrice(productDto.getPrice());
		this.productRepo.save(product);
		
		
		ProductDto dto =  new ProductDto(product.getId(), product.getName(), product.getPrice());
		
		Cache cache = this.cacheManager.getCache("PRODUCT_CACHE"); // if not exists lazily creates a redis bucket with name products
		cache.put(dto.getId(), dto); // put in Redis with ID as key
		return dto;
		
		// Similar to: @CachePut(value = "products", key = "#result.id")
	} 

	public ProductDto getProduct2(Long productId) {
		
		// Check in memory cache redis
		Cache cache = this.cacheManager.getCache("PRODUCT_CACHE"); // if not exists lazily creates a redis bucket with name products
		ProductDto cachedProduct = cache.get(productId, ProductDto.class);
		
		if(cachedProduct != null) {
			return cachedProduct; // return from Redis
		}
		
		// Not in cache → load from DB
		Product product = this.productRepo.findById(productId).orElseThrow(()-> new IllegalArgumentException("Cannot find product with product id: " + productId));
		
		
		ProductDto dto =  new ProductDto(product.getId(), product.getName(), product.getPrice());
		
		// Store in Redis
		cache.put(productId, dto);
		return dto;
		
		//Similar to:  @Cacheable(value = "products", key = "#productId")
	}

	public ProductDto updateProduct2(ProductDto productDto) {
		Long productId = productDto.getId();
		Product product = this.productRepo.findById(productId).orElseThrow(()-> new IllegalArgumentException("Cannot find product with product id: " + productId));
		product.setName(productDto.getName());
		product.setPrice(productDto.getPrice());
		
		Product updatedProduct = this.productRepo.save(product);
		ProductDto updated =  new ProductDto(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getPrice());
		
		Cache cache = this.cacheManager.getCache("PRODUCT_CACHE");
		cache.put(updated.getId(), updated);
		return updated;
		
	}

	public void deleteProduct2(Long productId) {
		this.productRepo.deleteById(productId);
		
	    Cache cache = cacheManager.getCache("PRODUCT_CACHE");
	    cache.evict(productId);
	}
	
}
