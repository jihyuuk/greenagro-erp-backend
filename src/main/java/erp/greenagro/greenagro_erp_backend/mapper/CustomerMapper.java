package erp.greenagro.greenagro_erp_backend.mapper;

import erp.greenagro.greenagro_erp_backend.dto.customer.*;
import erp.greenagro.greenagro_erp_backend.model.entity.Customer;
import erp.greenagro.greenagro_erp_backend.model.enums.CustomerType;
import erp.greenagro.greenagro_erp_backend.model.enums.SalesGroup;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CustomerMapper {

    /**
     * CreateCustomerRequest ===> Customer 엔티티로 변환합니다.
     */
    public Customer fromCreate(CreateCustomerRequest request) {

        return new Customer(
                request.getCustomerType(),
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
                request.getCustomerManager(),
                request.getMemo()
        );
    }


    public CreateCustomerResponse toCreate(Customer customer) {

        return new CreateCustomerResponse(
                customer.getId()
        );
    }


    public CustomerSummaryResponse toSummary(Customer customer) {

        return new CustomerSummaryResponse(
                customer.getId(),
                customer.getCustomerType(),
                customer.getSalesGroup(),
                customer.getBizNo(),
                customer.getRrn(),
                customer.getBizName(),
                customer.getCeoName(),
                customer.getTel(),
                customer.getFax(),
                customer.getEmail(),
                customer.getOurManager(),
                customer.getCustomerManager(),
                customer.getMemo()
        );
    }


    public CustomerDetailResponse toDetail(Customer customer) {

        return new CustomerDetailResponse(
                customer.getId(),
                customer.getCustomerType(),
                customer.getSalesGroup(),
                customer.getCorpNo(),
                customer.getBizNo(),
                customer.getRrn(),
                customer.getBizName(),
                customer.getCeoName(),
                customer.getBizType(),
                customer.getBizItem(),
                customer.getTel(),
                customer.getPhone(),
                customer.getAddressMain(),
                customer.getAddressSub(),
                customer.getFax(),
                customer.getEmail(),
                customer.getOurManager(),
                customer.getCustomerManager(),
                customer.getMemo()
        );
    }


    public CustomerEditResponse toEdit(Customer customer) {

        return new CustomerEditResponse(
                toDetail(customer),
                Arrays.stream(CustomerType.values()).toList(),
                Arrays.stream(SalesGroup.values()).toList()
        );
    }

}
