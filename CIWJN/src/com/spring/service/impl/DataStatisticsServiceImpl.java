package com.spring.service.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.spring.dao.DataStatisticsMapper;
import com.spring.dto.WeldDto;
import com.spring.model.DataStatistics;
import com.spring.page.Page;
import com.spring.service.DataStatisticsService;

@Service
@Transactional
public class DataStatisticsServiceImpl implements DataStatisticsService {
	@Autowired
	private DataStatisticsMapper ds;
	
	@Override
	public List<DataStatistics> getItemMachineCount(Page page,BigInteger parent) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getItemMachineCount(parent);
	}
	
	@Override
	public List<DataStatistics> getItemMachineCount(BigInteger parent) {
		return ds.getItemMachineCount(parent);
	}

	@Override
	public int getStartingUpMachineNum(BigInteger itemid, WeldDto dto) {
		return ds.getStartingUpMachineNum(itemid, dto);
	}

	@Override
	public DataStatistics getParameter() {
		return ds.getParameter();
	}

	@Override
	public DataStatistics getWorkMachineNum(BigInteger itemid, WeldDto dto) {
		return ds.getWorkMachineNum(itemid, dto);
	}

	@Override
	public DataStatistics getWorkJunctionNum(BigInteger itemid, WeldDto dto) {
		return ds.getWorkJunctionNum(itemid, dto);
	}

	@Override
	public BigInteger getStaringUpTime(BigInteger itemid, WeldDto dto) {
		return ds.getStaringUpTime(itemid, dto); 
	}

	@Override
	public BigInteger getStandytime(BigInteger itemid, WeldDto dto) {
		return ds.getStandytime(itemid, dto);
	}

	@Override
	public DataStatistics getWorkTimeAndEleVol(BigInteger itemid, WeldDto dto) {
		return ds.getWorkTimeAndEleVol(itemid, dto);
	}

	@Override
	public List<DataStatistics> getAllMachine(Page page,BigInteger itemid) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getAllMachine(itemid);
	}

	@Override
	public List<DataStatistics> getAllWelder(Page page,BigInteger parent) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getAllWelder(parent);
	}

	@Override
	public List<DataStatistics> getAllJunction(Page page,String junctionno) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getAllJunction(junctionno);
	}

	@Override
	public List<DataStatistics> getAllInsframe(BigInteger parent) {
		return ds.getAllInsframe(parent);
	}

	@Override
	public List<DataStatistics> getWeldItemInCount(Page page, WeldDto dto) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getWeldItemInCount(dto);
	}

	@Override
	public List<DataStatistics> getWeldItemOutCount(Page page, WeldDto dto) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getWeldItemOutCount(dto);
	}

	@Override
	public List<DataStatistics> getWeldMachineInCount(Page page, WeldDto dto ,BigInteger itemid) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getWeldMachineInCount(dto,itemid);
	}

	@Override
	public List<DataStatistics> getWeldMachineOutCount(Page page, WeldDto dto ,BigInteger itemid) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getWeldMachineOutCount(dto,itemid);
	}

	@Override
	public List<DataStatistics> getWeldPersonInCount(Page page, WeldDto dto) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getWeldPersonInCount(dto);
	}

	@Override
	public List<DataStatistics> getWeldPersonOutCount(Page page, WeldDto dto) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getWeldPersonOutCount(dto);
	}

	@Override
	public List<DataStatistics> getWeldPieceInCount(Page page, WeldDto dto,String junctionno) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getWeldPieceInCount(dto,junctionno);
	}

	@Override
	public List<DataStatistics> getWeldPieceOutCount(Page page, WeldDto dto,String junctionno) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getWeldPieceOutCount(dto,junctionno);
	}

	@Override
	public List<DataStatistics> getFauit(Page page, WeldDto dto, int value) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getFauit(dto, value);
	}

	@Override
	public List<DataStatistics> getFauitDetail(Page page, WeldDto dto, BigInteger id, int value) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getFauitDetail(dto, id, value);
	}

	@Override
	public List<DataStatistics> getAllItemData() {
		return ds.getItemMachineCount(null);
	}

	@Override
	public List<DataStatistics> getAllMachineData(BigInteger itemid) {
		return ds.getAllMachine(itemid);
	}

	@Override
	public List<DataStatistics> getAllPersonData(BigInteger parent) {
		return ds.getAllWelder(parent);
	}

	public List<DataStatistics> getAllJunctionData(String junctionno) {
		return ds.getAllJunction(junctionno);
	}

	@Override
	public List<DataStatistics> getWeldItemInCountData(WeldDto dto) {
		return ds.getWeldItemInCount(dto);
	}

	@Override
	public List<DataStatistics> getWeldItemOutCountData(WeldDto dto) {
		return ds.getWeldItemOutCount(dto);
	}

	@Override
	public List<DataStatistics> getWeldMachineInCountData(WeldDto dto, BigInteger itemid) {
		return ds.getWeldMachineInCount(dto, itemid);
	}

	@Override
	public List<DataStatistics> getWeldMachineOutCountData(WeldDto dto, BigInteger itemid) {
		return ds.getWeldMachineOutCount(dto, itemid);
	}

	@Override
	public List<DataStatistics> getWeldPersonInCountData(WeldDto dto) {
		return ds.getWeldPersonInCount(dto);
	}

	@Override
	public List<DataStatistics> getWeldPersonOutCountData(WeldDto dto) {
		return ds.getWeldPersonOutCount(dto);
	}

	@Override
	public List<DataStatistics> getWeldWorkpieceInCountData(WeldDto dto, String junctionno) {
		return ds.getWeldPieceInCount(dto, junctionno);
	}

	@Override
	public List<DataStatistics> getWeldWorkpieceOutCountData(WeldDto dto, String junctionno) {
		return ds.getWeldPieceOutCount(dto, junctionno);
	}

	@Override
	public List<DataStatistics> getWorkRank(BigInteger parent, String time) {
		return ds.getWorkRank(parent, time);
	}

	@Override
	public DataStatistics getWorkMachineCount(BigInteger itemid, String time) {
		return ds.getWorkMachineCount(itemid, time);
	}

	@Override
	public List<DataStatistics> getItemWeldTime(WeldDto dto) {
		return ds.getItemWeldTime(dto);
	}

	@Override
	public List<DataStatistics> getItemOverProofTime(WeldDto dto) {
		return ds.getItemOverProofTime(dto);
	}

	@Override
	public BigInteger getStaringUpTimeByJunction(BigInteger itemid, WeldDto dto) {
		return ds.getStaringUpTimeByJunction(itemid, dto); 
	}

	@Override
	public BigInteger getStaringUpTimeByWelder(BigInteger itemid, WeldDto dto) {
		return ds.getStaringUpTimeByWelder(itemid, dto); 
	}

	@Override
	public DataStatistics getWorkJunctionNumByWelder(BigInteger itemid, WeldDto dto) {
		return ds.getWorkJunctionNumByWelder(itemid, dto);
	}

	@Override
	public DataStatistics getWorkTimeAndEleVolByWelder(BigInteger itemid, WeldDto dto) {
		return ds.getWorkTimeAndEleVolByWelder(itemid, dto);
	}

	@Override
	public DataStatistics getWorkTimeAndEleVolByJunction(BigInteger itemid, WeldDto dto) {
		return ds.getWorkTimeAndEleVolByJunction(itemid, dto);
	}

	@Override
	public BigInteger getStandytimeByWelder(BigInteger itemid, WeldDto dto) {
		return ds.getStandytimeByWelder(itemid, dto);
	}

	@Override
	public BigInteger getStandytimeByJunction(BigInteger itemid, WeldDto dto) {
		return ds.getStandytimeByJunction(itemid, dto);
	}

	@Override
	public List<DataStatistics> getTaskDetail(BigInteger parent, String welderno, String taskno, String time1,
			String time2) {
		return ds.getTaskDetail(parent, welderno, taskno, time1, time2);
	}

	@Override
	public List<DataStatistics> getTask(Page page, BigInteger parent, String welderno, String taskno, String time1,
			String time2) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getTask(parent, welderno, taskno, time1, time2);
	}

	@Override
	public List<DataStatistics> getTask(BigInteger parent, String welderno, String taskno, String time1, String time2) {
		return ds.getTask(parent, welderno, taskno, time1, time2);
	}

	@Override
	public List<DataStatistics> getFauit(WeldDto dto, int value) {
		return ds.getFauit(dto, value);
	}

	@Override
	public List<DataStatistics> getMachineTask(Page page, BigInteger parent, String sql, int type) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getMachineTask(parent, sql, type);
	}
	
	@Override
	public List<DataStatistics> getMachineTask(BigInteger parent, String sql, int type) {
		return ds.getMachineTask(parent, sql, type);
	}

	@Override
	public String getDay(String time1,String time2){
		String str = "SELECT * FROM (";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = sdf.parse(time1);
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(d);
			int day=cal.get(Calendar.DATE);
			long t1 = sdf.parse(time1).getTime();
			long t2 = sdf.parse(time2).getTime();
			int days = day + (int)((t2-t1)/(1000*60*60*24))+1;
			for(int i = day; i < days; i++){
				Calendar calendar = Calendar.getInstance();  
				Date dates = sdf.parse(time1);
				calendar.setTime(dates);
				calendar.set(Calendar.DATE, i);
				if(i!=days-1){
					str += "SELECT '"+sdf.format(calendar.getTime())+"' AS weldTime UNION ALL ";
				}else{
					str += "SELECT '"+sdf.format(calendar.getTime())+"' AS weldTime)temp";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return str;
	}

	@Override
	public List<DataStatistics> getMachineNoTask(BigInteger insid, String time1, String totime, String time2) {
		// TODO Auto-generated method stub
		return ds.getMachineNoTask(insid,time1,totime,time2);
	}

	@Override
	public List<DataStatistics> getItemData(Page page, BigInteger insid, WeldDto dto) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getItemData(insid, dto);
	}

	@Override
	public List<DataStatistics> getMachineData(Page page, BigInteger insid, WeldDto dto) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getMachineData(insid, dto);
	}

	@Override
	public List<DataStatistics> getWelderData(Page page, BigInteger insid, WeldDto dto) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getWelderData(insid, dto);
	}

	@Override
	public List<DataStatistics> getJunctionData(Page page, BigInteger insid, WeldDto dto) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return ds.getJunctionData(insid, dto);
	}
}
