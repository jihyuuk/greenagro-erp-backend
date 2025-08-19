package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.partner.*;
import erp.greenagro.greenagro_erp_backend.exception.CustomException;
import erp.greenagro.greenagro_erp_backend.mapper.PartnerMapper;
import erp.greenagro.greenagro_erp_backend.model.entity.Partner;
import erp.greenagro.greenagro_erp_backend.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerMapper partnerMapper;

    //거래처 생성
    @Transactional
    public CreatePartnerResponse createPartner(CreatePartnerRequest request) {
        //1. 각종 검증

        //2. 중복확인 - 중복에 관해서는 열어두자! 추후 고려

        //3. 객체 생성 (주민번호의 경우 암호화 필요)
        Partner partner = partnerMapper.fromCreate(request);

        //3. 저장
        partnerRepository.save(partner);

        //4. id 반환
        return partnerMapper.toCreate(partner);
    }


    //전체 조회
    @Transactional(readOnly = true)
    public List<PartnerSummaryResponse> getAllPartners() {

        return partnerRepository.findAll().stream().map(partner ->
                partnerMapper.toSummary(partner)
        ).toList();
    }


    //상세 조회
    @Transactional(readOnly = true)
    public PartnerDetailResponse getPartnerDetail(Long id) {
        //1. 해당 거래처 조회 (없으면 에러)
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new CustomException(PARTNER_NOT_FOUND, id));

        //2. dto 변환 및 반환 (주민번호의 경우 앞자리만 복호화)
        return partnerMapper.toDetail(partner);
    }

    //수정 데이터 조회
    @Transactional(readOnly = true)
    public PartnerEditResponse getPartnerEditData(Long id) {
        //1. 해당 거래처 조회 (없으면 에러)
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new CustomException(PARTNER_NOT_FOUND, id));

        //2. DTO 생성 반환
        return partnerMapper.toEdit(partner);
    }

    //수정
    @Transactional
    public void updatePartner(Long id, UpdatePartnerRequest request) {
        //1. 해당 거래처 조회 (없으면 에러)
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new CustomException(PARTNER_NOT_FOUND, id));

        //검증 추후 구현

        //2. 업데이트
        partner.update(
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


    //삭제
    @Transactional
    public void deletePartner(Long id) {
        //1. 해당 거래처 조회 (없으면 에러)
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new CustomException(PARTNER_NOT_FOUND, id));

        //2. 논리 삭제
        partnerRepository.delete(partner);
    }


}
