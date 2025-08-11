package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.warehouse.CreateWarehouseRequest;
import erp.greenagro.greenagro_erp_backend.dto.warehouse.CreateWarehouseResponse;
import erp.greenagro.greenagro_erp_backend.dto.warehousesite.CreateWarehouseSiteRequest;
import erp.greenagro.greenagro_erp_backend.dto.warehousesite.CreateWarehouseSiteResponse;
import erp.greenagro.greenagro_erp_backend.dto.warehousesite.UpdateWarehouseSiteRequest;
import erp.greenagro.greenagro_erp_backend.dto.warehousesite.WarehouseSiteResponse;
import erp.greenagro.greenagro_erp_backend.dto.warehousezone.CreateWarehouseZoneRequest;
import erp.greenagro.greenagro_erp_backend.dto.warehousezone.CreateWarehouseZoneResponse;
import erp.greenagro.greenagro_erp_backend.model.entity.Warehouse;
import erp.greenagro.greenagro_erp_backend.model.entity.WarehouseSite;
import erp.greenagro.greenagro_erp_backend.model.entity.WarehouseZone;
import erp.greenagro.greenagro_erp_backend.repository.WarehouseRepository;
import erp.greenagro.greenagro_erp_backend.repository.WarehouseSiteRepository;
import erp.greenagro.greenagro_erp_backend.repository.WarehouseZoneRepository;
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
    @Autowired WarehouseRepository warehouseRepository;
    @Autowired WarehouseZoneRepository warehouseZoneRepository;

    private Long siteId1;
    private Long siteId2;
    private Long warehouseId1;
    private Long warehouseId2;

    //1.창고 지점(warehouse-site)
        //생성 - 이름,코드 중복 x
        //수정 - 이름,코드 중복 x
        //전체조회
        //단건조회

    //2.창고 (warehouse)
        //생성 - 같은 지점 내에 창고는 이름,코드 중복 x (다른 지점은 가능)
        //수정 - 같은 지점 내에 창고는 이름,코드 중복 x (다른 지점은 가능)

    //3.존 (warehouse-zone)
        //생성 - 같은 창고 내에 구역은 이름,코드 중복 x (다른 창고는 가능)
        //수정 - 같은 창고 내에 구역은 이름,코드 중복 x (다른 창고는 가능)


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



    @Test
    void 창고_생성_정상(){
        //given
        CreateWarehouseRequest request = new CreateWarehouseRequest(siteId1, "1번창고", "01");

        //when
        CreateWarehouseResponse response = warehouseService.createWarehouse(request);

        //then
        Warehouse warehouse = warehouseRepository.findById(response.getWarehouseId()).orElseThrow();

        assertNotNull(response.getWarehouseId()); //id 반환 확인
        assertEquals(warehouse.getId(), response.getWarehouseId());
        assertEquals(request.getName(), warehouse.getName());
        assertEquals(request.getCode(), warehouse.getCode());
    }


    @Test
    void 창고_생성_중복(){
        //같은 지점내에 이름 or 코드 중복 체크
        //다른 지점의 창고와 중복은 상관없음

        //given
        CreateWarehouseRequest request = new CreateWarehouseRequest(siteId1, "1번창고", "01");
        CreateWarehouseRequest requestDuplicateName = new CreateWarehouseRequest(siteId1, "1번창고", "02");
        CreateWarehouseRequest requestDuplicateCode = new CreateWarehouseRequest(siteId1, "2번창고", "01");
        CreateWarehouseRequest otherSiteDuplicateAll = new CreateWarehouseRequest(siteId2, "1번창고", "01");

        //when
        warehouseService.createWarehouse(request);

        //then
        assertThrows(IllegalArgumentException.class, () -> warehouseService.createWarehouse(requestDuplicateName));
        assertThrows(IllegalArgumentException.class, () -> warehouseService.createWarehouse(requestDuplicateCode));
        warehouseService.createWarehouse(otherSiteDuplicateAll); // 다른 지점의 창고와의 중복은 상관없음
    }



    @Test
    void 구역_생성_정상(){
        //given
        CreateWarehouseZoneRequest request = new CreateWarehouseZoneRequest(1L, "1번창고", "01");

        //when
        CreateWarehouseZoneResponse response = warehouseService.createWarehouseZone(request);

        //then
        WarehouseZone zone = warehouseZoneRepository.findById(response.getWarehouseZoneId()).orElseThrow();

        assertNotNull(response.getWarehouseZoneId()); //id 반환 확인
        assertEquals(zone.getId(), response.getWarehouseZoneId());
        assertEquals(request.getName(), zone.getName());
        assertEquals(request.getCode(), zone.getCode());
    }


    @Test
    void 구역_생성_중복(){
        //같은 창고내에 이름 or 코드 중복 체크
        //다른 창고의 구역과 중복은 상관없음

        //given
        CreateWarehouseZoneRequest request = new CreateWarehouseZoneRequest(warehouseId1, "1번창고", "01");
        CreateWarehouseZoneRequest requestDuplicateName = new CreateWarehouseZoneRequest(warehouseId1, "1번창고", "02");
        CreateWarehouseZoneRequest requestDuplicateCode = new CreateWarehouseZoneRequest(warehouseId1, "2번창고", "01");
        CreateWarehouseZoneRequest otherWarehouseDuplicateAll = new CreateWarehouseZoneRequest(warehouseId2, "1번창고", "01");

        //when
        warehouseService.createWarehouseZone(request);

        //then
        assertThrows(IllegalArgumentException.class, () -> warehouseService.createWarehouseZone(requestDuplicateName));
        assertThrows(IllegalArgumentException.class, () -> warehouseService.createWarehouseZone(requestDuplicateCode));
        warehouseService.createWarehouseZone(otherWarehouseDuplicateAll); // 다른 지점의 창고와의 중복은 상관없음
    }




    @BeforeEach
    void initData(){
        WarehouseSite site1 = warehouseSiteRepository.save(new WarehouseSite("김포지점", "김포시 하성면", "GMP"));
        WarehouseSite site2 = warehouseSiteRepository.save(new WarehouseSite("인천지점", "인천시 벌말로", "ICN"));

        Warehouse warehouse1 = warehouseRepository.save(new Warehouse(site1, "N번창고", "0N"));
        Warehouse warehouse2 = warehouseRepository.save(new Warehouse(site2, "N번창고", "0N"));

        siteId1 = site1.getId();
        siteId2 = site2.getId();
        warehouseId1 = warehouse1.getId();
        warehouseId2 = warehouse2.getId();
    }
}