package com.spring.service;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.WeldDto;
import com.spring.model.DataStatistics;
import com.spring.page.Page;

public interface DataStatisticsService {
	/**
	 * 获取项目部焊机总数
	 * @param page 分页
	 * @param parent 组织机构父id
	 * @return
	 */
	List<DataStatistics> getItemMachineCount(Page page, BigInteger parent);
	List<DataStatistics> getItemMachineCount(BigInteger parent);
	
	/**
	 * 获取开机焊机总数
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	int getStartingUpMachineNum(BigInteger itemid,WeldDto dto);
	
	/**
	 * 获取参数
	 * @return
	 */
	DataStatistics getParameter();
	
	/**
	 * 获取工作的焊机数
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	DataStatistics getWorkMachineNum(BigInteger itemid,WeldDto dto);

	/**
	 * 获取工作的焊口数
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	DataStatistics getWorkJunctionNum(BigInteger itemid,WeldDto dto);

	DataStatistics getWorkJunctionNumByWelder(BigInteger itemid,WeldDto dto);
	
	/**
	 * 获取开机总时长
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	BigInteger getStaringUpTime(BigInteger itemid,WeldDto dto);
	BigInteger getStaringUpTimeByJunction(BigInteger itemid,WeldDto dto);
	BigInteger getStaringUpTimeByWelder(BigInteger itemid,WeldDto dto);
	
	/**
	 * 获取待机总时长
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	BigInteger getStandytime(BigInteger itemid,WeldDto dto);
	
	BigInteger getStandytimeByWelder(BigInteger itemid,WeldDto dto);
	
	BigInteger getStandytimeByJunction(BigInteger itemid,WeldDto dto);
	
	/**
	 * 获取焊接时长，平均电流电压
	 * @param itemid  项目部id
	 * @param dto 扩展参数类
	 * @return
	 */
	DataStatistics getWorkTimeAndEleVol(BigInteger itemid,WeldDto dto);
	
	DataStatistics getWorkTimeAndEleVolByWelder(BigInteger itemid,WeldDto dto);
	
	DataStatistics getWorkTimeAndEleVolByJunction(BigInteger itemid,WeldDto dto);
	
	/**
	 * 获取所有的焊机id，编号以及组织机构id，名称
	 * @param page 分页
	 * @param itemid 组织机构id
	 * @return
	 */
	List<DataStatistics> getAllMachine(Page page,BigInteger itemid);
	
	/**
	 * 获取所有的焊工编号，姓名
	 * @param page
	 * @return
	 */
	List<DataStatistics> getAllWelder(Page page,BigInteger parent);
	
	/**
	 * 获取所有悍缝编号及组织机构id，name
	 * @param page 分页
	 * @param junctionno 焊缝编号
	 * @return
	 */
	List<DataStatistics> getAllJunction(Page page,String junctionno);
	
	/**
	 * 获取所有项目部组织机构
	 * @return
	 */
	List<DataStatistics> getAllInsframe(BigInteger parent);

	/**
	 * 获取组织机构累计焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldItemInCount(Page page, WeldDto dto);
	
	/**
	 * 获取组织机构超规范焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldItemOutCount(Page page, WeldDto dto);
	
	/**
	 * 获取焊机累计焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldMachineInCount(Page page, WeldDto dto ,BigInteger itemid);
	
	/**
	 * 获取焊机超规范焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldMachineOutCount(Page page, WeldDto dto ,BigInteger itemid);
	
	/**
	 * 获取焊工累计焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldPersonInCount(Page page, WeldDto dto);
	
	/**
	 * 获取焊工超规范焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldPersonOutCount(Page page, WeldDto dto);
	
	/**
	 * 获取工件累计焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldPieceInCount(Page page, WeldDto dto,String junctionno);
	
	/**
	 * 获取工件超规范焊接时间
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @return
	 */
	List<DataStatistics> getWeldPieceOutCount(Page page, WeldDto dto,String junctionno);
	
	/**
	 * 获取焊机故障
	 * @param page 分页
	 * @param dto 扩展参数类
	 * @param value 故障类型id
	 * @return
	 */
	List<DataStatistics> getFauit(Page page,WeldDto dto,int value);

	/**
	 * 获取焊机故障
	 * @param dto 扩展参数类
	 * @param value 故障类型id
	 * @return
	 */
	List<DataStatistics> getFauit(WeldDto dto,int value);
	
	/**
	 * 获取焊机故障明细
	 * @param page 分页
	 * @param dto 扩展dto类
	 * @param id 焊机id
	 * @param value 故障类型id
	 * @return
	 */
	List<DataStatistics> getFauitDetail(Page page,WeldDto dto,BigInteger id,int value);

	/**
	 * 班组生产数据导出Excel
	 * @param str
	 * @return
	 */
	List<DataStatistics> getAllItemData();

