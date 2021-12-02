package com.sega.application.oa.service.humanengineering.impl;

import com.sega.application.oa.utils.Excel;
import com.sega.application.oa.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sega.application.oa.dao.humanEngineering.IAttendanceRecordDao;
import com.sega.application.oa.entity.humanEngineering.AttendanceRecordEntity;
import com.sega.application.oa.service.humanengineering.IAttendanceRecordService;
import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.service.system.impl.BaseServiceImpl;

/**
 * 管理持久化层
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2020-1-8 15:57:37<br/>
 * 历史修订：<br/>
 */
 @Service
public class AttendanceRecordServiceImpl extends BaseServiceImpl implements IAttendanceRecordService {

	@Autowired
	private IAttendanceRecordDao attendanceRecordDao;

    @Override
	protected IBaseDao getDao() {
		return attendanceRecordDao;
	}

	@Override
	public void insert(AttendanceRecordEntity ar){
    	 attendanceRecordDao.insert(ar);
	}

	@Override
	public void importExcel(String[] path){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String a = "";
		Integer index = -1;//标记当月下一行为打卡数据;
		Integer index1 = -1;//标记上月月下一行为打卡数据;
		List<String> time = new ArrayList<>();
		List<List> date = new ArrayList<>();
		List<AttendanceRecordEntity> dateNew = new ArrayList<>();//所有数据
		List<List<String>> dateOld = new ArrayList<>();//存储上月最后六天数据
		List<List> dateOldMonth = new ArrayList<>();//存储上月最后六天数据倒序
		String name = "";//记录当月当前人员名字
		String name1 = "";//记录上月月当前人员名字
		int month = 11;
		int year = 2019;
		int mergeIndex = 1;//合并index
		List<List<Integer>> merge = new ArrayList<>();//所有合并对应行列
		List<List<String>> excepData1 = Excel.commonImport(path[0],32,6);
		List<List<String>> excepData = Excel.commonImport(path[1],32,6);
		//加载上月最后六天数据
		for(int q = 0;q < excepData1.size() ; q++){
			List list = new ArrayList();
			for(int i = 0 ; i < excepData1.get(q).size() ;i++){
				if(excepData1.get(q).get(i).equals("姓 名:")){
					name1 = excepData1.get(q).get(i+2);
					index1 = q + 1;
				}
				if(name1 != "" && list.size() == 0){
					list.add(name1);
				}
				if(index1 == q && i >= excepData1.get(q).size() - 6){
					list.add(excepData1.get(q).get(i));
				}
			}
			if(list.size() > 5){
				List list1 = new ArrayList();
				for(int w = list.size()-1;w>=0;w--){
					list1.add(list.get(w));
				}
				dateOldMonth.add(list1);
			}

		}
		//加载表格数据
		for(int j = 0;j<excepData.size() ; j++){
			Double allCb = 0.0;//小计餐补
			Double allTx = 0.0;//调休日
			Double allJb = 0.0;//核算加班工资日
			List<Integer> mergePart = new ArrayList<>();//单人合并行列数


			for(int i = 0 ; i < excepData.get(j).size() ;i++){
				AttendanceRecordEntity ar = new AttendanceRecordEntity();
				int pp = excepData.get(j).size();//每个人的行数;
				int h=(i+1+(26*(month+1)/10)+year%100+(year%100/4)+year/100/4+5*year/100)%7;//克里斯汀-泽勒计算某天是星期几的算法公式
				ar.setArWeek(h+"");
//				Map<String,Object> dateRow = new HashMap<>();
//				dateRow.put("date","2019-11-"+i+1);
				try{
					ar.setArDate(simpleDateFormat1.parse("2019-11-"+(i+1)));
				}catch (Exception e){
					System.out.println("时间错了");
				}
				ar.setArDel(0);
//				dateRow.put("arUsername",name);
				ar.setArUsername(name);
				if(excepData.get(j).get(i).equals("姓 名:")){
					name = excepData.get(j).get(i+2);
					index = j+1;
					mergePart.add(mergeIndex);
					mergePart.add(pp - 1+mergeIndex);
					mergePart.add(1);
					mergePart.add(1);
					mergePart.add(mergeIndex);
					mergePart.add(pp - 1+mergeIndex);
					mergePart.add(7);
					mergePart.add(7);
					mergePart.add(mergeIndex);
					mergePart.add(pp - 1+mergeIndex);
					mergePart.add(8);
					mergePart.add(8);
					mergePart.add(mergeIndex);
					mergePart.add(pp - 1+mergeIndex);
					mergePart.add(9);
					mergePart.add(9);
					merge.add(mergePart);
					mergeIndex += pp ;
				}
				if(j == index){
					if(excepData.get(j).get(i).length() != 0){
						String times = excepData.get(j).get(i);
//						dateRow.put("arBasicdata",times);
						ar.setArBasicdata(times);
						String str1 = times.substring(0,5);
						String str2 = times.substring(times.length()-5,times.length());

						try {

							System.out.println(str1);
							System.out.println(str2);
							Date d1 = simpleDateFormat.parse(str1);
							Date d2 = simpleDateFormat.parse(str2);
							Date d3 = simpleDateFormat.parse("09:00");
							Date d4 = simpleDateFormat.parse("09:30");
							System.out.println((d3.getTime()-d4.getTime())/3600000.0);

							if(excepData.get(j).get(i).length()<10){
//								dateRow.put("arSymbol",1);
//								dateRow.put("arDebuff","打卡次数不足");
								ar.setArSymbol(1);
								ar.setArDebuff("打卡次数不足");

							}else if(h>1 && h<7){
//								dateRow.put("arWeek",h);
//								dateRow.put("arTime",(d2.getTime()-d1.getTime())/3600000.0);
//								ar.setArWeek(h+"");
								ar.setArTime((d2.getTime()-d1.getTime())/3600000.0);
								if((d2.getTime()-d1.getTime())/3600000.0 < 12 && (d2.getTime()-d1.getTime())/3600000.0 >= 9){
//									dateRow.put("arSymbol",0);
//									dateRow.put("arOverstate",3);
									ar.setArSymbol(0);
									ar.setArOverstate(3);
									if((d1.getTime() - simpleDateFormat.parse("09:00").getTime())/3600000.0 <= 0){
										//工作日不加班未迟到无异常
//										dateRow.put("arMoney",0);
//										ar.setArMoney(0);
									}
									else if((d1.getTime() - simpleDateFormat.parse("09:00").getTime())/3600000.0 > 0 && (d1.getTime() - simpleDateFormat.parse("09:00").getTime())/3600000.0 <= 0.17){
										//工作日不加班迟到十分钟内无异常
//										dateRow.put("arMoney",-10);
										ar.setArMoney(-10);
									}
									else if((d1.getTime() - simpleDateFormat.parse("09:00").getTime())/3600000.0 > 0.1666 && (d1.getTime() - simpleDateFormat.parse("09:00").getTime())/3600000.0 <= 0.5){
										//工作日不加班迟到三十分钟内无异常
//										dateRow.put("arMoney",-30);
										ar.setArMoney(-30);
									}
									else if((d1.getTime() - simpleDateFormat.parse("09:00").getTime())/3600000.0 > 0.5){
										//工作日不加班迟到超三十分钟无异常
//										dateRow.put("arMoney",-50);
										ar.setArMoney(-50);
									}
								}else if((d2.getTime()-d1.getTime())/3600000 >= 12){
//									dateRow.put("arSymbol",0);
//									dateRow.put("arOverstate",0);
									ar.setArSymbol(0);
									ar.setArOverstate(0);
									if((d1.getTime() - simpleDateFormat.parse("09:00").getTime())/3600000.0 <= 0){
										//工作日加班未迟到无异常
//										dateRow.put("arMoney",20);
										ar.setArMoney(20);
									}
									if((d1.getTime() - simpleDateFormat.parse("09:00").getTime())/3600000.0 > 0 && (d1.getTime() - simpleDateFormat.parse("09:00").getTime())/3600000.0 <= 0.17){
										//工作日加班迟到十分钟内无异常
//										dateRow.put("arMoney",10);
										ar.setArMoney(10);
									}
									if((d1.getTime() - simpleDateFormat.parse("09:00").getTime())/3600000.0 > 0.1666 && (d1.getTime() - simpleDateFormat.parse("09:00").getTime())/3600000.0 <= 0.5){
										//工作日加班迟到三十分钟内无异常
//										dateRow.put("arMoney",-10);
										ar.setArMoney(-10);
									}
									if((d1.getTime() - simpleDateFormat.parse("09:00").getTime())/3600000.0 > 0.5){
										//工作日不加班迟到超三十分钟无异常
//										dateRow.put("arMoney",-30);
										ar.setArMoney(-30);
									}
									allCb += 20;
								}else{
//									dateRow.put("arSymbol",1);
//									dateRow.put("arDebuff","上班时间不足9小时");
									ar.setArSymbol(1);
									ar.setArDebuff("上班时间不足9小时");
								}
							}else {
//								dateRow.put("arWeek",h);
//								dateRow.put("arTime",(d2.getTime()-d1.getTime())/3600000.0);
//								ar.setArWeek(h+"");
								ar.setArTime((d2.getTime()-d1.getTime())/3600000.0);
								if((d2.getTime()-d1.getTime())/3600000 >= 4 && (d2.getTime()-d1.getTime())/3600000 < 9){
									long oo = 0;
//									dateRow.put("arSymbol",0);
//									dateRow.put("arOverstate",1);
									ar.setArSymbol(0);
									ar.setArOverstate(1);
									ar.setArMoney(20);
									ar.setArGetLieupay(0.5);
									allCb += 20;
									if(h == 0){
										if(i-5>=0) {
											for (int l = i - 5; l < i; l++) {
												if (excepData.get(j).get(l).length() != 0) {
													String times0 = excepData.get(j).get(l);
													String str0 = times0.substring(0, 5);
													String str00 = times0.substring(times0.length() - 5, times0.length());
													Date d0 = simpleDateFormat.parse(str0);
													Date d00 = simpleDateFormat.parse(str00);
													oo += d00.getTime() - d0.getTime();
												}
											}
											if (oo / 3600000 >= 57) {
//												dateRow.put("arLieupayState",2);
												ar.setArLieupayState(2);
												allJb += 0.5;

											}else if(oo / 3600000.0 <= 57 && oo / 3600000.0 >= 45){
//												dateRow.put("arLieupayState",1);
												ar.setArLieupayState(1);
												allTx += 0.5;
											}
										}else{
											for(List list:dateOldMonth){
												if(list.get(6).equals(name)){
													for(int e = 4-i;e>=0;e --){
														if(list.get(e).toString().length()!=0&& list.get(e) != null){
															String times0 = list.get(e).toString();
															String str0 = times0.substring(0, 5);
															String str00 = times0.substring(times0.length() - 5, times0.length());
															Date d0 = simpleDateFormat.parse(str0);
															Date d00 = simpleDateFormat.parse(str00);
															oo += d00.getTime() - d0.getTime();
														}
													}
												}
											}
											for(int e = 0;e<i;e++){
												String times0 = excepData.get(j).get(e);
												String str0 = times0.substring(0, 5);
												String str00 = times0.substring(times0.length() - 5, times0.length());
												Date d0 = simpleDateFormat.parse(str0);
												Date d00 = simpleDateFormat.parse(str00);
												oo += d00.getTime() - d0.getTime();
											}
											if (oo / 3600000 >= 57) {
//												dateRow.put("arLieupayState",2);
												ar.setArLieupayState(2);
												allJb += 0.5;

											}else if(oo / 3600000.0 <= 57 && oo / 3600000.0 >= 45){
//												dateRow.put("arLieupayState",1);
												ar.setArLieupayState(1);
												allTx += 0.5;
											}
										}
									}else{
										if(i-6>=0) {
											for (int l = i - 6; l < i - 1; l++) {
												if (excepData.get(j).get(l).length() != 0) {
													String times0 = excepData.get(j).get(l);
													String str0 = times0.substring(0, 5);
													String str00 = times0.substring(times0.length() - 5, times0.length());
													Date d0 = simpleDateFormat.parse(str0);
													Date d00 = simpleDateFormat.parse(str00);
													oo += d00.getTime() - d0.getTime();
												}
											}
											if (oo / 3600000 >= 57) {
//												dateRow.put("arLieupayState",2);
												ar.setArLieupayState(2);
												allJb += 0.5;
											}else if(oo / 3600000.0 <= 57 && oo / 3600000.0 >= 45){
//												dateRow.put("arLieupayState",1);
												ar.setArLieupayState(1);
												allTx +=0.5;
											}
										} else{
											for(List list:dateOldMonth){
												if(list.get(6).equals(name)){
													for(int e = 5-i;e>=0;e --){
														if(list.get(e).toString().length()!=0&& list.get(e) != null){
															String times0 = list.get(e).toString();
															String str0 = times0.substring(0, 5);
															String str00 = times0.substring(times0.length() - 5, times0.length());
															Date d0 = simpleDateFormat.parse(str0);
															Date d00 = simpleDateFormat.parse(str00);
															oo += d00.getTime() - d0.getTime();
														}
													}
												}
											}
											for(int e = 0;e<i-1;e++){
												if(excepData.get(j).get(e).length() != 0){
													String times0 = excepData.get(j).get(e);
													String str0 = times0.substring(0, 5);
													String str00 = times0.substring(times0.length() - 5, times0.length());
													Date d0 = simpleDateFormat.parse(str0);
													Date d00 = simpleDateFormat.parse(str00);
													oo += d00.getTime() - d0.getTime();
												}
											}
											if (oo / 3600000 >= 57) {
//												dateRow.put("arLieupayState",2);
												ar.setArLieupayState(2);
												allJb += 0.5;

											}else if(oo / 3600000.0 <= 57 && oo / 3600000.0 >= 45){
//												dateRow.put("arLieupayState",1);
												ar.setArLieupayState(1);
												allTx += 0.5;
											}
										}
									}
								}else if((d2.getTime()-d1.getTime())/3600000 >= 9){
									long oo = 0;
//									dateRow.put("arSymbol",0);
//									dateRow.put("arOverstate",2);
									ar.setArSymbol(0);
									ar.setArOverstate(2);
									ar.setArMoney(40);
									ar.setArGetLieupay(1.0);
									allCb += 40;
									if(h == 0){
										if(i-5>=0) {
											for (int l = i - 5; l < i; l++) {
												if (excepData.get(j).get(l).length() != 0) {
													String times0 = excepData.get(j).get(l);
													String str0 = times0.substring(0, 5);
													String str00 = times0.substring(times0.length() - 5, times0.length());
													Date d0 = simpleDateFormat.parse(str0);
													Date d00 = simpleDateFormat.parse(str00);
													oo += d00.getTime() - d0.getTime();
												}
											}
											if (oo / 3600000 >= 57) {
//												dateRow.put("arLieupayState",2);
												ar.setArLieupayState(2);
												allJb += 1;
											}else if(oo / 3600000.0 <= 57 && oo / 3600000.0 >= 45){
//												dateRow.put("arLieupayState",1);
												ar.setArLieupayState(1);
												allTx += 1;
											}
										}
										else{
											for(List<String> list:dateOldMonth){
												if(list.get(6).equals(name)){
													for(int e = 4-i;e>=0;e --){
														if(list.get(e).length()!=0 && list.get(e) != null){
															String times0 = list.get(e).toString();
															String str0 = times0.substring(0, 5);
															String str00 = times0.substring(times0.length() - 5, times0.length());
															Date d0 = simpleDateFormat.parse(str0);
															Date d00 = simpleDateFormat.parse(str00);
															oo += d00.getTime() - d0.getTime();
														}
													}
												}
											}
											for(int e = 0;e<i;e++){
												String times0 = excepData.get(j).get(e);
												String str0 = times0.substring(0, 5);
												String str00 = times0.substring(times0.length() - 5, times0.length());
												Date d0 = simpleDateFormat.parse(str0);
												Date d00 = simpleDateFormat.parse(str00);
												oo += d00.getTime() - d0.getTime();
											}
											if (oo / 3600000 >= 57) {
//												dateRow.put("arLieupayState",2);
												ar.setArLieupayState(2);
												allJb += 1;

											}else if(oo / 3600000.0 <= 57 && oo / 3600000.0 >= 45){
//												dateRow.put("arLieupayState",1);
												ar.setArLieupayState(1);
												allTx += 1;
											}
										}
									}else{
										if(i-6>=0) {
											for (int l = i - 6; l < i - 1; l++) {
												if (excepData.get(j).get(l).length() != 0) {
													String times0 = excepData.get(j).get(l);
													String str0 = times0.substring(0, 5);
													String str00 = times0.substring(times0.length() - 5, times0.length());
													Date d0 = simpleDateFormat.parse(str0);
													Date d00 = simpleDateFormat.parse(str00);
													oo += d00.getTime() - d0.getTime();
												}
											}
											if (oo / 3600000 >= 57) {
//												dateRow.put("arLieupayState",2);
												ar.setArLieupayState(2);
												allJb += 1;
											}else if(oo / 3600000.0 <= 57 && oo / 3600000.0 >= 45){
//												dateRow.put("arLieupayState",1);
												ar.setArLieupayState(1);
												allTx += 1;
											}
										}
										else{
											for(List list:dateOldMonth){
												if(list.get(6).equals(name)){
													for(int e = 5-i;e>=0;e --){
														if(list.get(e).toString().length()!=0 && list.get(e) != null){
															String times0 = list.get(e).toString();
															String str0 = times0.substring(0, 5);
															String str00 = times0.substring(times0.length() - 5, times0.length());
															Date d0 = simpleDateFormat.parse(str0);
															Date d00 = simpleDateFormat.parse(str00);
															oo += d00.getTime() - d0.getTime();
														}
													}
												}
											}
											for(int e = 0;e<i-1;e++){
												if(excepData.get(j).get(e).length() != 0){
													String times0 = excepData.get(j).get(e);
													String str0 = times0.substring(0, 5);
													String str00 = times0.substring(times0.length() - 5, times0.length());
													Date d0 = simpleDateFormat.parse(str0);
													Date d00 = simpleDateFormat.parse(str00);
													oo += d00.getTime() - d0.getTime();
												}
											}
											if (oo / 3600000.0 >= 57) {
//												dateRow.put("arLieupayState",2);
												ar.setArLieupayState(2);
												allJb += 1;

											}else if(oo / 3600000.0 <= 57 && oo / 3600000.0 >= 45){
//												dateRow.put("arLieupayState",1);
												ar.setArLieupayState(1);
												allTx += 1;
											}
										}
									}
								}else{
//									dateRow.put("arSymbol",1);
//									dateRow.put("arDebuff","周末上班时间少于4小时");
									ar.setArSymbol(1);
									ar.setArDebuff("周末上班时间少于4小时");
								}
							}
							dateNew.add(ar);

//							if(i == excepData.get(j).size()-1){
//								for(int o = mergeIndex - pp - 1;o < mergeIndex - 1;o++){
//									List dateRowOld = date.get(o);
//									dateRowOld.add(allCb+"");
//									dateRowOld.add(allTx+"");
//									dateRowOld.add(allJb+"");
//									dateNew.add(dateRowOld);
//								}
//							}

						} catch (ParseException e) {
							e.printStackTrace();
//							dateRow.put("arSymbol",1);
//							dateRow.put("arDebuff","打卡次数不足");
							ar.setArSymbol(1);
							ar.setArDebuff("打卡次数不足");
							dateNew.add(ar);
//							if(i == excepData.get(j).size()-1){
//								for(int o = mergeIndex - pp - 1;o < mergeIndex - 1;o++){
//									List dateRowOld = date.get(o);
//									dateRowOld.add(allCb+"");
//									dateRowOld.add(allTx+"");
//									dateRowOld.add(allJb+"");
//									dateNew.add(dateRowOld);
//								}
//							}
							System.out.println("报错报错报错");
						}
						a = str1 + "   " + str2;
					}else{
						if(h!=0 && h!=1){
//							dateRow.put("arSymbol",1);
//							dateRow.put("arDebuff","未上班");
							ar.setArSymbol(1);
							ar.setArDebuff("未上班");
						}else{
//							dateRow.put("arSymbol",0);
//							dateRow.put("arOverstate",3);
							ar.setArSymbol(0);
							ar.setArOverstate(3);
						}
						dateNew.add(ar);
//						if(i == excepData.get(j).size()-1){
//							for(int o = mergeIndex - pp - 1 ;o < mergeIndex - 1;o++){
//								List dateRowOld = date.get(o);
//								dateRowOld.add(allCb+"");
//								dateRowOld.add(allTx+"");
//								dateRowOld.add(allJb+"");
//								dateNew.add(dateRowOld);
//							}
//						}

					}
				}


//				System.out.print("--" + excepData.get(j).get(i));
			}
//			System.out.println();
		}
		for(AttendanceRecordEntity ar : dateNew){
			try{
				this.insert(ar);
			}catch (Exception e){
				System.out.println("咋回事啊");
			}

		}
		System.out.println(a);
	}
	
}