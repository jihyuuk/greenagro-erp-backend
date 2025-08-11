package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.warehousesite.CreateWarehouseSiteRequest;
import erp.greenagro.greenagro_erp_backend.dto.warehousesite.CreateWarehouseSiteResponse;
import erp.greenagro.greenagro_erp_backend.dto.warehousesite.UpdateWarehouseSiteRequest;
import erp.greenagro.greenagro_erp_backend.dto.warehousesite.WarehouseSiteResponse;
import erp.greenagro.greenagro_erp_backend.model.entity.WarehouseSite;
import erp.greenagro.greenagro_erp_backend.repository.WarehouseSiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class WarehouseServiceTest {

    @Autowired WarehouseService warehouseService;
    @Autowired WarehouseSiteRepository warehouseSiteRepository;

    //1.창고 지점(warehouse-site)
        //생성 - 이름,코드 중복 x
        //수정 - 이름,코드 중복 x
        //전체조회
        //단건조회

    //2.창고 (warehouse)
        //생성 - 같은 지점 내에 창고는 이름,코드 중복 x
        //수정 - 같은 지점 내에 창고는 이름,코드 중복 x

    //3.존 (warehouse-zone)
        //생성 - 같은 창고 내에 구역은 이름,코드 중복 x
        //수정 - 같은 창고 내에 구역은 이름,코드 중복 x


    @Test
    void 창고지점_생성_정상(){
        //given
        CreateWarehouseSiteRequest request = new CreateWarehouseSiteRequest("일산지점", "경기도 고양시", "ISN");

        //when
        CreateWarehouseSiteResponse response = warehouseService.createSite(request);

        //then
        //저장된 창고지점 찾기
        WarehouseSite warehouseSite = warehouseSiteRepository.findById(response.getWarehouseSiteId()).orElseThrow();
        assertNotNull(response.getWarehouseSiteId()); //id 반환 확인
        assertEquals(warehouseSite.getId(), response.getWarehouseSiteId());
        assertEquals(request.getName(), warehouseSite.getName());
        assertEquals(request.getAddress(), warehouseSite.getAddress());
        assertEquals(request.getCode(), warehouseSite.getCode());
    }


    @Test
    void 창고지점_생성_중복(){
        //given
        //이름중복, 코드중복, 둘다 중복
        CreateWarehouseSiteRequest requestDuplicateName = new CreateWarehouseSiteRequest("김포지점", "경기도 김포시 하성면", "ICN");
        CreateWarehouseSiteRequest requestDuplicateCode = new CreateWarehouseSiteRequest("인천지점", "경기도 김포시 하성면", "GMP");
        CreateWarehouseSiteRequest requestDuplicateAll = new CreateWarehouseSiteRequest("김포지점", "경기도 김포시 하성면", "GMP");

        //when-then
        assertThrows(IllegalArgumentException.class, ()->warehouseService.createSite(requestDuplicateName));
        assertThrows(IllegalArgumentException.class, ()->warehouseService.createSite(requestDuplicateCode));
        assertThrows(IllegalArgumentException.class, ()->warehouseService.createSite(requestDuplicateAll));
    }


    @Test
    void 창고지점_수정_정상(){
        //given
        WarehouseSite beforeSite = warehouseSiteRepository.save(new WarehouseSite("수정 확인용", "", "AAA"));
        UpdateWarehouseSiteRequest request = new UpdateWarehouseSiteRequest("일산지점", "경기도 고양시", "ISN");

        //when
        warehouseService.updateSite(beforeSite.getId(), request);

        //then
        WarehouseSite afterSite = warehouseSiteRepository.findById(beforeSite.getId()).orElseThrow();
        assertEquals(request.getName(), afterSite.getName());
        assertEquals(request.getAddress(), afterSite.getAddress());
        assertEquals(request.getCode(), afterSite.getCode());
    }


    @Test
    void 창고지점_수정_중복(){
        //given
        //이름중복, 코드중복, 둘다 중복
        WarehouseSite site = warehouseSiteRepository.save(new WarehouseSite("중복 확인용", "", "AAA"));
        UpdateWarehouseSiteRequest requestDuplicateName = new UpdateWarehouseSiteRequest("김포지점", "", "ISN");
        UpdateWarehouseSiteRequest requestDuplicateCode = new UpdateWarehouseSiteRequest("일산지점", "", "GMP");
        UpdateWarehouseSiteRequest requestDuplicateAll = new UpdateWarehouseSiteRequest("김포지점", "", "GMP");

        //when-then
        assertThrows(IllegalArgumentException.class, () -> warehouseService.updateSite(site.getId(), requestDuplicateName));
        assertThrows(IllegalArgumentException.class, () -> warehouseService.updateSite(site.getId(), requestDuplicateCode));
        assertThrows(IllegalArgumentException.class, () -> warehouseService.updateSite(site.getId(), requestDuplicateAll));
    }


    @Test
    void 창고지점_전체_조회(){
        //given
        List<WarehouseSite> expectedSites = warehouseSiteRepository.findAll();

        //when
        List<WarehouseSiteResponse> actualSites = warehouseService.getAllSites();

        //then
        assertEquals(expectedSites.size(), actualSites.size()); //개수비교
        //필드값 비교
        for (int i = 0; i < expectedSites.size(); i++) {
            WarehouseSite expected = expectedSites.get(i);
            WarehouseSiteResponse actual = actualSites.get(i);

            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getAddress(), actual.getAddress());
            assertEquals(expected.getCode(), actual.getCode());
        }
    }

    
    @Test
    void 창고지점_단건_조회(){
        //given
        WarehouseSite expectedSite = warehouseSiteRepository.save(new WarehouseSite("단건 조회용", "", "ONLY_ONE"));

        //when
        WarehouseSiteResponse actualSite = warehouseService.getSite(expectedSite.getId());

        //then
        assertEquals(expectedSite.getId(), actualSite.getId());
        assertEquals(expectedSite.getName(), actualSite.getName());
        assertEquals(expectedSite.getAddress(), actualSite.getAddress());
        assertEquals(expectedSite.getCode(), actualSite.getCode());
    }



    @BeforeEach
    void initWarehouseSite(){
        warehouseSiteRepository.save(new WarehouseSite("김포지점", "김포시 하성면", "GMP"));
        warehouseSiteRepository.save(new WarehouseSite("인천지점", "인천시 벌말로", "ICN"));
    }
}