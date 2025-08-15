package erp.greenagro.greenagro_erp_backend.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Entity
@Getter
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WarehouseZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;    //속한 창고

    private String name;            //구역 이름 ex)A구역, B구역, NONE

    private String code;            //코드  ex)A, B, C, NONE


    public WarehouseZone(Warehouse warehouse, String name, String code) {
        this.warehouse = warehouse;
        this.name = name;
        this.code = code;
    }


    //수정하기
    public void update(String name, String code){
        this.name = name;
        this.code = code;
    }
}
