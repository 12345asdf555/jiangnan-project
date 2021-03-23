package com.spring.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.LiveDataMapper;
import com.spring.dao.WeldedJunctionMapper;
import com.spring.dto.ModelDto;
import com.spring.dto.WeldDto;
import com.spring.model.LiveData;
import com.spring.model.WeldedJunction;
import com.spring.service.LiveDataService;

@Service
@Transactional
public class LiveDataServiceImpl implements LiveDataService {
	@Autowired
	private LiveDataMapper live;
	@Autowired
	private WeldedJunctionMapper wm;
	
	@Override
	public List<ModelDto> getCausehour(WeldDto dto, BigInteger parent) {
		try{
			return live.getCausehour(dto,parent);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ModelDto> getCompanyhour(WeldDto dto, BigInteger parent) {
		try{
			return live.getCompanyhour(dto, parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getItemhour(WeldDto dto) {
		try{
			return live.getItemhour(dto);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getJunctionHous(WeldDto dto) {
		try{
			return live.getJunctionHous(dto);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getCauseOverproof(WeldDto dto, BigInteger parent) {
		try{
			return live.getCauseOverproof(dto, parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<LiveData> getAllInsf(BigInteger parent,int type) {
		try{
			return live.getAllInsf(parent,type);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getAllTime(WeldDto dto) {
		try{
			return live.getAllTime(dto);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getCompanyOverproof(WeldDto dto,BigInteger parent) {
		try{
			return live.getCompanyOverproof(dto,parent);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public List<ModelDto> getcompanyOvertime(WeldDto dto, String num,BigInteger parent) {
		try{
			return live.getcompanyOvertime(dto, num,parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getCaustOvertime(WeldDto dto, String num, BigInteger parent) {
		try{
			return live.getCaustOvertime(dto, num, parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getItemOvertime(WeldDto dto, String num) {
		try{
			return live.getItemOvertime(dto, num);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<LiveData> getJunction(BigInteger parent) {
		try{
			return live.getJunction(parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getDetailovertime(WeldDto dto, String num, String junctionno) {
		try{
			return live.getDetailovertime(dto, num,junctionno);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getCompanyLoads(WeldDto dto,BigInteger parent) {
		try{
			return live.getCompanyLoads(dto,parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getCaustLoads(WeldDto dto, BigInteger parent) {
		try{
			return live.getCaustLoads(dto, parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getItemLoads(WeldDto dto, BigInteger parent) {
		try{
			return live.getItemLoads(dto, parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<LiveData> getMachine(BigInteger parent) {
		try{
			return live.getMachine(parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getDetailLoads(WeldDto dto, String machineno) {
		try{
			return live.getDetailLoads(dto, machineno);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getCompanyNoLoads(WeldDto dto,BigInteger parent) {
		try{
			return live.getCompanyNoLoads(dto,parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getCaustNOLoads(WeldDto dto, BigInteger parent) {
		try{
			return live.getCaustNoLoads(dto, parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getItemNOLoads(WeldDto dto, BigInteger parent,String equipmentno) {
		try{
			return live.getItemNOLoads(dto, parent,equipmentno);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getCompanyIdle(WeldDto dto,BigInteger parent) {
		try{
			return live.getCompanyIdle(dto,parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getCaustIdle(WeldDto dto, BigInteger parent) {
		try{
			return live.getCaustIdle(dto, parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getItemIdle(WeldDto dto, BigInteger itemid) {
		try{
			return live.getItemidle(dto, itemid);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public int getMachineCount(BigInteger id) {
		try{
			return live.getMachineCount(id);
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public List<ModelDto> getCompanyUse( WeldDto dto, BigInteger parent) {
		try{
			return live.getCompanyUse(dto, parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getCaustUse( WeldDto dto, BigInteger insid) {
		try{
			return live.getCaustUse(dto, insid);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getAllTimes(WeldDto dto) {
		try{
			return live.getAllTime(dto);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getBlochour( WeldDto dto) {
		try{
			return live.getBlochour(dto);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getBlocOverproof(WeldDto dto) {
		try{
			return live.getBlocOverproof(dto);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getBlocOvertime(WeldDto dto, String num) {
		try{
			return live.getBlocOvertime(dto, num);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getBlocLoads(WeldDto dto) {
		try{
			return live.getBlocLoads(dto);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getBlocNoLoads(WeldDto dto) {
		try{
			return live.getBlocNoLoads(dto);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getBlocIdle(WeldDto dto) {
		try{
			return live.getBlocIdle(dto);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getBlocUse(WeldDto dto, BigInteger parent) {
		try{
			return live.getBlocUse(dto, parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<LiveData> getBlocChildren() {
		try{
			return live.getBlocChildren();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> caustEfficiency(BigInteger parent, WeldDto dto, int min, int max) {
		try{
			return live.caustEfficiency(dto, parent,min,max);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> companyEfficiency(BigInteger parent, WeldDto dto, int min, int max) {
		try{
			return live.companyEfficiency(dto,parent,min,max);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> blocEfficiency(WeldDto dto,BigInteger parent, int min, int max) {
		try{
			return live.blocEfficiency(dto,parent,min,max);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getEfficiencyChartNum(WeldDto dto, BigInteger parent) {
		try{
			return live.getEfficiencyChartNum(dto, parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getEfficiencyChart(WeldDto dto, BigInteger parent, int minnum, int avgnum) {
		try{
			return live.getEfficiencyChart(dto, parent, minnum, avgnum);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public WeldedJunction getWeldedJunctionById(BigInteger id) {
		try{
			return wm.getWeldedJunctionById(id);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getHousClassify( BigInteger parent, String searchStr) {
		try{
			return live.getHousClassify(parent,searchStr);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getDetailNoLoads(WeldDto dto) {
		try{
			return live.getDetailNoLoads(dto);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getItemOverproof(WeldDto dto, BigInteger id) {
		try{
			return live.getItemOverproof(dto, id);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getItemUse( WeldDto dto, BigInteger insid) {
		try{
			return live.getItemUse(dto, insid);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getBlocMachineCount(WeldDto dto, BigInteger parent) {
		try{
			return live.getBlocMachineCount(dto, parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getCompanyMachineCount(WeldDto dto, BigInteger parent) {
		try{
			return live.getCompanyMachineCount(dto, parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getCaustMachineCount(WeldDto dto, BigInteger parent) {
		try{
			return live.getCaustMachineCount(dto, parent);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public BigInteger getCountByTime(BigInteger parent, String time, BigInteger mid) {
		try{
			return live.getCountByTime(parent, time, mid);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getJunctionByWelder(WeldDto dto, String welder) {
		try{
			return live.getJunctionByWelder(dto, welder);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getExcessiveBack(String time, String welder, String junction) {
		try{
			return live.getExcessiveBack(time, welder, junction);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ModelDto> getStandbytimeout(WeldDto dto) {
		try{
			return live.getStandbytimeout(dto);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ModelDto> getGatherMachine() {
		try{
			return live.getGatherMachine();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
