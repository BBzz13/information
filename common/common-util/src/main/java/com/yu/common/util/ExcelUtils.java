package com.yu.common.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    /**
     * 创建excel中的sheet表，如果需要多张表就创建多次
     * @param workbook 创建HSSFWorkbook对象(excel的文档对象)
     * @param title 表名
     * @param cellRangeAddressLength 表长度
     * @return
     */
    public static HSSFWorkbook makeExcelSheet(HSSFWorkbook workbook,String title, int cellRangeAddressLength){
        //创建表格的公共样式
        HSSFCellStyle styleTitle = createStyle(workbook, (short)16);
        //sheet表明
        HSSFSheet sheet = workbook.createSheet(title);
        //默认宽度
        sheet.setDefaultColumnWidth(25);
        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, cellRangeAddressLength);
        //合并单元格
        sheet.addMergedRegion(cellRangeAddress);
        HSSFRow rowTitle = sheet.createRow(0);
        rowTitle.setHeight((short)1000);
        HSSFCell cellTitle = rowTitle.createCell(0);
        // 为标题设置背景颜色
        styleTitle.setWrapText(true);
        cellTitle.setCellStyle(styleTitle);
        cellTitle.setCellValue(title);

        return workbook;
    }


    /**
     * 设定二级标题
     * @param workbook
     * @param secondTitles
     * @return
     */
    public static HSSFWorkbook makeSecondHead(HSSFWorkbook workbook, String[] secondTitles){
        // 创建用户属性栏
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow rowField = sheet.createRow(1);
        HSSFCellStyle styleField = createStyle(workbook, (short)13);
        for (int i = 0; i < secondTitles.length; i++) {
            HSSFCell cell = rowField.createCell(i);
            cell.setCellValue(secondTitles[i]);
            cell.setCellStyle(styleField);
        }
        return workbook;
    }
    /**
     * 提取公共的样式
     * @param workbook
     * @param fontSize
     * @return
     */
    private static HSSFCellStyle createStyle(HSSFWorkbook workbook, short fontSize){
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 创建一个字体样式
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        style.setFont(font);
        return style;
    }

    /**
     * 插入数据
     * @param workbook
     * @param dataList
     * @param beanPropertys
     * @return
     */
    public static <T> HSSFWorkbook exportExcelData(HSSFWorkbook workbook, List<T> dataList, String[] beanPropertys) {
        HSSFSheet sheet = workbook.getSheetAt(0);
        // 填充数据
        HSSFCellStyle styleData = workbook.createCellStyle();
        styleData.setAlignment(HorizontalAlignment.CENTER);
        styleData.setVerticalAlignment(VerticalAlignment.CENTER);


        for (int j = 0; j < dataList.size(); j++) {
            HSSFRow rowData = sheet.createRow(j + 2);
            T t = dataList.get(j);
            for(int k=0; k<beanPropertys.length; k++){
                Map map = objectToMap01(t);
                Object value = map.get(beanPropertys[k]);
                HSSFCell cellData = rowData.createCell(k);

                cellData.setCellValue(value == null?"":value.toString());

                cellData.setCellStyle(styleData);
            }
        }
        return workbook;
    }
    public static Map<String, Object> objectToMap01(Object obj) {
        if (obj == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        }catch (Exception e){

        }
        return map;
    }


    /**
     * 设定二级标题
     * @param workbook HSSFWorkbook
     * @param secondTitles 标题头
     * @param sheetInt 操作第几张表 从0开始
     * @return HSSFWorkbook
     */
    public static HSSFWorkbook makeSecondHead(HSSFWorkbook workbook, String[] secondTitles,int sheetInt){
        // 创建用户属性栏
        HSSFSheet sheet = workbook.getSheetAt(sheetInt);
        HSSFRow rowField = sheet.createRow(1);
        HSSFCellStyle styleField = createStyle(workbook, (short)13);
        for (int i = 0; i < secondTitles.length; i++) {
            HSSFCell cell = rowField.createCell(i);
            cell.setCellValue(secondTitles[i]);
            cell.setCellStyle(styleField);
        }
        return workbook;
    }

    /**
     * 插入数据
     * @param workbook
     * @param dataList
     * @param beanPropertys
     * @return
     */
    public static <T> HSSFWorkbook exportExcelData(HSSFWorkbook workbook, List<T> dataList, String[] beanPropertys,int sheetInt) {
        HSSFSheet sheet = workbook.getSheetAt(sheetInt);
        // 填充数据
        HSSFCellStyle styleData = workbook.createCellStyle();
        styleData.setAlignment(HorizontalAlignment.CENTER);
        styleData.setVerticalAlignment(VerticalAlignment.CENTER);


        for (int j = 0; j < dataList.size(); j++) {
            HSSFRow rowData = sheet.createRow(j + 2);
            T t = dataList.get(j);
            for(int k=0; k<beanPropertys.length; k++){
                Map map = objectToMap01(t);
                Object value = map.get(beanPropertys[k]);
                HSSFCell cellData = rowData.createCell(k);

                cellData.setCellValue(value == null?"":value.toString());

                cellData.setCellStyle(styleData);
            }
        }
        return workbook;
    }
}
