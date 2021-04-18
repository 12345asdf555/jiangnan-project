package com.spring.service.impl;

import com.github.pagehelper.PageHelper;
import com.spring.dao.ReportMapper;
import com.spring.dto.WeldDto;
import com.spring.model.Report;
import com.spring.page.Page;
import com.spring.service.ReportService;
import com.spring.util.JNDateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class ReportServiceImpl implements ReportService{

	@Resource
	private ReportMapper mapper;
	
	@Override
	public BigInteger getWpsid(BigInteger machid) {
		// TODO Auto-generated method stub
		return mapper.getWpsid(machid);
	}

	@Override
	public Report getWps(BigInteger wpsid) {
		// TODO Auto-generated method stub
		return mapper.getWps(wpsid);
	}

	@Override
	public Report getSyspara() {
		// TODO Auto-generated method stub
		return mapper.getSyspara();
	}

	@Override
	public List<Report> findAllWelder(Page page,WeldDto dto) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return mapper.findAllWelder(dto);
	}

	public long getWeldingTime(WeldDto dto, BigInteger machid,String weldid) {
		// TODO Auto-generated method stub
		return mapper.getWeldingTime(dto, machid,weldid);
	}

	@Override
	public long getOnTime(WeldDto dto, BigInteger machid) {
		// TODO Auto-generated method stub
		return mapper.getOnTime(dto, machid);
	}

	@Override
	public long getRealEle(WeldDto dto, BigInteger machid) {
		// TODO Auto-generated method stub
		return mapper.getRealEle(dto, machid);
	}

	@Override
	public long getRealVol(WeldDto dto, BigInteger machid) {
		// TODO Auto-generated method stub
		return mapper.getRealVol(dto, machid);
	}

	@Override
	public List<Report> findMachine(String weldid) {
		// TODO Auto-generated method stub
		return mapper.findMachine(weldid);
	}

	@Override
	public long getHjTime(BigInteger machid, String time) {
		// TODO Auto-generated method stub
		return mapper.getHjTime(machid, time);
	}

	@Override
	public long getZxTime(BigInteger machid, String time) {
		// TODO Auto-generated method stub
		return mapper.getZxTime(machid, time);
	}

	@Override
	public String getFirstTime(BigInteger machid, String time) {
		// TODO Auto-generated method stub
		return mapper.getFirstTime(machid, time);
	}

	@Override
	public List<Report> getAllPara(BigInteger parent, String str, String time) {
		// TODO Auto-generated method stub
		return mapper.getAllPara(parent, str, time);
	}

	@Override
	public List<Report> historyData(Page page,WeldDto dto,String fid,BigInteger mach,String welderid) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		List<Report> list = new ArrayList<>();
		List<Report> reportList = null;
		try {
			if (dto != null && dto.getDtoTime1() != null && dto.getDtoTime2() != null){
				List<String> tableList = JNDateUtil.getRtDataTableList(dto.getDtoTime1(), dto.getDtoTime2());
				if (null != tableList && tableList.size() > 0){
					for (String tableName : tableList) {
						if (null != tableName && !tableName.equals("")){
							dto.setRtDataTableName(tableName);
							reportList = mapper.historyData(dto, fid, mach, welderid);
							if (null != reportList && reportList.size() > 0){
								list.addAll(reportList);
							}
						}
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
}
