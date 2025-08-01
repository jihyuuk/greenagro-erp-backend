package erp.greenagro.greenagro_erp_backend.dto.employee;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ResignEmployeeRequest {

    @NotNull
    private LocalDate resignDate; //퇴사일

}
