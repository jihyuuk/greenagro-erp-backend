package erp.greenagro.greenagro_erp_backend.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Entity
@Getter
@SoftDelete
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WarehouseSite {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        //거점 이름 ex) 인천점, 김포점

    private String address;     //주소

    private String code;        //코드 ex) ICN, GMP


    public WarehouseSite(String name, String address, String code) {
        this.name = name;
        this.address = address;
        this.code = code;
    }


    //수정하기
    public void update(String name, String address, String code){
        this.name = name;
        this.address = address;
        this.code = code;
    }
}
