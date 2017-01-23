package com.haier.adp.common.util;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import io.terminus.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Keshao Liu
 * Email: liuks@terminus.io
 * Date: 16/3/14
 * Time: 上午10:16
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
public class ExcelUtil {

    public static final String EXCEL_2003_EXTEND = ".xls";

    public static final String EXCEL_2007_EXTEND = ".xlsx";

    /**
     * 读取excel
     *
     * @param inputStream excel输入流
     * @param password    密码
     * @return 每一行为String数组的列表
     */
    public static List<String[]> readExcel(InputStream inputStream, String password) {
        try {
            Workbook workbook = WorkbookFactory.create(inputStream, password);
            return readWorkbook(workbook);
        } catch (Exception e) {
            log.error("failed to read excel, caused:{}", Throwables.getStackTraceAsString(e));
            throw new ServiceException(e);
        }
    }

    /**
     * 读取workbook
     *
     * @param workbook workbook
     * @return 每一行为String数组的列表
     */
    public static List<String[]> readWorkbook(Workbook workbook) {
        List<String[]> rowList = Lists.newArrayList();
        Sheet sheet = workbook.getSheetAt(0);
        for (Iterator<Row> rows = sheet.iterator(); rows.hasNext(); ) {
            Row row = rows.next();
            int lastCellNum = (int) row.getLastCellNum();
            String[] cells = new String[lastCellNum];
            for (int i = 0; i < lastCellNum; i++) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                        cells[i] = new DateTime(cell.getDateCellValue()).toString("yyyy-MM-dd");
                    } else {
                        row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
                        cells[i] = cell.getStringCellValue();
                    }
                } else {
                    cells[i] = "";
                }
            }
            // 去空行
            boolean blankLine = true;
            for (String cell : cells) {
                if (cell.length() > 0) {
                    blankLine = false;
                    break;
                }
            }
            if (!blankLine) {
                rowList.add(cells);
            }
        }
        return rowList;
    }

    public static Date formatExcelDate(String date) {
        return new DateTime(date).toDate();
    }


    public static void exportTemplateExcel(String title, List<String> initParams, List<String> headers, OutputStream outputStream, String fileExtend) throws IOException {
        // 声明一个工作薄
        Workbook workbook;
        if (EXCEL_2007_EXTEND.equals(fileExtend)) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }
        // 生成一个表格
        Sheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 产生表格一些默认数值列
        Row paramRow = sheet.createRow(0);
        createCells(initParams, fileExtend, paramRow);
        // 产生表格表头
        Row headerRow = sheet.createRow(1);
        createCells(headers, fileExtend, headerRow);
        workbook.write(outputStream);
        workbook.close();
    }

    private static void createCells(List<String> list, String fileExtend, Row row) {
        for (int i = 0; i < list.size(); i++) {
            Cell cell = row.createCell(i);
            RichTextString text;
            if (EXCEL_2007_EXTEND.equals(fileExtend)) {
                text = new XSSFRichTextString(list.get(i));
            } else {
                text = new HSSFRichTextString(list.get(i));
            }
            cell.setCellValue(text);
        }
    }
}
