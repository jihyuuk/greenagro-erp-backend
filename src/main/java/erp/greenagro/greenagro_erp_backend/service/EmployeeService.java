package erp.greenagro.greenagro_erp_backend.service;

import erp.greenagro.greenagro_erp_backend.repository.BranchRepository;
import erp.greenagro.greenagro_erp_backend.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;

}
