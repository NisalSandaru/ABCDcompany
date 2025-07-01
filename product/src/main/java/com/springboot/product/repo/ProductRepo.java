package com.springboot.product.repo;

import com.springboot.product.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends CrudRepository<Product, Integer> {
    @Query(value = "SELECT * FROM product WHERE product_id = ?1", nativeQuery = true)
    Product getProductById(Integer productId);
}
