package repos;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {

}
