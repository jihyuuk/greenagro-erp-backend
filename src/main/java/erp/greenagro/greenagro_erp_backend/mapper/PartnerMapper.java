package erp.greenagro.greenagro_erp_backend.mapper;

import erp.greenagro.greenagro_erp_backend.dto.partner.*;
import erp.greenagro.greenagro_erp_backend.model.entity.Partner;
import erp.greenagro.greenagro_erp_backend.model.enums.PartnerType;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PartnerMapper {

    /**
     * CreatePartnerRequest ===> Partner 엔티티로 변환합니다.
     */
    public Partner fromCreate(CreatePartnerRequest request) {

        return new Partner(
                request.getPartnerType(),
                request.getSalesGroup(),
                request.getCorpNo(),
                request.getBizNo(),
                request.getRrn(),
                request.getBizName(),
                request.getCeoName(),
                request.getBizType(),
                request.getBizItem(),
                request.getTel(),
                request.getPhone(),
                request.getAddressMain(),
                request.getAddressSub(),
                request.getFax(),
                request.getEmail(),
                request.getOurManager(),
                request.getPartnerManager(),
                request.getMemo()
        );
    }


    public CreatePartnerResponse toCreate(Partner partner) {

        return new CreatePartnerResponse(
                partner.getId()
        );
    }


    public PartnerSummaryResponse toSummary(Partner partner) {

        return new PartnerSummaryResponse(
                partner.getId(),
                partner.getPartnerType(),
                partner.getSalesGroup(),
                partner.getBizNo(),
                partner.getRrn(),
                partner.getBizName(),
                partner.getCeoName(),
                partner.getTel(),
                partner.getFax(),
                partner.getEmail(),
                partner.getOurManager(),
                partner.getPartnerManager(),
                partner.getMemo()
        );
    }


    public PartnerDetailResponse toDetail(Partner partner) {

        return new PartnerDetailResponse(
                partner.getId(),
                partner.getPartnerType(),
                partner.getSalesGroup(),
                partner.getCorpNo(),
                partner.getBizNo(),
                partner.getRrn(),
                partner.getBizName(),
                partner.getCeoName(),
                partner.getBizType(),
                partner.getBizItem(),
                partner.getTel(),
                partner.getPhone(),
                partner.getAddressMain(),
                partner.getAddressSub(),
                partner.getFax(),
                partner.getEmail(),
                partner.getOurManager(),
                partner.getPartnerManager(),
                partner.getMemo()
        );
    }


    public PartnerEditResponse toEdit(Partner partner) {

        return new PartnerEditResponse(
                toDetail(partner),
                Arrays.stream(PartnerType.values()).toList(),
                Arrays.stream(SalesGroup.values()).toList()
        );
    }

}
