package erp.greenagro.greenagro_erp_backend.dto.customer;

import erp.greenagro.greenagro_erp_backend.model.enums.CustomerType;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CustomerEditResponse {

    private CustomerDetailResponse customer; //고객 상세 정보

    private List<CustomerType> customerTypeOptions; //사업자 유형 Enum (법인사업자, 개인사업자, 개인)

    private List<SalesGroup> salesGroupOptions; //영업 분류 Enum (조경, 농협, 관공서, 개인, 기타)

}
