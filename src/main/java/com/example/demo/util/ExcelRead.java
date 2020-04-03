package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * @version V1.0.0
 * @ClassName: {@link ExcelRead}
 * @Description: Poi 读取Excel
 * @author: 兰州
 * @date: 2019/7/16 10:12
 * @Copyright:2019 All rights reserved.
 */
@Slf4j
public class ExcelRead {

    /**
     * @param file  文件
     * @param head  头部信息
     * @param value 值信息
     * @param type  Cell 格式
     * @return
     */
    public static List<Map<String, Object>> readEcxcel(File file, String[] head, String[] value, CellType[] type) {
        Map<String, Integer> headValue = new HashMap<>();
        for (int i = 0; i < head.length; i++) {
            headValue.put(head[i], i);
        }
        List<Map<String, Object>> list = new ArrayList<>();
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        Workbook workbook = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            if (".xls".equals(fileType)) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (".xlsx".equals(fileType)) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                log.info("非excel文件");
                return list;
            }
            Sheet sheet;
            Row row;
            Cell cell;
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheet = workbook.getSheetAt(i);
                if (sheet == null) {
                    continue;
                }
                String[] sheetHead = null;
                int firstRow = -1;
                for (int firstRowNum = sheet.getFirstRowNum(), lastRowNum = sheet.getLastRowNum(), j = firstRowNum; j <= lastRowNum; j++) {
                    row = sheet.getRow(j);
                    if (row == null) {
                        // 跳过空行
                        continue;
                    }
                    if (sheetHead == null) {
                        // 如果首行未找到,查看当前是否首行
                        for (int firstCellNum = row.getFirstCellNum(), lastCellNum = row.getLastCellNum(), cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                            cell = row.getCell(cellNum);
                            if (cell != null && cell.getCellType() == CellType.STRING && headValue.containsKey(cell.getStringCellValue().trim())) {
                                // 找到首行
                                firstRow = j;
                                break;
                            }
                        }
                        // 首行未找到,继续下一次
                        if (firstRow == -1) {
                            continue;
                        }
                    }
                    if (firstRow == j) {
                        // 创建首行数组
                        sheetHead = new String[row.getLastCellNum()];
                        for (int firstCellNum = row.getFirstCellNum(), lastCellNum = row.getLastCellNum(), cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                            cell = row.getCell(cellNum);
                            if (cell != null && cell.getCellType() == CellType.STRING && headValue.containsKey(cell.getStringCellValue().trim())) {
                                // 如果headValue内存在该单元格的值,则加入数组
                                sheetHead[cellNum] = cell.getStringCellValue().trim();
                            }
                        }
                        continue;
                    }
                    // 首行未创建,继续寻找
                    if (sheetHead == null) {
                        continue;
                    }
                    Map<String, Object> cellMap = new HashMap<>();
                    for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                        cell = row.getCell(y);
                        if (y >= sheetHead.length || sheetHead[y] == null || headValue.get(sheetHead[y]) == null || cell == null) {
                            //数组越界,或字段不存在则跳过
                            continue;
                        }
                        if (type[headValue.get(sheetHead[y])] == CellType.STRING) {
                            if (cell.getCellType() != CellType.STRING) {
                                cell.setCellType(CellType.STRING);
                            }
                            cellMap.put(value[headValue.get(sheetHead[y])], cell.getStringCellValue().trim());
                        }
                        if (type[headValue.get(sheetHead[y])] == CellType.NUMERIC) {
                            if (cell.getCellType() != CellType.NUMERIC) {
                                cell.setCellType(CellType.STRING);
                                if (isDecimalNum(cell.getStringCellValue().trim())) {
                                    cellMap.put(value[headValue.get(sheetHead[y])], Double.valueOf(cell.getStringCellValue().trim()));
                                }
                                continue;
                            }
                            cellMap.put(value[headValue.get(sheetHead[y])], cell.getNumericCellValue());
                        }
                    }
                    list.add(cellMap);
                }
            }
            return list;
        } catch (IOException e) {
            log.error("文件读取失败", e);
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("流关闭失败", e);
            }
        }
        return list;
    }


    private static final Pattern DOUBLE_PATTERN = Pattern.compile("-?[0-9]+.[0-9]*");

    public static boolean isDecimalNum(String value) {
        if (isNotBlank(value)) {
            return DOUBLE_PATTERN.matcher(value).matches();
        }
        return false;
    }

    private static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
