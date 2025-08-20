package erp.greenagro.greenagro_erp_backend.mapper;

import erp.greenagro.greenagro_erp_backend.dto.partner.PartnerEditResponse;
import erp.greenagro.greenagro_erp_backend.dto.partner.PartnerSummaryResponse;
import erp.greenagro.greenagro_erp_backend.dto.partner.create.CreatePartnerBase;
import erp.greenagro.greenagro_erp_backend.dto.partner.create.CreatePartnerResponse;
import erp.greenagro.greenagro_erp_backend.dto.partner.detail.BizPartnerDetailResponse;
import erp.greenagro.greenagro_erp_backend.dto.partner.detail.IndPartnerDetailResponse;
import erp.greenagro.greenagro_erp_backend.dto.partner.detail.PartnerDetailBase;
import erp.greenagro.greenagro_erp_backend.exception.CustomException;
import erp.greenagro.greenagro_erp_backend.model.entity.BusinessPartner;
import erp.greenagro.greenagro_erp_backend.model.entity.IndividualPartner;
import erp.greenagro.greenagro_erp_backend.model.entity.Partner;
import erp.greenagro.greenagro_erp_backend.model.enums.PartnerType;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import erp.greenagro.greenagro_erp_backend.util.RrnCryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode.INTERNAL_SERVER_ERROR;

@Component
@RequiredArgsConstructor
public class PartnerMapper {

    private final RrnCryptoUtil rrnCryptoUtil;

    public CreatePartnerResponse toCreate(Partner partner) {
        return new CreatePartnerResponse(
                partner.getId()
        );
    }


    public PartnerSummaryResponse toSummary(Partner partner) {

        PartnerType partnerType = null;

        if(partner instanceof BusinessPartner){
            partnerType = PartnerType.BUSINESS;
        }
        else if(partner instanceof IndividualPartner){
            partnerType = PartnerType.INDIVIDUAL;
        }
        else{
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }

        return new PartnerSummaryResponse(
                partner.getId(),
                partner.getCode(),
                partnerType,
                partner.getSalesGroup(),
                partner.getPartnerName(),
                partner.getRepName(),
                partner.getTel(),
                partner.getPhone(),
                partner.getFax(),
                partner.getEmail(),
                partner.getOurManager(),
                partner.getPartnerManager(),
                partner.getMemo()
        );
    }


    public PartnerDetailBase toDetail(Partner partner){

        // --- 사업자 ---
        if (partner instanceof BusinessPartner b) {
            return new BizPartnerDetailResponse(
                    b.getId(),
                    b.getCode(),
                    PartnerType.BUSINESS,
                    b.getSalesGroup(),
                    b.getPartnerName(),
                    b.getRepName(),
                    b.getTel(),
                    b.getPhone(),
                    b.getAddressMain(),
                    b.getAddressSub(),
                    b.getFax(),
                    b.getEmail(),
                    b.getOurManager(),
                    b.getPartnerManager(),
                    b.getMemo(),
                    b.getBizNo(),
                    b.getBizType(),
                    b.getBizItem()
            );
        }

        // --- 개인 ---
        else if (partner instanceof IndividualPartner i) {
            //주민번호 복호화
            String rrnDecrypted = rrnCryptoUtil.decryptRrn(i.getRrnEncrypted());

            return new IndPartnerDetailResponse(
                    i.getId(),
                    i.getCode(),
                    PartnerType.INDIVIDUAL,
                    i.getSalesGroup(),
                    i.getPartnerName(),
                    i.getRepName(),
                    i.getTel(),
                    i.getPhone(),
                    i.getAddressMain(),
                    i.getAddressSub(),
                    i.getFax(),
                    i.getEmail(),
                    i.getOurManager(),
                    i.getPartnerManager(),
                    i.getMemo(),
                    rrnDecrypted
            );
        }

        // --- 안전망 ---
        else {
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }
    }


    public PartnerEditResponse toEdit(Partner partner) {

        return new PartnerEditResponse(
                toDetail(partner),
                Arrays.stream(PartnerType.values()).toList(),
                Arrays.stream(SalesGroup.values()).toList()
        );
    }

}
