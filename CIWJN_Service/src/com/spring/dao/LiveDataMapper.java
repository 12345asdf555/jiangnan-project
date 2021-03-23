package com.spring.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.ModelDto;
import com.spring.dto.WeldDto;
import com.spring.model.LiveData;
import com.spring.model.WeldingMachine;

import tk.mybatis.mapper.common.Mapper;

public interface LiveDataMapper extends Mapper<LiveData>{
	List<ModelDto> getCausehour(@Param("dto") WeldDto dto,@Param("parent") BigInteger parent);
	
	List<ModelDto> getCompanyhour(@Param("dto") WeldDto dto,@Param("parent") BigInteger parent);

	List<ModelDto> getItemhour(@Param("dto") WeldDto dto);
	
	List<ModelDto> getJunctionHous(@Param("dto") WeldDto dto);
	
	List<ModelDto> getCompanyOverproof(@Param("dto") WeldDto dto,@Param("parent") BigInteger parent);
	
	List<ModelDto> getCauseOverproof(@Param("dto") WeldDto dto,@Param("parent") BigInteger parent);
	
	List<ModelDto> getItemOverproof(@Param("dto") WeldDto dto,@Param("id") BigInteger id);
	
	List<ModelDto> getDatailOverproof(@Param("dto") WeldDto dto,@Param("parent") BigInteger parent);
	
	List<LiveData> getAllInsf(@Param("parent") BigInteger parent,@Param("type") int type);
	
	List<ModelDto> getAllTime(@Param("dto") WeldDto dto);
	
	int getCountTime(@Param("welderno") String welderno,@Param("machineno") String machineno,@Param("junctionno") String junctionno,@Param("time") String time,@Param("id") BigInteger id);

	List<ModelDto> getjunctionoverproof(@Param("welderno") String welderno,@Param("machineno") String machineno,@Param("junctionno") String junctionno,@Param("time") String time,@Param("id") BigInteger id);

	List<ModelDto> getcompanyOvertime(@Param("dto") WeldDto dto,@Param("num") String num,@Param("parent") BigInteger parent);
	
	List<ModelDto> getCaustOvertime(@Param("dto") WeldDto dto,@Param("num") String num,@Param("parent") BigInteger parent);
	
	List<ModelDto> getItemOvertime(@Param("dto") WeldDto dto,@Param("num") String num);
	
	List<LiveData> getJunction(@Param("parent") BigInteger parent);
	
	List<LiveData> getMachine(@Param("parent") BigInteger parent);
	
	List<ModelDto> getDetailovertime(@Param("dto") WeldDto dto,@Param("num") String num,@Param("parent") String parent);
	
	List<ModelDto> getCompanyLoads(@Param("dto")WeldDto dto,@Param("parent") BigInteger parent);
	
	List<ModelDto> getCaustLoads(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent);
	
	List<ModelDto> getItemLoads(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent);
	
	List<ModelDto> getDetailLoads(@Param("dto")WeldDto dto,@Param("machineno")String machineno);
	
	List<ModelDto> getCompanyNoLoads(@Param("dto")WeldDto dto,@Param("parent") BigInteger parent);
	
	List<ModelDto> getDetailNoLoads(@Param("dto")WeldDto dto);
	
	List<ModelDto> getCaustNoLoads(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent);
	
	List<ModelDto> getItemNOLoads(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent,@Param("equipmentno")String equipmentno);
	
	List<ModelDto> getCompanyIdle(@Param("dto")WeldDto dto,@Param("parent") BigInteger parent);
	
	List<ModelDto> getCaustIdle(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent);
	
	List<ModelDto> getItemidle(@Param("dto")WeldDto dto,@Param("itemid")BigInteger itemid);
	
	int getMachineCount(@Param("id") BigInteger id);
	
	List<ModelDto> getCompanyUse(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent);
	
	List<ModelDto> getCaustUse(@Param("dto")WeldDto dto,@Param("insid")BigInteger insid);
	
	List<ModelDto> getItemUse(@Param("dto")WeldDto dto,@Param("insid")BigInteger insid);
	
	List<ModelDto> getBlochour(@Param("dto") WeldDto dto);
	
	List<ModelDto> getBlocOverproof(@Param("dto") WeldDto dto);
	
	List<ModelDto> getBlocOvertime(@Param("dto") WeldDto dto,@Param("num") String num);
	
	List<ModelDto> getBlocLoads(@Param("dto")WeldDto dto);
	
	List<ModelDto> getBlocNoLoads(@Param("dto")WeldDto dto);
	
	List<ModelDto> getBlocIdle(@Param("dto")WeldDto dto);
	
	List<ModelDto> getBlocUse(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent);
	
	List<LiveData> getBlocChildren();
	
	List<ModelDto> caustEfficiency(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent,@Param("min")int min,@Param("max")int max);
	
	List<ModelDto> companyEfficiency(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent,@Param("min")int min,@Param("max")int max);

	List<ModelDto> blocEfficiency(@Param("dto")WeldDto dto,@Param("parent") BigInteger parent,@Param("min")int min,@Param("max")int max);
	
	List<ModelDto> getEfficiencyChartNum(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent);
	
	List<ModelDto> getEfficiencyChart(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent,@Param("minnum")int minnum,@Param("avgnum")int avgnum);
	
	List<ModelDto> getHousClassify(@Param("parent")BigInteger parent,@Param("str")String str);
	
	BigInteger getDyneByJunctionno(@Param("str") String str);
	
	List<ModelDto> getBlocMachineCount(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent);
	
	List<ModelDto> getCompanyMachineCount(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent);
	
	List<ModelDto> getCaustMachineCount(@Param("dto")WeldDto dto,@Param("parent")BigInteger parent);
	
	BigInteger getCountByTime(@Param("parent")BigInteger parent,@Param("time")String time,@Param("mid")BigInteger mid);
	
	List<ModelDto> getJunctionByWelder(@Param("dto")WeldDto dto,@Param("welder")String welder);
	
	List<ModelDto> getExcessiveBack(@Param("time")String time,@Param("welder")String welder,@Param("junction")String junction);
	
	List<ModelDto> getStandbytimeout(@Param("dto")WeldDto dto);
	
	List<ModelDto> getGatherMachine();
}
