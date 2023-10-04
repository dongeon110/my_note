package com.note.dbdocs.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class DbdocsCellStyleUtils {

    /**
     * workbook의 가장 큰 타이틀 제목의 CellStyle
     * @param workbook
     * @return
     */
    public static CellStyle getMainTitleCellStyle(Workbook workbook) {
        CellStyle mainTitleCellStyle = workbook.createCellStyle();

        // Background Color
        byte[] mainTitleColor = new byte[] {(byte) 242, (byte) 242, (byte) 242}; // 연한 회색
        mainTitleCellStyle.setFillForegroundColor(new XSSFColor(mainTitleColor, null));
        mainTitleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Sort
        setSortCenter(mainTitleCellStyle);

        // Font
        Font font = workbook.createFont();
        font.setFontName("KoPub돋움체 Bold");
        font.setFontHeight((short)(20*14)); // Font Size
        mainTitleCellStyle.setFont(font);

        return mainTitleCellStyle;
    }

    /**
     * workbook의 작은 제목의 CellStyle
     * @param workbook
     * @return
     */
    public static CellStyle getSubTitleCellStyle(Workbook workbook) {
        CellStyle subTitleCellStyle = workbook.createCellStyle();

        // Background Color
        byte[] subTitleColor = new byte[] {(byte) 217, (byte) 217, (byte) 217}; // 회색
        subTitleCellStyle.setFillForegroundColor(new XSSFColor(subTitleColor, null));
        subTitleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // BorderStyle
        setBorderThin(subTitleCellStyle);

        // Sort
        setSortCenter(subTitleCellStyle);

        // Font
        Font font = workbook.createFont();
        font.setFontName("KoPub돋움체 Bold");
        font.setFontHeight((short)(20*14)); // Font Size
        subTitleCellStyle.setFont(font);

        return subTitleCellStyle;
    }

    /**
     * CellStyle에 Cell THIN테두리를 추가해주는 메서드
     * @param cellStyle 셀 스타일
     */
    private static void setBorderThin(CellStyle cellStyle) {
        // BorderStyle
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
    }

    /**
     * CellStyle에 Cell 가운데 정렬을 추가해주는 메서드
     * @param cellStyle 셀 스타일
     */
    private static void setSortCenter(CellStyle cellStyle) {
        // Sort
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }
}
