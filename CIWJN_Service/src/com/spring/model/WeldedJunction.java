package com.spring.model;

import java.math.BigInteger;

import javax.persistence.Transient;

import org.springframework.stereotype.Component;

/**
 * 焊口
 * @author gpyf16
 *
 */
@Component
public class WeldedJunction {
	private BigInteger id;
	private String weldedJunctionno;
	private String serialNo;
	private String pipelineNo;
	private String roomNo;
	private String unit;
	private String area;
	private String systems;
	private String children;
	private String externalDiameter;
	private String wallThickness;
	private int dyne;
	private String specification;
	private double maxElectricity;
	private double minElectricity;
	private double maxValtage;
	private double minValtage;
	private String material;//材质（新增字段）
	private String nextexternaldiameter;//下游外径（新增字段）
	private String startTime;
	private String endTime;
	private String creatTime;
	private String updateTime;
	private int updatecount;
	private String nextwall_thickness;
	private String next_material;
	private String electricity_unit;
	private String valtage_unit;
	private BigInteger taskid;
	private BigInteger welderid;
	private BigInteger machineid;
	private BigInteger operatorid;
	private BigInteger iid;
	private String iname;

	private String creator;
	private String modifier;
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getElectricity_unit() {
		return electricity_unit;
	}
	public void setElectricity_unit(String electricity_unit) {
		this.electricity_unit = electricity_unit;
	}
	public String getValtage_unit() {
		return valtage_unit;
	}
	public void setValtage_unit(String valtage_unit) {
		this.valtage_unit = valtage_unit;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getWeldedJunctionno() {
		return weldedJunctionno;
	}
	public void setWeldedJunctionno(String weldedJunctionno) {
		this.weldedJunctionno = weldedJunctionno;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getPipelineNo() {
		return pipelineNo;
	}
	public void setPipelineNo(String pipelineNo) {
		this.pipelineNo = pipelineNo;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getSystems() {
		return systems;
	}
	public void setSystems(String systems) {
		this.systems = systems;
	}
	public String getChildren() {
		return children;
	}
	public void setChildren(String children) {
		this.children = children;
	}
	public String getExternalDiameter() {
		return externalDiameter;
	}
	public void setExternalDiameter(String externalDiameter) {
		this.externalDiameter = externalDiameter;
	}
	public String getWallThickness() {
		return wallThickness;
	}
	public void setWallThickness(String wallThickness) {
		this.wallThickness = wallThickness;
	}
	public int getDyne() {
		return dyne;
	}
	public void setDyne(int dyne) {
		this.dyne = dyne;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public double getMaxElectricity() {
		return maxElectricity;
	}
	public void setMaxElectricity(double maxElectricity) {
		this.maxElectricity = maxElectricity;
	}
	public double getMinElectricity() {
		return minElectricity;
	}
	public void setMinElectricity(double minElectricity) {
		this.minElectricity = minElectricity;
	}
	public double getMaxValtage() {
		return maxValtage;
	}
	public void setMaxValtage(double maxValtage) {
		this.maxValtage = maxValtage;
	}
	public double getMinValtage() {
		return minValtage;
	}
	public void setMinValtage(double minValtage) {
		this.minValtage = minValtage;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getNextexternaldiameter() {
		return nextexternaldiameter;
	}
	public void setNextexternaldiameter(String nextexternaldiameter) {
		this.nextexternaldiameter = nextexternaldiameter;
	}
	public String getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getUpdatecount() {
		return updatecount;
	}
	public void setUpdatecount(int updatecount) {
		this.updatecount = updatecount;
	}
	public String getNextwall_thickness() {
		return nextwall_thickness;
	}
	public void setNextwall_thickness(String nextwall_thickness) {
		this.nextwall_thickness = nextwall_thickness;
	}
	public String getNext_material() {
		return next_material;
	}
	public void setNext_material(String next_material) {
		this.next_material = next_material;
	}
	public BigInteger getTaskid() {
		return taskid;
	}
	public void setTaskid(BigInteger taskid) {
		this.taskid = taskid;
	}
	public BigInteger getWelderid() {
		return welderid;
	}
	public void setWelderid(BigInteger welderid) {
		this.welderid = welderid;
	}
	public BigInteger getMachineid() {
		return machineid;
	}
	public void setMachineid(BigInteger machineid) {
		this.machineid = machineid;
	}
	public BigInteger getOperatorid() {
		return operatorid;
	}
	public void setOperatorid(BigInteger operatorid) {
		this.operatorid = operatorid;
	}
	public BigInteger getIid() {
		return iid;
	}
	public void setIid(BigInteger iid) {
		this.iid = iid;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	
}
