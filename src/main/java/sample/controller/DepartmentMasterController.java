package sample.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Value;
import sample.context.report.csv.CsvGzWriter;
import sample.model.entity.DepartmentMaster;
import sample.usecase.DepartmentMasterService;

/**
 * DepartmentMasterドメインに関わる Controller。
 */
@RestController
@RequestMapping("/api/department")
public class DepartmentMasterController {
    private final DepartmentMasterService departmentMasterService;

    /** コンストラクタインジェクション */
    public DepartmentMasterController(
            DepartmentMasterService departmentMasterService) {
        this.departmentMasterService = departmentMasterService;
    }

    /** 部署を .csv.gz形式のファイルで出力します。 */
    @GetMapping("/report/csv/export")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Object> exportDepartment() throws IOException {
        CsvGzWriter.execute("department_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                "C:\\home\\5h1m0kayu02", // 仮置き
                departmentMasterService.readAll()
                        .stream()
                        .map(x -> DepartmentMasterCsvDTO.of(x))
                        .collect(Collectors.toList()));
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Value
    public static class DepartmentMasterCsvDTO implements Serializable {

        private static final long serialVersionUID = 1L;

        private String 部署コード; // department_code

        private String 部署名; // department_name

        private String 部署権限; // department_authority

        private String 課コード; // section_code

        private String 課名; // section_name

        private String 班コード; // group_code

        private String 班名; // group_name

        private String 作成日時; // created_date

        private String 最終更新日時; // last_modified_date

        public static DepartmentMasterCsvDTO of(DepartmentMaster entity) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            return new DepartmentMasterCsvDTO(entity.getDepartmentCode(), entity.getDepartmentName(),
                    entity.getDepartmentAuthority(),
                    entity.getSectionCode(), entity.getSectionName(),
                    entity.getGroupCode(), entity.getGroupName(),
                    sdf.format(entity.getCreatedDate()), sdf.format(entity.getLastModifiedDate()));
        }
    }
}
