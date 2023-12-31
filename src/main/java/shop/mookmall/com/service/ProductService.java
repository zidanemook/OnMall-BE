package shop.mookmall.com.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import shop.mookmall.com.core.exception.Exception400;
import shop.mookmall.com.dto.PageDTO;
import shop.mookmall.com.dto.product.ProductResponse;
import shop.mookmall.com.model.product.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public PageDTO<ProductResponse.ProductDTO, Product> getList(int page, int size,
                                                                ProductType productType,
                                                                OrderType orderType,
                                                                SortOrder sortOrder,
                                                                String keyword){

        Sort sort;
        switch(orderType) {
            case CREATED_AT:
                sort = Sort.by(sortOrder == SortOrder.ASCENDING ? Sort.Order.asc("createdAt") : Sort.Order.desc("createdAt"));
                break;
            case COMMENT:
                sort = Sort.by(sortOrder == SortOrder.ASCENDING ? Sort.Order.asc("commentCount") : Sort.Order.desc("commentCount"));
                break;
            case RATING:
                sort = Sort.by(sortOrder == SortOrder.ASCENDING ? Sort.Order.asc("rating") : Sort.Order.desc("rating"));
                break;
            case PRICE:
                sort = Sort.by(sortOrder == SortOrder.ASCENDING ? Sort.Order.asc("price") : Sort.Order.desc("price"));
                break;
            default:
                throw new IllegalArgumentException("Invalid OrderType");
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productsPage = null;

        if(keyword.length() > 0){
            if(productType == ProductType.PRODUCT_TYPE_ALL){
                productsPage = productRepository.findByProductNameContaining(keyword, pageable);
            }else{
                productsPage = productRepository.findByProductNameContainingAndProductType(keyword, productType, pageable);
            }
        }
        else{
            if(productType == ProductType.PRODUCT_TYPE_ALL){
                productsPage = productRepository.findAll(pageable);
            }else{
                productsPage = productRepository.findByProductType(productType, pageable);
            }
        }

        List<ProductResponse.ProductDTO> productDTOList = productsPage.getContent().stream()
                .map(product -> new ProductResponse.ProductDTO(
                        product.getId(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getManufacturer(),
                        product.getRating(),
                        product.getPhoto(),
                        product.getProductType().toString(),
                        product.getCommentCount(),
                        product.getCreatedAt().toString()))
                .collect(Collectors.toList());

        return new PageDTO<>(productDTOList, productsPage);
    }

    public ProductResponse.ProductDTO findProductByIdAndGetDTO(Long productid){

        Optional<Product> opProduct = productRepository.findById(productid);

        if (opProduct.isPresent()) {
            Product product = opProduct.get();

            ProductResponse.ProductDTO productDTO = new ProductResponse.ProductDTO(product.getId(),
                    product.getProductName(),
                    product.getPrice(),
                    product.getManufacturer(),
                    product.getRating(),
                    product.getPhoto(),
                    product.getProductType().toString(),
                    product.getCommentCount(),
                    product.getCreatedAt().toString());

            return productDTO;
        }

        return null;
    }

    public Product findProductById(Long productid){
        Product product = productRepository.findById(productid)
                .orElseThrow(()->new Exception400("id", "해당 제품이 존재하지 않습니다."));

        return product;
    }

    public void saveProduct(Product product){
        productRepository.save(product);
    }
}
