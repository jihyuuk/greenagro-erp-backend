package erp.greenagro.greenagro_erp_backend.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductGroup {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        //그룹 이름

    private boolean deleted;   //삭제 여부


    public ProductGroup(String name) {
        this.name = name;
    }

    //수정하기
    public void update(String name){
        this.name = name;
    }

    //삭제하기
    public void delete(){
        this.deleted = true;
    }
}
