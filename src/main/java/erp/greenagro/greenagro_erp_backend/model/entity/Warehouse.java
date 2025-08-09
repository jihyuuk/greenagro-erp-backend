package erp.greenagro.greenagro_erp_backend.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Warehouse {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_site_id", nullable = false)
    private WarehouseSite warehouseSite; //거점명 (인천점, 김포점)

    private String name;        //창고명 ex) 1번창고, 2번창고

    private String code;        //코드 ex) 01, 02

}
