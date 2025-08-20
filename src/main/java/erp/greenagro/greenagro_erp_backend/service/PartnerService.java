package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.partner.PartnerEditResponse;
import erp.greenagro.greenagro_erp_backend.dto.partner.PartnerSummaryResponse;
import erp.greenagro.greenagro_erp_backend.dto.partner.create.CreateBizPartnerRequest;
import erp.greenagro.greenagro_erp_backend.dto.partner.create.CreateIndPartnerRequest;
import erp.greenagro.greenagro_erp_backend.dto.partner.create.CreatePartnerResponse;
import erp.greenagro.greenagro_erp_backend.dto.partner.detail.PartnerDetailBase;
import erp.greenagro.greenagro_erp_backend.dto.partner.update.UpdateBizPartnerRequest;
import erp.greenagro.greenagro_erp_backend.dto.partner.update.UpdateIndPartnerRequest;
import erp.greenagro.greenagro_erp_backend.exception.CustomException;
import erp.greenagro.greenagro_erp_backend.mapper.PartnerMapper;
import erp.greenagro.greenagro_erp_backend.model.entity.BusinessPartner;
import erp.greenagro.greenagro_erp_backend.model.entity.IndividualPartner;
import erp.greenagro.greenagro_erp_backend.model.entity.Partner;
import erp.greenagro.greenagro_erp_backend.repository.BusinessPartnerRepository;
import erp.greenagro.greenagro_erp_backend.repository.IndividualPartnerRepository;
import erp.greenagro.greenagro_erp_backend.repository.PartnerRepository;
import erp.greenagro.greenagro_erp_backend.util.RrnCryptoUtil;
import erp.greenagro.greenagro_erp_backend.validator.DuplicationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static erp.greenagro.greenagro_erp_backend.model.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final IndividualPartnerRepository indPartnerRepo;
    private final BusinessPartnerRepository bizPartnerRepo;
    private final PartnerMapper partnerMapper;
    private final RrnCryptoUtil rrnCryptoUtil;


    //거래처 생성(사업자)
    @Transactional
    public CreatePartnerResponse createBizPartner(CreateBizPartnerRequest request) {
        //1. 각종 검증

        //2. 중복확인 - 거래처명, 사업자번호, 주민번호 <- 중복허용
        DuplicationValidator.validate(dv -> dv
                //코드 중복 확인
                .check(partnerRepository.existsByCode(request.getCode()),"code", request.getCode())
        );


        //3. 객체 생성 (주민번호의 경우 암호화 필요)
        BusinessPartner bizPartner = new BusinessPartner(
                request.getSalesGroup(),
                request.getCode(),
                request.getPartnerName(),
                request.getRepName(),
                request.getTel(),
                request.getPhone(),
                request.getAddressMain(),
                request.getAddressSub(),
                request.getFax(),
                request.getEmail(),
                request.getOurManager(),
                request.getPartnerManager(),
                request.getMemo(),
                request.getBizNo(),
                request.getBizType(),
                request.getBizItem()
        );

        //3. 저장
        BusinessPartner savedBizPartner = bizPartnerRepo.save(bizPartner);

        //4. id 반환
        return partnerMapper.toCreate(savedBizPartner);
    }


    //거래처 생성(개인)
    @Transactional
    public CreatePartnerResponse createIndPartner(CreateIndPartnerRequest request) {
        //1. 각종 검증

        //2. 중복확인 - 거래처명, 사업자번호, 주민번호 <- 중복허용
        DuplicationValidator.validate(dv -> dv
                //코드 중복 확인
                .check(partnerRepository.existsByCode(request.getCode()),"code", request.getCode())
        );


        //3. 객체 생성 (주민번호의 경우 암호화 필요)
        IndividualPartner indPartner = new IndividualPartner(
                request.getSalesGroup(),
                request.getCode(),
                request.getPartnerName(),
                request.getRepName(),
                request.getTel(),
                request.getPhone(),
                request.getAddressMain(),
                request.getAddressSub(),
                request.getFax(),
                request.getEmail(),
                request.getOurManager(),
                request.getPartnerManager(),
                request.getMemo(),
                rrnCryptoUtil.encryptRrn(request.getRrn()) //주민번호 암호화
        );

        //3. 저장
        IndividualPartner savedIndPartner = indPartnerRepo.save(indPartner);

        //4. id 반환
        return new CreatePartnerResponse(savedIndPartner.getId());
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
    public PartnerDetailBase getPartnerDetail(Long id) {
        //1. 해당 거래처 조회 (없으면 에러)
        Partner partner = partnerRepository.findById(id).orElseThrow(() -> new CustomException(PARTNER_NOT_FOUND, id));

        //2. dto 변환 및 반환
        return partnerMapper.toDetail(partner);
    }


    //수정 데이터 조회
    @Transactional(readOnly = true)
    public PartnerEditResponse getPartnerEditData(Long id) {
        //1. 해당 거래처 조회 (없으면 에러)
        Partner partner = partnerRepository.findById(id).orElseThrow(() -> new CustomException(PARTNER_NOT_FOUND, id));

        //2. DTO 생성 반환
        return partnerMapper.toEdit(partner);
    }


    //수정 - 사업자
    @Transactional
    public void updateBizPartner(Long id, UpdateBizPartnerRequest request) {
        //1. 해당 거래처 조회 (없으면 에러)
        BusinessPartner partner = bizPartnerRepo.findById(id).orElseThrow(() -> new CustomException(PARTNER_NOT_FOUND, id));

        //2. 중복 검사
        DuplicationValidator.validate(dv -> dv
                //코드 중복 체크
                .check(!partner.getCode().equals(request.getCode()) && partnerRepository.existsByCode(request.getCode()), "code", request.getCode())
        );

        //3. 업데이트
        partner.update(
                request.getSalesGroup(),
                request.getCode(),
                request.getPartnerName(),
                request.getRepName(),
                request.getTel(),
                request.getPhone(),
                request.getAddressMain(),
                request.getAddressSub(),
                request.getFax(),
                request.getEmail(),
                request.getOurManager(),
                request.getPartnerManager(),
                request.getMemo(),
                request.getBizNo(),
                request.getBizType(),
                request.getBizItem()
        );

    }


    //수정 - 개인
    @Transactional
    public void updateIndPartner(Long id, UpdateIndPartnerRequest request) {
        //1. 해당 거래처 조회 (없으면 에러)
        IndividualPartner partner = indPartnerRepo.findById(id).orElseThrow(() -> new CustomException(PARTNER_NOT_FOUND, id));

        //2. 중복 검사
        DuplicationValidator.validate(dv -> dv
                //코드 중복 체크
                .check(!partner.getCode().equals(request.getCode()) && partnerRepository.existsByCode(request.getCode()), "code", request.getCode())
        );

        //3. 업데이트
        partner.update(
                request.getSalesGroup(),
                request.getCode(),
                request.getPartnerName(),
                request.getRepName(),
                request.getTel(),
                request.getPhone(),
                request.getAddressMain(),
                request.getAddressSub(),
                request.getFax(),
                request.getEmail(),
                request.getOurManager(),
                request.getPartnerManager(),
                request.getMemo(),
                rrnCryptoUtil.encryptRrn(request.getRrn()) // 주민번호 암호화
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
