package erp.greenagro.greenagro_erp_backend.model.entity;

import erp.greenagro.greenagro_erp_backend.model.enums.ProductGroupType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Entity
@Getter
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductGroup {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;            //그룹 이름

    @Enumerated(EnumType.STRING)
    private ProductGroupType type;  //타입 (노말:기본, 농약)

    private boolean removable;      //삭제 가능 여부

    public ProductGroup(String name, ProductGroupType type) {
        this.name = name;
        this.type = type;
        this.removable = true;
    }

    //수정하기
    public void update(String name){
        this.name = name;
    }

}
