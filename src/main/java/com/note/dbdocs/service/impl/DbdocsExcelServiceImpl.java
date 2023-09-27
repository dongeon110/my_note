package com.note.dbdocs.service.impl;

import com.note.dbdocs.service.DbdocsExcelService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.stereotype.Service;

@Service("dbdocsExcelService")
class DbdocsExcelServiceImpl implements DbdocsExcelService {

    @Override
    public CellStyle getMainTitleCellStyle(Workbook workbook) {
        CellStyle mainTitleCellStyle = workbook.createCellStyle();

        // Background Color
        byte[] mainTitleColor = new byte[] {(byte) 242, (byte) 242, (byte) 242}; // 연한 회색
        mainTitleCellStyle.setFillForegroundColor(new XSSFColor(mainTitleColor, null));
        mainTitleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Sort
        mainTitleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        mainTitleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Font
        Font font = workbook.createFont();
        font.setFontName("KoPub돋움체 Bold");
        font.setFontHeight((short)(20*14)); // Font Size
        mainTitleCellStyle.setFont(font);

        return mainTitleCellStyle;
    }

    @Override
    public CellStyle getSubTitleCellStyle(Workbook workbook) {
        CellStyle subTitleCellStyle = workbook.createCellStyle();

        // Background Color
        byte[] subTitleColor = new byte[] {(byte) 217, (byte) 217, (byte) 217}; // 회색
        subTitleCellStyle.setFillForegroundColor(new XSSFColor(subTitleColor, null));
        subTitleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // BorderStyle
        subTitleCellStyle.setBorderRight(BorderStyle.THIN);
        subTitleCellStyle.setBorderLeft(BorderStyle.THIN);
        subTitleCellStyle.setBorderTop(BorderStyle.THIN);
        subTitleCellStyle.setBorderBottom(BorderStyle.THIN);

        // Sort
        subTitleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        subTitleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Font
        Font font = workbook.createFont();
        font.setFontName("KoPub돋움체 Bold");
        font.setFontHeight((short)(20*14)); // Font Size
        subTitleCellStyle.setFont(font);

        return subTitleCellStyle;
    }
}
