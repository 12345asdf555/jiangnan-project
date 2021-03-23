package com.spring.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


/**
 * 用户
 *
 *
 */
public class Weld{
	/**
	 * 
	 */
	private BigInteger id;
	private String fwelder_no;
	private String fname;
	private String fwjn;
	private String fsn;
	private String fpn;
	private String frn;
	private String funit;
	private String farea;
	private String fsystems;
	private String fchildren;
	private String fed;
	private String fwt;
	private int fdyne;
	private String fspecification;
	private BigDecimal fmaxele;
	private BigDecimal fminele;
	private String fele_unit;
	private BigDecimal fmaxval;
	private BigDecimal fminval;
	private String fval_unit;
	private BigInteger fitemid;
	private String fmaterial;
	private String fnd;
	private String fnt;
	private String fnm;
	private Date fstart_time;
	private Date fend_time;
	private String creator;
	private String modifier;
	public Weld(){
		super();
	}
	
	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
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

	public void setModifiter(String modifier) {
		this.modifier = modifier;
	}

	public String getFwelder_no(){
		return fwelder_no;
	}
	public void setFwelder_no(String fwelder_no){
		this.fwelder_no = fwelder_no;
	}
	public String getFname(){
		return fname;
	}
	public void setFname(String fname){
		this.fname = fname;
	}
	public String getFwjn(){
		return fwjn;
	}
	public void setFwjn(String fwjn){
		this.fwjn = fwjn;
	}
	public String getFsn(){
		return fsn;
	}
	public void setFsn(String fsn){
		this.fsn = fsn;
	}
	public String getFpn(){
		return fpn;
	}
	public void setFpn(String fpn){
		this.fpn = fpn;
	}
	public String getFrn(){
		return frn;
	}
	public void setFrn(String frn){
		this.frn = frn;
	}
	public String getFunit(){
		return funit;
	}
	public void setFunit(String funit){
		this.funit = funit;
	}
	public String getFarea(){
		return farea;
	}
	public void setFarea(String farea){
		this.farea = farea;
	}
	public String getFsystems(){
		return fsystems;
	}
	public void setFsystems(String fsystems){
		this.fsystems = fsystems;
	}
	public String getFchildren(){
		return fchildren;
	}
	public void setFchildren(String fchildren){
		this.fchildren = fchildren;
	}
	public String getFed(){
		return fed;
	}
	public void setFed(String fed){
		this.fed = fed;
	}
	public String getFwt(){
		return fwt;
	}
	public void setFwt(String fwt){
		this.fwt = fwt;
	}
	public int getFdyne(){
		return fdyne;
	}
	public void setFdyne(int fdyne){
		this.fdyne = fdyne;
	}
	public String getFspecification(){
		return fspecification;
	}
	public void setFspecification(String fspecification){
		this.fspecification = fspecification;
	}
	public BigDecimal getFmaxele(){
		return fmaxele;
	}
	public void setFmaxele(BigDecimal fmaxele){
		this.fmaxele = fmaxele;
	}
	public BigDecimal getFminele(){
		return fminele;
	}
	public void setFminele(BigDecimal fminele){
		this.fminele = fminele;
	}
	public String getFele_unit(){
		return fele_unit;
	}
	public void setFele_unit(String fele_unit){
		this.fele_unit = fele_unit;
	}
	public String getFval_unit(){
		return fval_unit;
	}
	public void setFval_unit(String fval_unit){
		this.fval_unit = fval_unit;
	}
	public BigDecimal getFmaxval(){
		return fmaxval;
	}
	public void setFmaxval(BigDecimal fmaxval){
		this.fmaxval = fmaxval;
	}
	public BigDecimal getFminval(){
		return fminval;
	}
	public void setFminval(BigDecimal fminval){
		this.fminval = fminval;
	}
	public BigInteger getFitemid(){
		return fitemid;
	}
	public void setFitemid(BigInteger fitemid){
		this.fitemid = fitemid;
	}
	public String getFmaterial(){
		return fmaterial;
	}
	public void setFmaterial(String fmaterial){
		this.fmaterial = fmaterial;
	}
	public String getFnd(){
		return fnd;
	}
	public void setFnd(String fnd){
		this.fnd = fnd;
	}
	public String getFnt(){
		return fnt;
	}
	public void setFnt(String fnt){
		this.fnt = fnt;
	}
	public String getFnm(){
		return fnm;
	}
	public void setFnm(String fnm){
		this.fnm = fnm;
	}
	public Date getFstart_time(){
		return fstart_time;
	}
	public void setFstart_time(Date fstart_time){
		this.fstart_time = fstart_time;
	}
	public Date getFend_time(){
		return fend_time;
	}
	public void setFend_time(Date fend_time){
		this.fend_time = fend_time;
	}
	public Weld(String fwelder_no,String fname,String fwjn,String fsn,String fpn,String frn,String funit,String farea,String fsystems,String fchildren,String fed,String fwt,int fdyne,String fspecification,BigDecimal fmaxele,BigDecimal fminele,String fele_unit,BigDecimal fmaxval,BigDecimal fminval,String fval_unit,BigInteger fitemid,String fmaterial,String fnd,String fnt,String fnm,Date fstart_time,Date fend_time){
			super();

			this.fwelder_no = fwelder_no;
			this.fname = fname;
			this.fwjn = fwjn;
			this.fsn = fsn;
			this.fpn = fpn;
			this.frn = frn;
			this.funit = funit;
			this.farea = farea;
			this.fsystems = fsystems;
			this.fchildren = fchildren;
			this.fed = fed;
			this.fwt = fwt;
			this.fdyne = fdyne;
			this.fspecification = fspecification;
			this.fmaxele = fmaxele;
			this.fminele = fminele;
			this.fele_unit = fele_unit;
			this.fmaxval = fmaxval;
			this.fminval = fminval;
			this.fval_unit = fval_unit;
			this.fitemid = fitemid;
			this.fmaterial = fmaterial;
			this.fnd = fnd;
			this.fnt = fnt;
			this.fnm = fnm;
			this.fstart_time = fstart_time;
			this.fend_time = fend_time;

	}
}