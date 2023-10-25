package shop.mookmall.com.model.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByProductType(ProductType productType, Pageable pageable);

    Page<Product> findByProductNameContaining(String keyword, Pageable pageable);
    Page<Product> findByProductNameContainingAndProductType(String keyword, ProductType productType, Pageable pageable);
}
