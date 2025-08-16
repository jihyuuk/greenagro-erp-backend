package erp.greenagro.greenagro_erp_backend.validator;

import erp.greenagro.greenagro_erp_backend.dto.exception.DuplicatedField;
import erp.greenagro.greenagro_erp_backend.exception.DuplicateValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DuplicationValidator {

    private final List<DuplicatedField> conflicts = new ArrayList<>();

    //외부에서 직접 객체 생성 금지
    private DuplicationValidator() {}

    public static void validate(Consumer<DuplicationValidator> consumer) {
        DuplicationValidator dv = new DuplicationValidator();
        consumer.accept(dv);
        dv.throwIfConflicts();
    }


    /**
     * 주어진 조건이 중복일 경우 해당 필드와 값을 기록.
     * @param isDuplicate 중복 판별 조건
     * @param fieldName 필드명
     * @param value 값
     * @return 체이닝을 위한 현재 DuplicationValidator
     */
    public DuplicationValidator check(boolean isDuplicate, String fieldName, Object value) {
        if (isDuplicate) {
            this.conflicts.add(new DuplicatedField(fieldName, value));
        }
        return this;
    }


    //유효성 검사를 마무리하고 예외를 던지는 메서드
    private void throwIfConflicts() {
        if (!conflicts.isEmpty()) {
            throw new DuplicateValueException(this.conflicts);
        }
    }

}