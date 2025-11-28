package runner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import entity.Product;
import repos.ProductRepo;

@Component
public class DataInitalizer implements CommandLineRunner {
	// DI repo
	private ProductRepo productRepo;

	public DataInitalizer(ProductRepo productRepo) {
		super();
		this.productRepo = productRepo;
	}

	@Override
	public void run(String... args) throws Exception {
		
		if(this.productRepo.count() > 0) {
			System.out.println("Data Already Exists no need to Initalize");
			return;
		}
		
		// else 
		Product product1 = new Product();
		product1.setName("Playstation");
		product1.setPrice(BigDecimal.valueOf(50000));
		
		Product product2 = new Product();
		product2.setName("Google Pixel");
		product2.setPrice(BigDecimal.valueOf(60000));
		
		Product product3 = new Product();
		product3.setName("Lenovo LOQ");
		product3.setPrice(BigDecimal.valueOf(100000));
		
		
		List<Product> products = new ArrayList<>();
		products.add(product1);
		products.add(product2);
		products.add(product3);
		
		this.productRepo.saveAll(products);
		
		System.out.println("Data Initialized");
		
	}

}