	/**
	 * 设备生产数据导出Excel
	 * @param itemid
	 * @return
	 */
	List<DataStatistics> getAllMachineData(BigInteger itemid);
	
	/**
	 * 人员生产数据导出Excel
	 * @return
	 */
	List<DataStatistics> getAllPersonData(BigInteger parent);

	/**
	 * 工件生产数据导出Excel
	 * @param itemid
	 * @return
	 */
	List<DataStatistics> getAllJunctionData(String junctionno);

	/**
	 * 班组累计焊接时间
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getWeldItemInCountData(WeldDto dto);

	/**
	 * 班组超规范焊接时间
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getWeldItemOutCountData(WeldDto dto);

	/**
	 * 设备累计焊接时间
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getWeldMachineInCountData(WeldDto dto,BigInteger itemid);

	/**
	 * 设备超规范焊接时间
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getWeldMachineOutCountData(WeldDto dto,BigInteger itemid);

	/**
	 * 人员累计焊接时间
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getWeldPersonInCountData(WeldDto dto);

	/**
	 * 人员超规范焊接时间
	 * @param dto
	 * @return
	 */
	List<DataStatistics> getWeldPersonOutCountData(WeldDto dto);

	/**
	 * 工件累计焊接时长
	 * @param dto
	 * @param itemid
	 * @return
	 */
	List<DataStatistics> getWeldWorkpieceInCountData(WeldDto dto, String junctionno);

	/**
	 * 工件超规范焊接时长
	 * @param dto
	 * @param itemid
	 * @return
	 */
	List<DataStatistics> getWeldWorkpieceOutCountData(WeldDto dto, String junctionno);
	
	/**
	 * 焊工工作量排行
	 * @param parent 事业部id
	 * @param time 时间
	 * @return
	 */
	List<DataStatistics> getWorkRank(BigInteger parent,String time);
	
	/**
	 * 获取工作的焊机数
	 * @param itemid
	 * @param time
	 * @return
	 */
	DataStatistics getWorkMachineCount(BigInteger itemid, String time);
	
	/**
	 * 获取项目部正常工作时长
	 * @param dto 事业部id，起始时间，结束时间
	 * @return
	 */
	List<DataStatistics> getItemWeldTime(WeldDto dto);
	
	/**
	 * 获取项目部超标工作时长
	 * @param dto 事业部id，起始时间，结束时间
	 * @return
	 */
	List<DataStatistics> getItemOverProofTime(WeldDto dto);
	
	/**
	 * 获取生产任务详情
	 * @param parent 组织机构id
	 * @param welderno 焊机编号
	 * @param taskno 任务编号
	 * @param time1 开始时间
	 * @param time2 结束时间
	 * @return
	 */
	List<DataStatistics> getTaskDetail(BigInteger parent,String welderno,String taskno,String time1,String time2);
	
	/**
	 * 获取所有任务
	 * @param page 分页
	 * @param parent 组织机构id
	 * @param welderno 焊机编号
	 * @param taskno 任务编号
	 * @param time1 开始时间
	 * @param time2 结束时间
	 * @return
	 */
	List<DataStatistics> getTask(Page page,BigInteger parent,String welderno,String taskno,String time1,String time2);
	List<DataStatistics> getTask(BigInteger parent,String welderno,String taskno,String time1,String time2);
	
	/**
	 * 获取焊机任务表
	 * @param page
	 * @param parent 归属id
	 * @param sql 时间查询sql
	 * @param type 分配状态 0：所有，1：未分配，2：已分配
	 * @return
	 */
	List<DataStatistics> getMachineTask(Page page,BigInteger parent,String sql,int type);
	List<DataStatistics> getMachineTask(BigInteger parent,String sql,int type);
	
	String getDay(String time,String time2);
	
	/**
	 * 
	 * @param bigInteger 组织机构id
	 * @param dto 时间
	 * @return
	 */
	List<DataStatistics> getMachineNoTask(BigInteger insid, String time1, String totime, String time2);
	
	/**
	 * 班组生产数据报表
	 * @param bigInteger 组织机构id
	 * @param dto 时间
	 * @return
	 */
	List<DataStatistics> getItemData(Page page, BigInteger insid, WeldDto dto);
	
	/**
	 * 班组生产数据报表
	 * @param bigInteger 组织机构id
	 * @param dto 时间
	 * @return
	 */
	List<DataStatistics> getMachineData(Page page, BigInteger insid, WeldDto dto);
	
	/**
	 * 班组生产数据报表
	 * @param bigInteger 组织机构id
	 * @param dto 时间
	 * @return
	 */
	List<DataStatistics> getWelderData(Page page, BigInteger insid, WeldDto dto);
	
	/**
	 * 班组生产数据报表
	 * @param bigInteger 组织机构id
	 * @param dto 时间
	 * @return
	 */
	List<DataStatistics> getJunctionData(Page page, BigInteger insid, WeldDto dto);
}
