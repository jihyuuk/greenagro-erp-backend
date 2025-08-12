package erp.greenagro.greenagro_erp_backend.controller;

import erp.greenagro.greenagro_erp_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

}
