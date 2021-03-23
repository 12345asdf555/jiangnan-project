package com.spring.service;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.WeldDto;
import com.spring.model.Report;
import com.spring.page.Page;

public interface ReportService {
	BigInteger getWpsid(BigInteger machid);
	Report getWps(BigInteger wpsid);
	Report getSyspara();
	List<Report> findAllWelder(Page page,WeldDto dto);
	List<Report> findMachine(String weldid);
	long getWeldingTime(WeldDto dto,BigInteger machid,String weldid);
	long getOnTime(WeldDto dto,BigInteger machid);
	long getRealEle(WeldDto dto,BigInteger machid);
	long getRealVol(WeldDto dto,BigInteger machid);
	long getHjTime(BigInteger machid,String time);
	long getZxTime(BigInteger machid,String time);
	String getFirstTime(BigInteger machid,String time);
	List<Report> getAllPara(BigInteger parent,String str,String time);
	List<Report> historyData(Page page,WeldDto dto,String fid,BigInteger mach, String welderid);
}
