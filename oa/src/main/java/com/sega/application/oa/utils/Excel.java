package com.sega.application.oa.utils;
/**
 * 和Excel与有关的工具类
 * @author sq
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 读取单位的excel文件
 *
 */
@SuppressWarnings({ "static-access", "deprecation", "resource", "unused"})
public class Excel {

	/**
	 * 通用批量导入
	 */
	public static List<List<String>> commonImport(String filePath,int cols,Integer sheetIndex){
		try {
			if(sheetIndex == null){
				sheetIndex = 0;
			}
			if(!filePath.matches("^.+\\.(?i)(xls|xlsx)$")){ //判断是否excel文档
				return null;			
			}
			List<List<String>> dataList = new ArrayList<List<String>>();	
			Row row;
			Cell cell = null;
			FileInputStream fileInputStream = new FileInputStream(filePath);
			boolean is03Excel = filePath.matches("^.+\\.(?i)(xlsx)$") ? false : true;
			Workbook workbook = is03Excel ? new HSSFWorkbook(fileInputStream) : new XSSFWorkbook(fileInputStream);
			Sheet sheet = workbook.getSheetAt(sheetIndex);
			Iterator rows = sheet.rowIterator();
			int curRow = 0; //当前行
			while (rows.hasNext()){	
				row = (Row) rows.next();
				Iterator cells = row.cellIterator();
	            int curCol = 0; //当前列	  
	            List<String> curList = new ArrayList<String>();
	            while (curCol < cols){	
	            	cell = row.getCell(curCol);            	            	
	            	//判断当前的cell是否有值
	            	if(cell == null ){
	            		curList.add("");
	            		curCol++;
	            		continue;
	            	}            	
	            	if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)){
	            		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	            	}else{
	            		cell.setCellType(Cell.CELL_TYPE_STRING);
	            	}            	

	        		if(curRow ==  0){
	        			break;
	        		}
	        		if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)){
	            		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	            		SimpleDateFormat lastSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");   
    			        curList.add(new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue()));
	            		
	            	}else{
	            		curList.add(cell.getStringCellValue());
	            	}	        		
	        		curCol++;
	            }
	            if(curRow != 0){
	            	dataList.add(curList);
	            }	 
	            curRow++;	                       
			}
			return dataList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			File file = new File(filePath);
			//file.delete();
		}
		
	}
	
	/**
	 * 通用批量导入
	 * <b>适用于行没有空值</b>
	 * @param filePath
	 * @return
	 */
	public static List<List<String>> commonImport(String filePath){
		try {
			if(!filePath.matches("^.+\\.(?i)(xls|xlsx)$")){ //判断是否excel文档
				return null;			
			}
			List<List<String>> dataList = new ArrayList<List<String>>();	
			Row row;
			Cell cell = null;
			FileInputStream fileInputStream = new FileInputStream(filePath);
			boolean is03Excel = filePath.matches("^.+\\.(?i)(xlsx)$") ? true : false;
			Workbook workbook = is03Excel ? new HSSFWorkbook(fileInputStream) : new XSSFWorkbook(fileInputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator rows = sheet.rowIterator();
			int curRow = 0; //当前行
			while (rows.hasNext()){	
				row = (Row) rows.next();
				Iterator cells = row.cellIterator();
	            int curCol = 0; //当前列	  
	            List<String> curList = new ArrayList<String>();
	            while (true){	
	            	cell = row.getCell(curCol);            	            	
	            	//判断当前的cell是否有值
	            	if(cell == null ){
	            		break;
	            	}            	
	            	if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)){
	            		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	            	}else{
	            		cell.setCellType(Cell.CELL_TYPE_STRING);
	            	}            	

	        		if(curRow ==  0){
	        			break;
	        		}
	        		if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)){
	            		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	            		SimpleDateFormat lastSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");   
    			        curList.add(new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue()));
	            		
	            	}else{
	            		curList.add(cell.getStringCellValue());
	            	}	        		
	        		curCol++;
	            }
	            if(curRow != 0 && StringUtils.isNotBlank(curList.get(0))){
	            	dataList.add(curList);
	            }	 
	            curRow++;	                       
			}
			return dataList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			File file = new File(filePath);
			file.delete();
		}
		
	}
	
	/**
	 * 下载excel
	 * @param path
	 * @param response
	 * @throws IOException 
	 */
	public static void download(String path,String filename, HttpServletResponse response){
		try {
			// path是指欲下载的文件的路径。  
	        File file = new File(path);  
	        // 取得文件名。  
	        // 以流的形式下载文件。  
	        InputStream fis = new BufferedInputStream(new FileInputStream(path));  
	        byte[] buffer = new byte[fis.available()];  
	        fis.read(buffer);  
	        fis.close();  
	        // 清空response  
	        response.reset();  
	        // 设置response的Header          	        
	        response.addHeader("Content-Disposition", "attachment;filename="+new String((filename).getBytes("utf-8"), "ISO8859-1"));  
	        response.addHeader("Content-Length", "" + file.length());  
	        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());  
	        response.setContentType("application/vnd.ms-excel;charset=utf-8");  
	        toClient.write(buffer);  
	        toClient.flush();  
	        toClient.close(); 		
		} catch (Exception e) {
			e.printStackTrace();
		}		
	} 
	/**
	 * 通用导出Excel方法
	 * 注意： title和数据集合list中包含的String数组 的数据需要对应
	 * @param fileName
	 * @param title
	 * @param headers
	 * @param dataset
	 * @param pattern
	 * @return
	 * @throws IOException
	 */
	public static String  commonExport(String fileName,String title, String[] headers,List<List> dataset, String realPath){
		try{
			File file =new File(realPath); 
			//判断文件夹是否存在，不存在就创建问价
			if  (!file .exists()  && !file .isDirectory()){       
			    file.mkdir();    
			} 
			realPath += "\\"+fileName;
			//文件所在路径
			OutputStream out = new FileOutputStream(realPath); 
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			//设置单元格样式
			 HSSFCellStyle style = workbook.createCellStyle();
			 HSSFFont font = workbook.createFont();
			 style.setAlignment(style.ALIGN_CENTER);//水平居中  
			 style.setVerticalAlignment(style.VERTICAL_CENTER);//垂直居中 
			 font.setFontHeightInPoints((short)12);
			 style.setFont(font);
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(title);	
			sheet.setDefaultColumnWidth((short) 15);
			sheet.setDefaultRowHeight((short) (25 * 20));
			HSSFRow row = sheet.createRow(0);
			row.setHeight((short) (25 * 20));
			for (short i = 0; i < headers.length; i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(style);
			}
			// 遍历集合数据，产生数据行
			Iterator<List> it = dataset.iterator();
			int index = 0;
			while (it.hasNext()){  
	            index++;  
	            row = sheet.createRow(index); 
	            row.setHeight((short) (25 * 20));
	            List data = (List) it.next();  
	            for (short i = 0; i < data.size(); i++){
	    			HSSFCell cell = row.createCell(i);
	    			if(data.get(i) != null){
	    				cell.setCellValue(data.get(i).toString());
	    			}else{
	    				cell.setCellValue("");
	    			}	    			
	    			cell.setCellStyle(style);
	    		}  
	        }  
	        workbook.write(out);  
	        out.flush();
	        out.close();
	        return realPath;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
			// TODO: handle exception
		}		
	}
	/**
	 * 加密处理信息
	 * @return
	 */
	private String encryption(){
		return "b2b9f7c8ad30c44c12524a506a3c4317";
	}


}
