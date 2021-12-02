package com.sega.application.oa.utils;

import com.sega.application.oa.entity.daily.dto.TjTaskDto;
import com.sega.application.oa.entity.project.TaskTimeEntity;
import com.sega.application.oa.entity.project.dto.TaskTimeDto;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * @description:
 * @author: EdgarLi
 * @date: 2021-09-10 09:21
 **/
public class JxlUtil {
  public static ByteArrayInputStream getFileStrteam(List<String> head1, List<String> head2, List<TjTaskDto> xmList, List<TaskTimeDto> taskTimeLis) throws Exception {
    // 声明一个工作薄
    HSSFWorkbook wb = new HSSFWorkbook();

    createTjhzSheet(wb, head1, xmList);
    createGstjSheet(wb, head2, taskTimeLis);

    ByteArrayOutputStream os = new ByteArrayOutputStream(1000);
    wb.write(os);
    wb.close();

    ByteArrayInputStream iss = new ByteArrayInputStream(os.toByteArray());
    os.close();
    return iss;
  }

  private static void createTjhzSheet(HSSFWorkbook wb, List<String> head, List<TjTaskDto> xmList) {
    HSSFSheet sheet = wb.createSheet("工时统计汇总");
    HSSFCellStyle basic = basic(wb);
    HSSFCellStyle basicHeader = basic(wb);
    HSSFRow rowHeader = sheet.createRow(0);
    for (int i = 0; i < head.size(); i++) {
      rowHeader.createCell(i).setCellValue(head.get(i));
    }
    for (Cell cell : rowHeader) {
      HSSFFont f = basic.getFont(wb);
      f.setBold(true);
      basicHeader.setFont(f);
      cell.setCellStyle(basicHeader);
    }
    int rowNum = 1;
    for (TjTaskDto tjTaskDto : xmList) {
      HSSFRow row = sheet.createRow(rowNum);
      row.createCell(0).setCellValue(tjTaskDto.getProjectName());
      row.createCell(1).setCellValue(tjTaskDto.getDemandTime());
      row.createCell(2).setCellValue(tjTaskDto.getDevelTime());
      row.createCell(3).setCellValue(tjTaskDto.getTestTime());
      rowNum++;
      for (Cell cell : row) {
        HSSFFont f = basic.getFont(wb);
        f.setBold(false);
        basic.setFont(f);
        cell.setCellStyle(basic);
      }
    }

  }

  private static void createGstjSheet(HSSFWorkbook wb, List<String> head, List<TaskTimeDto> xmList) {
    HSSFSheet sheet = wb.createSheet("员工工时统计");
    HSSFCellStyle basic = basic(wb);
    HSSFCellStyle basicHeader = basic(wb);
    HSSFRow rowHeader = sheet.createRow(0);
    for (int i = 0; i < head.size(); i++) {
      rowHeader.createCell(i).setCellValue(head.get(i));
    }
    for (Cell cell : rowHeader) {
      HSSFFont f = basic.getFont(wb);
      f.setBold(true);
      basicHeader.setFont(f);
      cell.setCellStyle(basicHeader);
    }
    int rowNum = 1;
    for (TaskTimeDto taskTime : xmList) {
      HSSFRow row = sheet.createRow(rowNum);
      row.createCell(0).setCellValue(taskTime.getTxDate());
      row.createCell(1).setCellValue(taskTime.getTtProjectName());
      row.createCell(2).setCellValue(taskTime.getTtUserName());
      row.createCell(3).setCellValue(taskTime.getTtSpend());
      row.createCell(4).setCellValue(taskTime.getSpendDay());
      row.createCell(5).setCellValue(taskTime.getTtTypeDetail());
      row.createCell(6).setCellValue(getTaskType(taskTime.getTtTaskType()));
      rowNum++;
      for (Cell cell : row) {
        HSSFFont f = basic.getFont(wb);
        f.setBold(false);
        basic.setFont(f);
        cell.setCellStyle(basic);
      }
    }
  }

  private static HSSFCellStyle basic(HSSFWorkbook wb) {
    HSSFCellStyle style = wb.createCellStyle();
    style.setBorderBottom(BorderStyle.THIN);//下边框
    style.setBorderLeft(BorderStyle.THIN);//左边框
    style.setBorderRight(BorderStyle.THIN);//右边框
    style.setBorderTop(BorderStyle.THIN);//上边框
    HSSFFont f = wb.createFont();
    f.setFontName("宋体");
    f.setFontHeightInPoints((short) 12);//字号
    style.setFont(f);
    style.setVerticalAlignment(VerticalAlignment.CENTER);//上下居中
    style.setAlignment(HorizontalAlignment.CENTER_SELECTION);//左右居中
//设置自动换行
//        style.setWrapText(true);
    return style;
  }


  private static String getTaskType(String ttTaskType) {
    switch (ttTaskType) {
      case "1":
        return "需求";
      case "2":
        return "开发";
      case "3":
        return "测试";
      case "4":
        return "实施";
      default:
        return "其他";
    }
  }
}
