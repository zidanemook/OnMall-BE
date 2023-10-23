package shop.mookmall.com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.mookmall.com.core.exception.Exception404;
import shop.mookmall.com.dto.PageDTO;
import shop.mookmall.com.dto.ResponseDTO;
import shop.mookmall.com.dto.product.ProductResponse;
import shop.mookmall.com.model.product.Product;
import shop.mookmall.com.service.ProductService;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product/recent")
    public ResponseEntity<?> getRecentList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size){

        PageDTO<ProductResponse.ProductDTO, Product> pageDTO = productService.getRecent(page, size);
        ResponseDTO<PageDTO<ProductResponse.ProductDTO, Product>> responseDTO = new ResponseDTO<>(pageDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/product/food")
    public ResponseEntity<?> getFoodList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size){

        PageDTO<ProductResponse.ProductDTO, Product> pageDTO = productService.getFood(page, size);
        ResponseDTO<PageDTO<ProductResponse.ProductDTO, Product>> responseDTO = new ResponseDTO<>(pageDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/productdetail")
    public ResponseEntity<?> getDetail(@RequestParam("productid") Long productId) {

        ProductResponse.ProductDTO productDTO = productService.findProductByIdAndGetDTO(productId);

        if (productDTO != null) {
            ResponseDTO<ProductResponse.ProductDTO> responseDTO = new ResponseDTO<>(productDTO);
            return ResponseEntity.ok(responseDTO);
        } else {
            // 404 Not Found 응답 반환
            throw new Exception404(productId + " not found");
        }
    }

}
