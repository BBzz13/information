## 导入maven地址

```xml
        <!-- https://mvnrepository.com/artifact/org.apache.poi/poi -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>3.17</version>
        </dependency>
```

## 1、简单的生成excel

```java

@GetMapping(value = "/excel")
public void outExperienceExcel(HttpServletResponse response) throws IOException {
    //表的用户名
    String fileName = "用户报表";
    //创建HSSFWorkbook对象(excel的文档对象)
    HSSFWorkbook workbook = new HSSFWorkbook();
    //创建sheet对象（excel的表单）
    HSSFSheet sheet = wb.createSheet("sheet1");

    //创建第一行，这里即是表头。行的最小值是0，代表每一行，上限没研究过，可参考官方的文档
    //createRow(int a) 创建表的第几行数据类，0在表格中代表第一行数据。
    HSSFRow row1 = sheet.createRow(0);

    //在这一行创建单元格，并且将这个单元格的内容设为“账号”，下面同理。
    //列的最小值标识也是0 
    row1.createCell(0).setCellValue("id");
    row1.createCell(1).setCellValue("姓名");
    row1.createCell(2).setCellValue("性别");
    row1.createCell(3).setCellValue("年龄");

    //输出Excel文件
    OutputStream output = response.getOutputStream();
    response.reset();
    //                                                                  设置字符编码格式
    response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + ".xls");
    response.setContentType("application/x-xls");
    wb.write(output);
    output.close();
}
```
至此我们就写好一个生成excel表格的接口。但这个方法有些缺陷，比如不能修改表的格式，放入少量数据没什么问题，但是放入大量数据就需要写很多重复的代码。用来填入数据，至此我们需要封装一个工具类，用于生成excel。
## 2、生成Excel表格工具类
```java
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils{

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
     * 创建excel中的sheet表，如果需要多张表就创建多次
     * @param workbook excel
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
```
下面就是工具类如何使用：
```
@GetMapping(value = "/excel")
public void outAllExperienceExcel(HttpServletResponse response, HttpServletRequest request,StatisticalVO statisticalVO) {
    //创建集合List，此处应该为你查询到的数据，或者你也可以手动创建一个list，只需要把User类换成你自己的实体类就ok了
    //User内的参数 id，name，sex，age 对应上下面 beanProperty2
    List<User> list = new ArrayLiet<>();

    //创建HSSFWorkbook对象(excel的文档对象)
    HSSFWorkbook workbook = new HSSFWorkbook();
    //创建excel的表名和表单（excel的表单）
    ExcelUtils.makeExcelSheet(workbook, "用户报表", 4);
    //此行数据为第二行数据，用来放入列名
    String[] beanProperty = {"id","姓名","性别","年龄"};
    
    ExcelUtils.makeSecondHead(workbook, beanProperty);
    //Excel表内数据从第三行开始，此行数据为传入数据的第一行
    String[] beanProperty2 = {"id","name","sex","age"};
    
    ExcelUtils.exportExcelData(workbook, list, beanProperty2);

    //导入excel
    try {
        String userAgent = request.getHeader("User-Agent");
        String name;
        if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("like Gecko")) {
            //IE浏览器处理
            name = java.net.URLEncoder.encode("用户报表.xls", "UTF-8");
        } else {
            name = new String("用户报表.xls".getBytes("UTF-8"), "ISO-8859-1");
        }

        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename="+name);
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        workbook.write(output);
        output.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

}
```
生成excel表格的接口就写好了。
## 3、一个Excel文件中生成多个表单
上述工具类可以继续使用，其中就有生成多个表单的方法
```
@GetMapping("/excel")
public void getOutWeekExcel(String startTime, String endTime, HttpServletResponse response, HttpServletRequest request) {
    List<User> list1 = new ArrayList<>();
    List<User> list2= new ArrayList<>();

    HSSFWorkbook workbook = new HSSFWorkbook();
    //第一张报表
    ExcelUtils.makeExcelSheet(workbook, "用户报表1", 4);
    String[] beanProperty = {"姓名","id","年龄","性别"};
    ExcelUtils.makeSecondHead(workbook, beanProperty,0);
    String[] beanProperty2 = {"name","id","age","sex"};
    ExcelUtils.exportExcelData(workbook, list1, beanProperty2,0);


    //第二张报表
    ExcelUtils.makeExcelSheet(workbook, "用户报表2", 4);
    String[] experienceProperty = {"id","姓名","性别","年龄"};
    ExcelUtils.makeSecondHead(workbook, list2,1);
    String[] experienceProperty2 = {"id","name","sex","age"};
    ExcelUtils.exportExcelData(workbook, list2, experienceProperty2,1);


    //输出excel
    try {
        String userAgent = request.getHeader("User-Agent");
        String name;
        if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("like Gecko")) {
            //IE浏览器处理
            name = java.net.URLEncoder.encode("用户报表.xls", "UTF-8");
        } else {
            name = new String("用户报表.xls".getBytes("UTF-8"), "ISO-8859-1");
        }

        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename="+name);
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        workbook.write(output);
        output.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```