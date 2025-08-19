package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.partner.*;
import erp.greenagro.greenagro_erp_backend.model.entity.Partner;
import erp.greenagro.greenagro_erp_backend.model.enums.*;
import erp.greenagro.greenagro_erp_backend.repository.PartnerRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class PartnerServiceTest {


    @Autowired
    PartnerService partnerService;
    @Autowired
    PartnerRepository partnerRepository;
    @Autowired
    EntityManager em;

    private Long dummyId;


    @Test
    void 거래처_생성_정상() {
        //given
        CreatePartnerRequest request = generateCreatePartnerRequest();

        //when
        CreatePartnerResponse response = partnerService.createPartner(request); //거래처 생성 서비스 수행

        //저장된 거래처 조회
        Partner partner = partnerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 거래처 찾기 실패 Id:" + dummyId));

        //then
        assertNotNull(response.getPartnerId()); //생성된 거래처 아이디 반환 확인

        assertEquals(request.getPartnerType(), partner.getPartnerType());
        assertEquals(request.getSalesGroup(), partner.getSalesGroup());
        assertEquals(request.getCorpNo(), partner.getCorpNo());
        assertEquals(request.getBizNo(), partner.getBizNo());
        assertEquals(request.getRrn(), partner.getRrn());
        assertEquals(request.getBizName(), partner.getBizName());
        assertEquals(request.getCeoName(), partner.getCeoName());
        assertEquals(request.getBizType(), partner.getBizType());
        assertEquals(request.getBizItem(), partner.getBizItem());
        assertEquals(request.getTel(), partner.getTel());
        assertEquals(request.getPhone(), partner.getPhone());
        assertEquals(request.getAddressMain(), partner.getAddressMain());
        assertEquals(request.getAddressSub(), partner.getAddressSub());
        assertEquals(request.getFax(), partner.getFax());
        assertEquals(request.getEmail(), partner.getEmail());
        assertEquals(request.getOurManager(), partner.getOurManager());
        assertEquals(request.getPartnerManager(), partner.getPartnerManager());
        assertEquals(request.getMemo(), partner.getMemo());
    }


    //상세조회
    @Test
    void 거래처_상세_조회() {
        //given
        Partner partner = partnerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 거래처 찾기 실패 Id:" + dummyId));

        //when
        PartnerDetailResponse response = partnerService.getPartnerDetail(partner.getId());

        //then
        assertEquals(partner.getId(), response.getId());
        assertEquals(partner.getPartnerType(), response.getPartnerType());
        assertEquals(partner.getSalesGroup(), response.getSalesGroup());
        assertEquals(partner.getCorpNo(), response.getCorpNo());
        assertEquals(partner.getBizNo(), response.getBizNo());
        assertEquals(partner.getRrn(), response.getRrn());
        assertEquals(partner.getBizName(), response.getBizName());
        assertEquals(partner.getCeoName(), response.getCeoName());
        assertEquals(partner.getBizType(), response.getBizType());
        assertEquals(partner.getBizItem(), response.getBizItem());
        assertEquals(partner.getTel(), response.getTel());
        assertEquals(partner.getPhone(), response.getPhone());
        assertEquals(partner.getAddressMain(), response.getAddressMain());
        assertEquals(partner.getAddressSub(), response.getAddressSub());
        assertEquals(partner.getFax(), response.getFax());
        assertEquals(partner.getEmail(), response.getEmail());
        assertEquals(partner.getOurManager(), response.getOurManager());
        assertEquals(partner.getPartnerManager(), response.getPartnerManager());
        assertEquals(partner.getMemo(), response.getMemo());
    }


    //전체조회
    @Test
    void 거래처_전체_조회() {
        //given
        Partner partner = partnerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 거래처 찾기 실패 Id:" + dummyId));


        //when
        List<PartnerSummaryResponse> allPartners = partnerService.getAllPartners();

        //id = dummyid인 거래처 찾기
        PartnerSummaryResponse summaryDto = allPartners.stream().filter(dto -> dto.getId() == partner.getId()).findFirst().orElseThrow();

        //then
        assertEquals(partnerRepository.findAll().size(), allPartners.size());
        assertEquals(partner.getId(), summaryDto.getId());
        assertEquals(partner.getPartnerType(), summaryDto.getPartnerType());
        assertEquals(partner.getSalesGroup(), summaryDto.getSalesGroup());
        assertEquals(partner.getBizNo(), summaryDto.getBizNo());
        assertEquals(partner.getRrn(), summaryDto.getRrn());
        assertEquals(partner.getBizName(), summaryDto.getBizName());
        assertEquals(partner.getCeoName(), summaryDto.getCeoName());
        assertEquals(partner.getTel(), summaryDto.getTel());
        assertEquals(partner.getFax(), summaryDto.getFax());
        assertEquals(partner.getEmail(), summaryDto.getEmail());
        assertEquals(partner.getOurManager(), summaryDto.getOurManager());
        assertEquals(partner.getPartnerManager(), summaryDto.getPartnerManager());
        assertEquals(partner.getMemo(), summaryDto.getMemo());
    }


    @Test
    void 거래처_수정하기() {
        //given
        Partner partner = partnerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 거래처 찾기 실패 Id:" + dummyId));
        UpdatePartnerRequest request = generateUpdatePartnerRequest();

        //when
        partnerService.updatePartner(partner.getId(), request);

        //then
        Partner afterPartner = partnerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 거래처 찾기 실패 Id:" + dummyId));


        assertEquals(partner.getId(), afterPartner.getId());
        assertEquals(request.getPartnerType(), afterPartner.getPartnerType(), "PartnerType 불일치");
        assertEquals(request.getSalesGroup(), afterPartner.getSalesGroup(), "salesGroup 불일치");
        assertEquals(request.getCorpNo(), afterPartner.getCorpNo(), "corpNo 불일치");
        assertEquals(request.getBizNo(), afterPartner.getBizNo(), "bizNo 불일치");
        assertEquals(request.getRrn(), afterPartner.getRrn(), "rrn 불일치");
        assertEquals(request.getBizName(), afterPartner.getBizName(), "bizName 불일치");
        assertEquals(request.getCeoName(), afterPartner.getCeoName(), "ceoName 불일치");
        assertEquals(request.getBizType(), afterPartner.getBizType(), "bizType 불일치");
        assertEquals(request.getBizItem(), afterPartner.getBizItem(), "bizItem 불일치");
        assertEquals(request.getTel(), afterPartner.getTel(), "tel 불일치");
        assertEquals(request.getPhone(), afterPartner.getPhone(), "phone 불일치");
        assertEquals(request.getAddressMain(), afterPartner.getAddressMain(), "addressMain 불일치");
        assertEquals(request.getAddressSub(), afterPartner.getAddressSub(), "addressSub 불일치");
        assertEquals(request.getFax(), afterPartner.getFax(), "fax 불일치");
        assertEquals(request.getEmail(), afterPartner.getEmail(), "email 불일치");
        assertEquals(request.getOurManager(), afterPartner.getOurManager(), "ourManager 불일치");
        assertEquals(request.getPartnerManager(), afterPartner.getPartnerManager(), "PartnerManager 불일치");
        assertEquals(request.getMemo(), afterPartner.getMemo(), "memo 불일치");
    }


    @Test
    void 거래처_수정_데이터_조회() {
        //given
        Partner partner = partnerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 거래처 찾기 실패 Id:" + dummyId));

        //when
        PartnerEditResponse response = partnerService.getPartnerEditData(dummyId);

        //then
        assertTrue(Arrays.stream(PartnerType.values()).allMatch(response.getPartnerTypeOptions()::contains));//모든 사업자 분류 enum이 옵션에 포함되었는지
        assertTrue(Arrays.stream(SalesGroup.values()).allMatch(response.getSalesGroupOptions()::contains));//모든 영업 분류 enum이 옵션에 포함되었는지
        assertEquals(partner.getId(), response.getPartner().getId());//PartnerDetailResponse 확인 (서비스 내부에서 getPartnerDetail()호출함 따라서 자세한 검증 패스)
    }


    @Test
    void 거래처_논리_삭제() {
        //given
        Partner partner = partnerRepository.findById(dummyId).orElseThrow(() -> new IllegalArgumentException("더미 거래처 찾기 실패 Id:" + dummyId));

        //when
        partnerService.deletePartner(partner.getId()); //삭제 수행

        //then
        assertNull(partnerRepository.findById(partner.getId()).orElse(null)); //findById 논리 삭제 테스트
        assertFalse(partnerRepository.existsById(partner.getId())); //existsById 논리 삭제 테스트
    }




    @BeforeEach
    void initData() {
        //더미 거래처 생성
        Partner partner = new Partner(
                PartnerType.INDIVIDUAL,            // PartnerType
                SalesGroup.NONGHYUP,               // salesGroup
                "1234567890123",                   // corpNo
                "9876543210",                      // bizNo
                "900101-1234567",                  // rrn
                "테스트 상호명",                     // bizName
                "홍길동",                           // ceoName
                "도소매",                           // bizType
                "농자재",                           // bizItem
                "02-123-4567",                     // tel
                "010-1234-5678",                   // phone
                "서울특별시 강남구 테헤란로 123",       // addressMain
                "101호",                           // addressSub
                "02-987-6543",                     // fax
                "test@example.com",                // email
                "김우리",                           // ourManager
                "이거래",                           // PartnerManager
                "테스트 메모입니다."                 // memo
        );

        //더미 직원 저장
        partnerRepository.save(partner);

        dummyId = partner.getId();
    }


    private CreatePartnerRequest generateCreatePartnerRequest() {
        return new CreatePartnerRequest(
                PartnerType.INDIVIDUAL,           // PartnerType
                SalesGroup.NONGHYUP,               // salesGroup
                "1234567890123",                   // corpNo
                "9876543210",                      // bizNo
                "900101-1234567",                  // rrn
                "테스트 상호명",                      // bizName
                "홍길동",                           // ceoName
                "도소매",                           // bizType
                "농자재",                           // bizItem
                "02-123-4567",                     // tel
                "010-1234-5678",                   // phone
                "서울특별시 강남구 테헤란로 123",        // addressMain
                "101호",                           // addressSub
                "02-987-6543",                     // fax
                "test@example.com",                // email
                "김우리",                           // ourManager
                "이거래",                           // PartnerManager
                "테스트 메모입니다."                  // memo
        );
    }


    private UpdatePartnerRequest generateUpdatePartnerRequest() {
        return new UpdatePartnerRequest(
                PartnerType.CORPORATION,              // PartnerType
                SalesGroup.ETC,                        // salesGroup
                "1234567890123 업데이트",                 // corpNo
                "9876543210 업데이트",                    // bizNo
                "900101-1234567 업데이트",                // rrn
                "테스트 상호명 업데이트",                    // bizName
                "홍길동 업데이트",                          // ceoName
                "도소매 업데이트",                          // bizType
                "농자재 업데이트",                          // bizItem
                "02-123-4567 업데이트",                    // tel
                "010-1234-5678 업데이트",                  // phone
                "서울특별시 강남구 테헤란로 123 업데이트",       // addressMain
                "101호 업데이트",                           // addressSub
                "02-987-6543 업데이트",                     // fax
                "test@example.com 업데이트",                // email
                "김우리 업데이트",                           // ourManager
                "이거래 업데이트",                           // PartnerManager
                "테스트 메모입니다. 업데이트"                  // memo
        );
    }
}