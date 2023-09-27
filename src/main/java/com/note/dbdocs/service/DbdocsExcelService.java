package com.note.dbdocs.service;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public interface DbdocsExcelService {

    /**
     * workbook의 가장 큰 타이틀 제목의 CellStyle
     * @param workbook
     * @return
     */
    public CellStyle getMainTitleCellStyle(Workbook workbook);

    /**
     * workbook의 작은 제목의 CellStyle
     * @param workbook
     * @return
     */
    public CellStyle getSubTitleCellStyle(Workbook workbook);
}
