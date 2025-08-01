package erp.greenagro.greenagro_erp_backend.mapper;

import erp.greenagro.greenagro_erp_backend.dto.payinfo.PayInfoDTO;
import erp.greenagro.greenagro_erp_backend.model.entity.PayInfo;
import org.springframework.stereotype.Component;

@Component
public class PayInfoMapper {

    public PayInfo toEntity(PayInfoDTO request){
        return new PayInfo(
                request.getBankName(),     //은행
                request.getAccountNumber(),//계좌번호
                request.getDepositorName(),//예금주
                request.getBaseSalary()    //기본급
        );
    }

    public PayInfoDTO toResponse(PayInfo payInfo){
        return new PayInfoDTO(
                payInfo.getBankName(),     //은행
                payInfo.getAccountNumber(),//계좌번호
                payInfo.getDepositorName(),//예금주
                payInfo.getBaseSalary()    //기본급
        );
    }

}
