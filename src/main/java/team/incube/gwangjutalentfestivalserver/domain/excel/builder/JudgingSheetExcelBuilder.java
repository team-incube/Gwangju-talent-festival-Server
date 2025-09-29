package team.incube.gwangjutalentfestivalserver.domain.excel.builder;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JudgingSheetExcelBuilder {

    public record ScoreRow(String no, String teamName,
                           Integer completion, Integer creativity, Integer stage) {}

    public record SheetSpec(String sheetName, List<ScoreRow> rows) {}

    public static byte[] buildMulti(String ignoredTitleParam, List<SheetSpec> sheets) {
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            if (sheets == null || sheets.isEmpty()) {
                Sheet sh = wb.createSheet("심사표A");
                createOneSheet(wb, sh, "A", List.of());
            } else {
                for (SheetSpec spec : sheets) {
                    Sheet sh = wb.createSheet(spec.sheetName());
                    String suffix = extractSuffix(spec.sheetName());
                    createOneSheet(wb, sh, suffix, spec.rows());
                }
            }
            wb.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("엑셀 생성 실패", e);
        }
    }

    private static String extractSuffix(String sheetName) {
        if (sheetName == null || sheetName.isBlank()) return "A";
        String s = sheetName.replace("심사표", "").trim();
        return s.isEmpty() ? "A" : s;
    }

    private static void createOneSheet(XSSFWorkbook wb, Sheet sh, String suffix, List<ScoreRow> rows) {
        sh.setDisplayGridlines(false);

        XSSFFont titleFont = wb.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 12);

        XSSFFont headerFont = wb.createFont();
        headerFont.setBold(true);

        XSSFFont judgeFont = wb.createFont();
        judgeFont.setFontHeightInPoints((short) 11);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setFont(titleFont);
        titleStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(220, 230, 241), null));
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCellStyle headerStyle = wb.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(242, 242, 242), null));
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFont(headerFont);

        XSSFCellStyle bodyCenter = wb.createCellStyle();
        bodyCenter.setAlignment(HorizontalAlignment.CENTER);
        bodyCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        bodyCenter.setBorderTop(BorderStyle.THIN);
        bodyCenter.setBorderBottom(BorderStyle.THIN);
        bodyCenter.setBorderLeft(BorderStyle.THIN);
        bodyCenter.setBorderRight(BorderStyle.THIN);

        XSSFCellStyle judgeStyle = wb.createCellStyle();
        judgeStyle.setAlignment(HorizontalAlignment.RIGHT);
        judgeStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        judgeStyle.setFont(judgeFont);
        judgeStyle.setBorderTop(BorderStyle.THIN);
        judgeStyle.setBorderBottom(BorderStyle.THIN);
        judgeStyle.setBorderLeft(BorderStyle.THIN);
        judgeStyle.setBorderRight(BorderStyle.THIN);

        int r = 0;

        Row titleRow = sh.createRow(r++);
        titleRow.setHeightInPoints(28);
        for (int i = 0; i <= 5; i++) titleRow.createCell(i);
        String titleText = "2025 광주학생탈렌트페스티벌 본선 심사위원 개인별 심사표 " + suffix;
        titleRow.getCell(0).setCellValue(titleText);
        titleRow.getCell(0).setCellStyle(titleStyle);

        CellRangeAddress titleRange = new CellRangeAddress(0, 0, 0, 5);
        sh.addMergedRegion(titleRange);
        RegionUtil.setBorderTop(BorderStyle.THIN, titleRange, sh);
        RegionUtil.setBorderBottom(BorderStyle.THIN, titleRange, sh);
        RegionUtil.setBorderLeft(BorderStyle.THIN, titleRange, sh);
        RegionUtil.setBorderRight(BorderStyle.THIN, titleRange, sh);

        sh.createRow(r++);

        int judgeRowIdx = r;
        Row judgeRow = sh.createRow(r++);
        judgeRow.setHeightInPoints(26);
        for (int i = 0; i <= 5; i++) judgeRow.createCell(i);
        judgeRow.getCell(0).setCellValue("심사위원: 소속(                         )  직(                    )  성명(                    )  (서명)");
        judgeRow.getCell(0).setCellStyle(judgeStyle);

        CellRangeAddress judgeRange = new CellRangeAddress(judgeRowIdx, judgeRowIdx, 0, 5);
        sh.addMergedRegion(judgeRange);
        RegionUtil.setBorderTop(BorderStyle.THIN, judgeRange, sh);
        RegionUtil.setBorderBottom(BorderStyle.THIN, judgeRange, sh);
        RegionUtil.setBorderLeft(BorderStyle.THIN, judgeRange, sh);
        RegionUtil.setBorderRight(BorderStyle.THIN, judgeRange, sh);

        sh.createRow(r++);

        Row h = sh.createRow(r++);
        h.setHeightInPoints(34);
        String[] headers = {
                "심사번호", "팀명",
                "완성도 및 표현력\n(40점)",
                "창의력과 구성\n(30점)",
                "무대매너 및 퍼포먼스 등\n(30점)",
                "계\n(100점)"
        };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = h.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        if (rows == null || rows.isEmpty()) {
            Row row = sh.createRow(r++);
            row.setHeightInPoints(24);
            for (int col = 0; col < 6; col++) {
                Cell c = row.createCell(col);
                c.setCellStyle(bodyCenter);
            }
            row.getCell(0).setCellValue(1);
            row.getCell(5).setCellFormula("C"+(row.getRowNum()+1)+"+D"+(row.getRowNum()+1)+"+E"+(row.getRowNum()+1));
        } else {
            for (ScoreRow data : rows) {
                Row row = sh.createRow(r++);
                row.setHeightInPoints(24);

                Cell cNo   = row.createCell(0); cNo.setCellValue(data.no());         cNo.setCellStyle(bodyCenter);
                Cell cTeam = row.createCell(1); cTeam.setCellValue(data.teamName()); cTeam.setCellStyle(bodyCenter);

                setOptionalInt(row.createCell(2), data.completion(), bodyCenter);
                setOptionalInt(row.createCell(3), data.creativity(), bodyCenter);
                setOptionalInt(row.createCell(4), data.stage(), bodyCenter);

                int excelRow = row.getRowNum() + 1;
                Cell cTot = row.createCell(5);
                cTot.setCellFormula("C"+excelRow+"+D"+excelRow+"+E"+excelRow);
                cTot.setCellStyle(bodyCenter);
            }
        }

        int[] widths = {8, 21, 20, 18, 23, 12};
        for (int i = 0; i < widths.length; i++) sh.setColumnWidth(i, widths[i] * 256);
    }

    private static void setOptionalInt(Cell cell, Integer value, CellStyle style) {
        cell.setCellStyle(style);
        if (value != null) cell.setCellValue(value);
    }

    public static String contentDisposition(String filename) {
        String enc = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
        return "attachment; filename*=UTF-8''" + enc;
    }
}