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
import shop.mookmall.com.model.product.OrderType;
import shop.mookmall.com.model.product.Product;
import shop.mookmall.com.model.product.ProductType;
import shop.mookmall.com.model.product.SortOrder;
import shop.mookmall.com.service.ProductService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product")
    public ResponseEntity<?> getProductList(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size,
                                            @RequestParam @NotBlank String productType,
                                            @RequestParam @NotBlank String orderType,
                                            @RequestParam @NotBlank String sortOrder,
                                            @RequestParam(required = true) String keyword){


        ProductType productTypeEnum = ProductType.valueOf(productType);
        OrderType orderTypeEnum = OrderType.valueOf(orderType);
        SortOrder sortOrderEnum = SortOrder.valueOf(sortOrder);


        PageDTO<ProductResponse.ProductDTO, Product> pageDTO = productService.getList(
                page,
                size,
                productTypeEnum,
                orderTypeEnum,
                sortOrderEnum,
                keyword);

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
