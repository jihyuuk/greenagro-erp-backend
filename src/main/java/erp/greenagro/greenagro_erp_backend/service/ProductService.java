package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

}
