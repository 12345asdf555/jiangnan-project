package com.spring.model;

import java.math.BigInteger;

import javax.persistence.Transient;

import org.springframework.stereotype.Component;

/**
 * 焊机
 * @author gpyf16
 *
 */
@Component
public class WeldingMachine {
	private BigInteger id;
	private String equipmentNo;
	private String position;
	private int isnetworking;
	private String joinTime;
	private int typeId;
	private int statusId;
	private int money;
	private String creator;
	private String modifier;
	@Transient
	private Gather gatherId;
	@Transient
	private Insframework insframeworkId;
	@Transient
	private EquipmentManufacturer manufacturerId;
	
	//导入时用来暂存值
	private String typename;
	private String statusname;
	
	
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
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
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getEquipmentNo() {
		return equipmentNo;
	}
	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Gather getGatherId() {
		return gatherId;
	}
	public void setGatherId(Gather gatherId) {
		this.gatherId = gatherId;
	}
	public int getIsnetworking() {
		return isnetworking;
	}
	public void setIsnetworking(int isnetworking) {
		this.isnetworking = isnetworking;
	}
	public String getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public Insframework getInsframeworkId() {
		return insframeworkId;
	}
	public void setInsframeworkId(Insframework insframeworkId) {
		this.insframeworkId = insframeworkId;
	}
	public EquipmentManufacturer getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(EquipmentManufacturer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getStatusname() {
		return statusname;
	}
	public void setStatusname(String statusname) {
		this.statusname = statusname;
	}
	
	
}
