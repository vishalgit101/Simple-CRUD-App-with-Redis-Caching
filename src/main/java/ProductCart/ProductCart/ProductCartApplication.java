package ProductCart.ProductCart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"controllers", "services", "repos", "configs", "entity", "runner"})
@EntityScan(basePackages = {"entity"})
@EnableJpaRepositories(basePackages = {"repos"})
@EnableCaching
public class ProductCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductCartApplication.class, args);
	}

}
