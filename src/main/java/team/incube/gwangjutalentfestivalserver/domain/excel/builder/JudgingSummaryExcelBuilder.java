package team.incube.gwangjutalentfestivalserver.domain.excel.builder;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class JudgingSummaryExcelBuilder {

    public record SummaryRow(
            String no,
            List<Integer> judgeScores,
            Integer total,
            Integer rank
    ) {}

    public static byte[] build(String title) {
        return build(title, Collections.emptyList());
    }

    public static byte[] build(String title, List<SummaryRow> rows) {
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sh = wb.createSheet("심사집계표");
            sh.setDisplayGridlines(false);

            // ===== Fonts =====
            XSSFFont titleFont = wb.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 12);

            XSSFFont headerFont = wb.createFont();
            headerFont.setBold(true);

            XSSFFont infoFont = wb.createFont();
            infoFont.setFontHeightInPoints((short) 11);
            infoFont.setBold(true);

            // ===== Styles =====
            XSSFCellStyle titleStyle = wb.createCellStyle();
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleStyle.setFont(titleFont);
            titleStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(220,230,241), null));
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFCellStyle infoStyle = wb.createCellStyle();
            infoStyle.setAlignment(HorizontalAlignment.RIGHT);
            infoStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            infoStyle.setWrapText(true);
            infoStyle.setFont(infoFont);

            XSSFCellStyle headerStyle = wb.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setWrapText(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(242,242,242), null));
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setAllThin(headerStyle);

            XSSFCellStyle bodyCenter = wb.createCellStyle();
            bodyCenter.setAlignment(HorizontalAlignment.CENTER);
            bodyCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            setAllThin(bodyCenter);

            int r = 0;

            // ===== Title =====
            Row titleRow = sh.createRow(r++);
            titleRow.setHeightInPoints(36); // 🔥 박스 크게
            for (int i = 0; i <= 8; i++) titleRow.createCell(i);
            titleRow.getCell(0).setCellValue(
                    (title != null && !title.isBlank())
                            ? title
                            : "2025학년도 『光탈페(광주학생탈렌트페스티벌)』 본선 심사위원 심사집계표"
            );
            titleRow.getCell(0).setCellStyle(titleStyle);
            CellRangeAddress titleRange = new CellRangeAddress(0, 0, 0, 8);
            sh.addMergedRegion(titleRange);
            boxThin(sh, titleRange);

            // ===== 확인자/작성자 =====
            Row infoRow = sh.createRow(r++);
            infoRow.setHeightInPoints(40); // 🔥 크게
            for (int i = 0; i <= 8; i++) infoRow.createCell(i);
            infoRow.getCell(0).setCellValue(
                    "확인자: 체육예술인성교육과 사무관 양정숙   (서명)\n" +
                            "작성자: 체육예술인성교육과 장학사 이승남   (서명)"
            );
            infoRow.getCell(0).setCellStyle(infoStyle);
            CellRangeAddress infoRange = new CellRangeAddress(1, 1, 0, 8);
            sh.addMergedRegion(infoRange);
            boxThin(sh, infoRange);

            // ===== Header =====
            Row h = sh.createRow(r++);
            h.setHeightInPoints(40); // 🔥 헤더 박스 키움
            String[] headers = {
                    "심사번호",
                    "심사위원\n(A)", "심사위원\n(B)", "심사위원\n(C)",
                    "심사위원\n(D)", "심사위원\n(E)", "심사위원\n(F)",
                    "산출점수", "순위"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell c = h.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }

            // ===== Body =====
            if (rows.isEmpty()) {
                for (int i = 1; i <= 10; i++) {
                    Row row = sh.createRow(r++);
                    row.setHeightInPoints(34); // 🔥 데이터 행 박스 크게
                    for (int col = 0; col < headers.length; col++) {
                        Cell c = row.createCell(col);
                        c.setCellStyle(bodyCenter);
                    }
                    row.getCell(0).setCellValue(i);
                }
            } else {
                for (SummaryRow sr : rows) {
                    Row row = sh.createRow(r++);
                    row.setHeightInPoints(34); // 🔥 데이터 행 박스 크게
                    Cell c0 = row.createCell(0);
                    c0.setCellValue(sr.no());
                    c0.setCellStyle(bodyCenter);

                    for (int j = 0; j < 6; j++) {
                        Cell cj = row.createCell(1 + j);
                        cj.setCellStyle(bodyCenter);
                        Integer s = (j < sr.judgeScores().size()) ? sr.judgeScores().get(j) : null;
                        if (s != null) cj.setCellValue(s);
                    }

                    Cell cTot = row.createCell(7);
                    cTot.setCellStyle(bodyCenter);
                    if (sr.total() != null) cTot.setCellValue(sr.total());

                    Cell cRank = row.createCell(8);
                    cRank.setCellStyle(bodyCenter);
                    if (sr.rank() != null) cRank.setCellValue(sr.rank());
                }
            }

            // ===== Column widths =====
            int[] widths = {10, 14, 14, 14, 14, 14, 14, 14, 10};
            for (int i = 0; i < widths.length; i++) sh.setColumnWidth(i, widths[i] * 256);

            wb.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("심사집계표 생성 실패", e);
        }
    }

    private static void setAllThin(CellStyle s) {
        s.setBorderTop(BorderStyle.THIN);
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);
    }

    private static void boxThin(Sheet sh, CellRangeAddress range) {
        RegionUtil.setBorderTop(BorderStyle.THIN, range, sh);
        RegionUtil.setBorderBottom(BorderStyle.THIN, range, sh);
        RegionUtil.setBorderLeft(BorderStyle.THIN, range, sh);
        RegionUtil.setBorderRight(BorderStyle.THIN, range, sh);
    }

    public static String contentDisposition(String filename) {
        String enc = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
        return "attachment; filename*=UTF-8''" + enc;
    }
}