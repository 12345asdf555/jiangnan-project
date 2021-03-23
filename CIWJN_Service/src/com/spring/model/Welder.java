package com.spring.model;

import java.math.BigInteger;

/**
 * 焊工
 * @author gpyf16
 *
 */
public class Welder {
	private BigInteger id;
	private String welderno;
	private String name;
	private String iname;
	private BigInteger iid;
	private String creator;
	private String modifier;
	private String phone;
	private String level;
	private String quality;
	private String cardnum;
	
	public BigInteger getIid() {
		return iid;
	}
	public void setIid(BigInteger iid) {
		this.iid = iid;
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
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getWelderno() {
		return welderno;
	}
	public void setWelderno(String welderno) {
		this.welderno = welderno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getCardnum() {
		return cardnum;
	}
	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}
	
}
