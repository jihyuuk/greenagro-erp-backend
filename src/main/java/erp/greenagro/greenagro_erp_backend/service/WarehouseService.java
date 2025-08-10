package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.dto.warehouse.CreateWarehouseRequest;
import erp.greenagro.greenagro_erp_backend.dto.warehouse.CreateWarehouseResponse;
import erp.greenagro.greenagro_erp_backend.dto.warehouse.UpdateWarehouseRequest;
import erp.greenagro.greenagro_erp_backend.dto.warehousesite.CreateWarehouseSiteRequest;
import erp.greenagro.greenagro_erp_backend.dto.warehousesite.CreateWarehouseSiteResponse;
import erp.greenagro.greenagro_erp_backend.dto.warehousesite.UpdateWarehouseSiteRequest;
import erp.greenagro.greenagro_erp_backend.dto.warehousesite.WarehouseSiteResponse;
import erp.greenagro.greenagro_erp_backend.model.entity.Warehouse;
import erp.greenagro.greenagro_erp_backend.model.entity.WarehouseSite;
import erp.greenagro.greenagro_erp_backend.repository.WarehouseRepository;
import erp.greenagro.greenagro_erp_backend.repository.WarehouseSiteRepository;
import erp.greenagro.greenagro_erp_backend.repository.WarehouseZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseSiteRepository warehouseSiteRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseZoneRepository warehouseZoneRepository;


    //site 생성, 수정, 조회, 삭제 <- 나중에
    @Transactional
    public CreateWarehouseSiteResponse createSite(CreateWarehouseSiteRequest request) {
        //1. 검증
        //1-1. 이름 중복 검사
        if (warehouseSiteRepository.existsByName(request.getName()))
            throw new IllegalArgumentException("중복된 창고 지점명 입니다. 이름:" + request.getName());
        //1-2. code 중복 검사
        if (warehouseSiteRepository.existsByCode(request.getCode()))
            throw new IllegalArgumentException("중복된 창고 지점 코드 입니다. 코드:" + request.getCode());

        //2. site 생성
        WarehouseSite warehouseSite = new WarehouseSite(
                request.getName(),
                request.getAddress(),
                request.getCode()
        );

        //3. 저장
        warehouseSiteRepository.save(warehouseSite);

        //4. 반환
        return new CreateWarehouseSiteResponse(warehouseSite.getId());
    }


    //창고지점 수정
    @Transactional
    public void updateSite(Long id, UpdateWarehouseSiteRequest request) {
        //1. 찾기
        WarehouseSite warehouseSite = warehouseSiteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고지점 입니다. id:" + id));

        //2. 검증
        //2-1. 이름 중복 검사
        if (!request.getName().equals(warehouseSite.getName()) && warehouseSiteRepository.existsByName(request.getName()))
            throw new IllegalArgumentException("중복된 창고 지점명 입니다. 이름:" + request.getName());
        //2-2. code 중복 검사
        if (!request.getCode().equals(warehouseSite.getCode()) && warehouseSiteRepository.existsByCode(request.getCode()))
            throw new IllegalArgumentException("중복된 창고 지점 코드 입니다. 코드:" + request.getCode());

        //3. 수정
        warehouseSite.update(
                request.getName(),
                request.getAddress(),
                request.getCode()
        );
    }


    //창고지점 전체 조회
    @Transactional(readOnly = true)
    public List<WarehouseSiteResponse> getAllSites() {

        return warehouseSiteRepository.findAll().stream().map(warehouseSite ->
                new WarehouseSiteResponse(
                        warehouseSite.getId(),
                        warehouseSite.getName(),
                        warehouseSite.getAddress(),
                        warehouseSite.getCode()
                )
        ).toList();
    }


    //창고지점 단건 조회
    @Transactional(readOnly = true)
    public WarehouseSiteResponse getSite(Long id) {

        WarehouseSite warehouseSite = warehouseSiteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고지점 입니다. id:" + id));

        return new WarehouseSiteResponse(
                warehouseSite.getId(),
                warehouseSite.getName(),
                warehouseSite.getAddress(),
                warehouseSite.getCode()
        );
    }


    //창고 생성, 수정, 삭제
    //창고 생성
    @Transactional
    public CreateWarehouseResponse createWarehouse(CreateWarehouseRequest request){

        //1.지점 찾기
        WarehouseSite warehouseSite = warehouseSiteRepository.findById(request.getWarehouseSiteId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고 지점 입니다. id:" + request.getWarehouseSiteId()));

        //2.검증
        if(warehouseRepository.existsByWarehouseSiteAndName(warehouseSite, request.getName()))
            throw new IllegalArgumentException("중복된 창고명 입니다. 이름:"+request.getName());
        if(warehouseRepository.existsByWarehouseSiteAndCode(warehouseSite, request.getCode()))
            throw new IllegalArgumentException("중복되는 창고 code 입니다. code:"+request.getCode());

        //3.생성
        Warehouse warehouse = new Warehouse(warehouseSite, request.getName(), request.getCode());

        //4.저장
        warehouseRepository.save(warehouse);

        //5.반환
        return new CreateWarehouseResponse(warehouse.getId());
    }

    
    //창고 수정
    @Transactional
    public void updateWarehouse(Long id, UpdateWarehouseRequest request){
        //1. 기본 창고 찾기
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고입니다. id:" + id));

        //2. 검증
        if(!warehouse.getName().equals(request.getName()) && warehouseRepository.existsByWarehouseSiteAndName(warehouse.getWarehouseSite(), request.getName()))
            throw new IllegalArgumentException("중복된 창고명 입니다. 이름:"+request.getName());
        if(!warehouse.getCode().equals(request.getCode()) && warehouseRepository.existsByWarehouseSiteAndCode(warehouse.getWarehouseSite(), request.getCode()))
            throw new IllegalArgumentException("중복되는 창고 code 입니다. code:"+request.getCode());

        //3. 업데이트
        warehouse.update(request.getName(), request.getCode());
    }



    //존 생성, 수정, 삭제
}
