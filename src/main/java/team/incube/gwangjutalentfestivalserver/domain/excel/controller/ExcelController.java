package team.incube.gwangjutalentfestivalserver.domain.excel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.incube.gwangjutalentfestivalserver.domain.excel.builder.JudgingSheetExcelBuilder;
import team.incube.gwangjutalentfestivalserver.domain.excel.usecase.JudgingSheetExcelUsecase;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final JudgingSheetExcelUsecase judgingSheetExcelUsecase;

    @GetMapping("/judging-sheets.xlsx")
    public ResponseEntity<byte[]> downloadAll() {
        byte[] bytes = judgingSheetExcelUsecase.execute();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        JudgingSheetExcelBuilder.contentDisposition("심사표_전체(심사표A~F).xlsx"))
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }
}
