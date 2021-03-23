package com.spring.model;

import java.math.BigInteger;
import java.util.Date;


public class Wps {
	private long fid;
	private BigInteger insid;
	private BigInteger macid;
	private String insname;
	private BigInteger welderid;
	private String weldername;
	private String updatename;
	private String fwpsnum;
	private int fweld_i;
	private int fweld_v;
	private int fweld_i_max;
	private int fweld_i_min;
	private int fweld_v_max;
	private int fweld_v_min;
	private int fweld_alter_i;
	private int fweld_alter_v;
	private int fweld_prechannel;
	private Date fcreatedate;
	private Date fupdatedate;
	private long fcreater;
	private long fupdater;
	private long fowner;
	private String fback;
	private String fname;
	private double fdiameter;
	private double ftime;
	private double fadvance;
	private double fhysteresis;
	private double fini_ele;
	private double fini_vol;
	private double fini_vol1;
	private double fweld_ele;
	private double fweld_vol;
	private double fweld_vol1;
	private double farc_ele;
	private double farc_vol;
	private double farc_vol1;
	private double fweld_tuny_ele;
	private double fweld_tuny_vol;
	private double farc_tuny_ele;
	private double farc_tuny_vol;
	private String finitial;
	private String fcontroller;
	private String fmode;
	private int fstatus;
	private String arcname;
	private String selectname;
	private String gasname;
	private String dianame;
	private String materialname;
	private String conname;
	

	private double fpreset_ele_top;
	private double fpreset_vol_top;
	private double fpreset_ele_bottom;
	private double fpreset_vol_bottom;
	private double farc_vol_top;
	private double fpreset_ele_warn_top;
	private double fpreset_vol_warn_top;
	private double fpreset_ele_warn_bottom;
	private double fpreset_vol_warn_bottom;
	private double fini_ele_warn_top;
	private double fini_vol_warn_top;
	private double fini_ele_warn_bottom;
	private double fini_vol_warn_bottom;
	private double farc_ele_warn_top;
	private double farc_vol_warn_top;
	private double farc_ele_warn_bottom;
	private double farc_vol_warn_bottom;
	private double farc_delay_time;
	private double fwarn_delay_time;
	private double fwarn_stop_time;
	private double fflow_top;
	private double fflow_bottom;
	private double fdelay_time;
	private double fover_time;
	private double ffixed_cycle;
	private double fwarn_ele_up;
	private double fwarn_ele_down;
	private double fwarn_vol_up;
	private double fwarn_vol_down;
	private int fselect;
	private int farc;
	private int fcharacter;
	private int fmaterial;
	private int fgas;
	private int fini;
	private int ftorch;
	private int fprocessid;
	private String fprocessname;
	
	public int getFini() {
		return fini;
	}
	public void setFini(int fini) {
		this.fini = fini;
	}
	private BigInteger fwpslib_id;
	
	public String getConname() {
		return conname;
	}
	public void setConname(String conname) {
		this.conname = conname;
	}
	public BigInteger getFwpslib_id() {
		return fwpslib_id;
	}
	public void setFwpslib_id(BigInteger fwpslib_id) {
		this.fwpslib_id = fwpslib_id;
	}
	public double getFarc_tuny_vol() {
		return farc_tuny_vol;
	}
	public void setFarc_tuny_vol(double farc_tuny_vol) {
		this.farc_tuny_vol = farc_tuny_vol;
	}
	public int getFarc() {
		return farc;
	}
	public void setFarc(int farc) {
		this.farc = farc;
	}
	public int getFcharacter() {
		return fcharacter;
	}
	public void setFcharacter(int fcharacter) {
		this.fcharacter = fcharacter;
	}
	public int getFmaterial() {
		return fmaterial;
	}
	public void setFmaterial(int fmaterial) {
		this.fmaterial = fmaterial;
	}
	public int getFgas() {
		return fgas;
	}
	public void setFgas(int fgas) {
		this.fgas = fgas;
	}
	public int getFselect() {
		return fselect;
	}
	public void setFselect(int fselect) {
		this.fselect = fselect;
	}
	public Wps(){
		super();
	}
	public long getFid() {
		return fid;
	}
	public void setFid(long fid) {
		this.fid = fid;
	}
	public BigInteger getWelderid() {
		return welderid;
	}
	public void setWelderid(BigInteger welderid) {
		this.welderid = welderid;
	}
	public BigInteger getMacid() {
		return macid;
	}
	public void setMacid(BigInteger macid) {
		this.macid = macid;
	}
	public BigInteger getInsid() {
		return insid;
	}
	public void setInsid(BigInteger insid) {
		this.insid = insid;
	}
	public String getWeldername(){
		return weldername;
	}
	public void setWeldername(String weldername){
		this.weldername = weldername;
	}
	public String getUpdatename(){
		return updatename;
	}
	public void setUpdatename(String updatename){
		this.updatename = updatename;
	}
	public String getInsname(){
		return insname;
	}
	public void setInsname(String insname){
		this.insname = insname;
	}
	public String getFwpsnum(){
		return fwpsnum;
	}
	public void setFwpsnum(String fwpsnum){
		this.fwpsnum = fwpsnum;
	}
	public int getFweld_i() {
		return fweld_i;
	}
	public void setFweld_i(int fweld_i) {
		this.fweld_i = fweld_i;
	}
	public int getFweld_v() {
		return fweld_v;
	}
	public void setFweld_v(int fweld_v) {
		this.fweld_v = fweld_v;
	}
	public int getFweld_i_max() {
		return fweld_i_max;
	}
	public void setFweld_i_max(int fweld_i_max) {
		this.fweld_i_max = fweld_i_max;
	}
	public int getFweld_i_min() {
		return fweld_i_min;
	}
	public void setFweld_i_min(int fweld_i_min) {
		this.fweld_i_min = fweld_i_min;
	}
	public int getFweld_v_max() {
		return fweld_v_max;
	}
	public void setFweld_v_max(int fweld_v_max) {
		this.fweld_v_max = fweld_v_max;
	}
	public int getFweld_v_min() {
		return fweld_v_min;
	}
	public void setFweld_v_min(int fweld_v_min) {
		this.fweld_v_min = fweld_v_min;
	}
	public int getFweld_alter_i() {
		return fweld_alter_i;
	}
	public void setFweld_alter_i(int fweld_alter_i) {
		this.fweld_alter_i = fweld_alter_i;
	}
	public int getFweld_alter_v() {
		return fweld_alter_v;
	}
	public void setFweld_alter_v(int fweld_alter_v) {
		this.fweld_alter_v = fweld_alter_v;
	}
	public int getFweld_prechannel() {
		return fweld_prechannel;
	}
	public void setFweld_prechannel(int fweld_prechannel) {
		this.fweld_prechannel = fweld_prechannel;
	}
	public Date getFcreatedate(){
		return fcreatedate;
	}
	public void setFcreatedate(Date fcreatedate){
		this.fcreatedate = fcreatedate;
	}
	public Date getFupdatedate(){
		return fupdatedate;
	}
	public void setFupdatedate(Date fupdatedate){
		this.fupdatedate = fupdatedate;
	}
	public long getFcreater() {
		return fcreater;
	}
	public void setFcreater(long fcreater) {
		this.fcreater = fcreater;
	}
	public long getFupdater() {
		return fupdater;
	}
	public void setFupdater(long fupdater) {
		this.fupdater = fupdater;
	}
	public long getFowner() {
		return fowner;
	}
	public void setFowner(long fowner) {
		this.fowner = fowner;
	}
	public String getFback(){
		return fback;
	}
	public void setFback(String fback){
		this.fback = fback;
	}
	public String getFname(){
		return fname;
	}
	public void setFname(String fname){
		this.fname = fname;
	}
	public double getFdiameter(){
		return fdiameter;
	}
	public void setFdiameter(double fdiameter){
		this.fdiameter = fdiameter;
	}
	public Wps(long fid,BigInteger insid,BigInteger macid,String insname,BigInteger welderid,String weldername,String updatename,String fwpsnum,
			int fweld_i,int fweld_v,int fweld_i_max,int fweld_i_min,int fweld_v_max,int fweld_v_min,int fweld_alter_i,int fweld_alter_v,int fweld_prechannel,Date fcreatedate,Date fupdatedate,long fcreater,long fupdater,long fowner,
			String fback,String fname,double fdiameter,double ftime,double fadvance,double fhysteresis,double fini_ele,double fini_vol,double fini_vol1,double fweld_ele,
			double fweld_vol,double fweld_vol1,double farc_ele,double farc_vol,double farc_vol1,double fweld_tuny_ele,double fweld_tuny_vol,double farc_tuny_ele,String finitial,
			String fcontroller,String fmode,int fstatus,String arcname,String selectname,String gasname,String dianame,String materialname,int ftorch,int fprocessid,
			String fprocessname,double fwarn_ele_up,double fwarn_ele_down,double fwarn_vol_up,double fwarn_vol_down) {
		super();
		this.macid = macid;
		this.insname = insname;
		this.weldername = weldername;
		this.updatename = updatename;
		this.insid = insid;
		this.welderid = welderid;
		this.fid = fid;
		this.fwpsnum = fwpsnum;
		this.fweld_i = fweld_i;
		this.fweld_v = fweld_v;
		this.fweld_i_max = fweld_i_max;
		this.fweld_i_min = fweld_i_min;
		this.fweld_v_max = fweld_v_max;
		this.fweld_v_min = fweld_v_min;
		this.fweld_alter_i = fweld_alter_i;
		this.fweld_alter_v = fweld_alter_v;
		this.fweld_prechannel = fweld_prechannel;
		this.fcreatedate = fcreatedate;
		this.fupdatedate = fupdatedate;
		this.fcreater = fcreater;
		this.fupdater = fupdater;
		this.fowner = fowner;
		this.fback = fback;
		this.fname = fname;
		this.fdiameter = fdiameter;
		this.ftime = ftime;
		this.fadvance = fadvance;
		this.fhysteresis = fhysteresis;
		this.fini_ele = fini_ele;
		this.fini_vol = fini_vol;
		this.fini_vol1 = fini_vol1;
		this.fweld_ele = fweld_ele;
		this.fweld_vol = fweld_vol1;
		this.farc_ele = farc_ele;
		this.farc_vol = farc_vol;
		this.farc_vol1 = farc_vol1;
		this.fweld_tuny_ele = fweld_tuny_ele;
		this.fweld_tuny_vol = fweld_tuny_vol;
		this.farc_tuny_ele = farc_tuny_ele;
		this.finitial = finitial;
		this.fcontroller = fcontroller;
		this.fmode = fmode;
		this.fstatus = fstatus;
		this.arcname = arcname;
		this.gasname = gasname;
		this.selectname = selectname;
		this.materialname = materialname;
		this.dianame = dianame;
		this.fprocessid = fprocessid;
		this.fprocessname = fprocessname;
		this.ftorch = ftorch;
		this.setFwarn_ele_up(fwarn_ele_up);
		this.setFwarn_ele_down(fwarn_ele_down);
		this.setFwarn_vol_up(fwarn_vol_up);
		this.setFwarn_vol_down(fwarn_vol_down);
	}
	public double getFtime() {
		return ftime;
	}
	public void setFtime(double ftime) {
		this.ftime = ftime;
	}
	public double getFadvance() {
		return fadvance;
	}
	public void setFadvance(double fadvance) {
		this.fadvance = fadvance;
	}
	public double getFhysteresis() {
		return fhysteresis;
	}
	public void setFhysteresis(double fhysteresis) {
		this.fhysteresis = fhysteresis;
	}
	public double getFini_ele() {
		return fini_ele;
	}
	public void setFini_ele(double fini_ele) {
		this.fini_ele = fini_ele;
	}
	public double getFini_vol() {
		return fini_vol;
	}
	public void setFini_vol(double fini_vol) {
		this.fini_vol = fini_vol;
	}
	public double getFweld_ele() {
		return fweld_ele;
	}
	public void setFweld_ele(double fweld_ele) {
		this.fweld_ele = fweld_ele;
	}
	public double getFweld_vol() {
		return fweld_vol;
	}
	public void setFweld_vol(double fweld_vol) {
		this.fweld_vol = fweld_vol;
	}
	public double getFarc_ele() {
		return farc_ele;
	}
	public void setFarc_ele(double farc_ele) {
		this.farc_ele = farc_ele;
	}
	public double getFarc_vol() {
		return farc_vol;
	}
	public void setFarc_vol(double farc_vol) {
		this.farc_vol = farc_vol;
	}
	public double getFweld_tuny_ele() {
		return fweld_tuny_ele;
	}
	public void setFweld_tuny_ele(double fweld_tuny_ele) {
		this.fweld_tuny_ele = fweld_tuny_ele;
	}
	public double getFweld_tuny_vol() {
		return fweld_tuny_vol;
	}
	public void setFweld_tuny_vol(double fweld_tuny_vol) {
		this.fweld_tuny_vol = fweld_tuny_vol;
	}
	public double getFarc_tuny_ele() {
		return farc_tuny_ele;
	}
	public void setFarc_tuny_ele(double farc_tuny_ele) {
		this.farc_tuny_ele = farc_tuny_ele;
	}
	public String getFinitial() {
		return finitial;
	}
	public void setFinitial(String finitial) {
		this.finitial = finitial;
	}
	public String getFcontroller() {
		return fcontroller;
	}
	public void setFcontroller(String fcontroller) {
		this.fcontroller = fcontroller;
	}
	public String getFmode() {
		return fmode;
	}
	public void setFmode(String fmode) {
		this.fmode = fmode;
	}
	public double getFini_vol1() {
		return fini_vol1;
	}
	public void setFini_vol1(double fini_vol1) {
		this.fini_vol1 = fini_vol1;
	}
	public double getFweld_vol1() {
		return fweld_vol1;
	}
	public void setFweld_vol1(double fweld_vol1) {
		this.fweld_vol1 = fweld_vol1;
	}
	public double getFarc_vol1() {
		return farc_vol1;
	}
	public void setFarc_vol1(double farc_vol1) {
		this.farc_vol1 = farc_vol1;
	}
	public int getFstatus() {
		return fstatus;
	}
	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}
	public String getArcname() {
		return arcname;
	}
	public void setArcname(String arcname) {
		this.arcname = arcname;
	}
	public String getSelectname() {
		return selectname;
	}
	public void setSelectname(String selectname) {
		this.selectname = selectname;
	}
	public String getGasname() {
		return gasname;
	}
	public void setGasname(String gasname) {
		this.gasname = gasname;
	}
	public String getDianame() {
		return dianame;
	}
	public void setDianame(String dianame) {
		this.dianame = dianame;
	}
	public String getMaterialname() {
		return materialname;
	}
	public void setMaterialname(String materialname) {
		this.materialname = materialname;
	}
	public double getFpreset_ele_top() {
		return fpreset_ele_top;
	}
	public void setFpreset_ele_top(double fpreset_ele_top) {
		this.fpreset_ele_top = fpreset_ele_top;
	}
	public double getFpreset_vol_top() {
		return fpreset_vol_top;
	}
	public void setFpreset_vol_top(double fpreset_vol_top) {
		this.fpreset_vol_top = fpreset_vol_top;
	}
	public double getFpreset_ele_bottom() {
		return fpreset_ele_bottom;
	}
	public void setFpreset_ele_bottom(double fpreset_ele_bottom) {
		this.fpreset_ele_bottom = fpreset_ele_bottom;
	}
	public double getFpreset_vol_bottom() {
		return fpreset_vol_bottom;
	}
	public void setFpreset_vol_bottom(double fpreset_vol_bottom) {
		this.fpreset_vol_bottom = fpreset_vol_bottom;
	}
	public double getFarc_vol_top() {
		return farc_vol_top;
	}
	public void setFarc_vol_top(double farc_vol_top) {
		this.farc_vol_top = farc_vol_top;
	}
	public double getFpreset_ele_warn_top() {
		return fpreset_ele_warn_top;
	}
	public void setFpreset_ele_warn_top(double fpreset_ele_warn_top) {
		this.fpreset_ele_warn_top = fpreset_ele_warn_top;
	}
	public double getFpreset_vol_warn_top() {
		return fpreset_vol_warn_top;
	}
	public void setFpreset_vol_warn_top(double fpreset_vol_warn_top) {
		this.fpreset_vol_warn_top = fpreset_vol_warn_top;
	}
	public double getFpreset_ele_warn_bottom() {
		return fpreset_ele_warn_bottom;
	}
	public void setFpreset_ele_warn_bottom(double fpreset_ele_warn_bottom) {
		this.fpreset_ele_warn_bottom = fpreset_ele_warn_bottom;
	}
	public double getFpreset_vol_warn_bottom() {
		return fpreset_vol_warn_bottom;
	}
	public void setFpreset_vol_warn_bottom(double fpreset_vol_warn_bottom) {
		this.fpreset_vol_warn_bottom = fpreset_vol_warn_bottom;
	}
	public double getFini_ele_warn_top() {
		return fini_ele_warn_top;
	}
	public void setFini_ele_warn_top(double fini_ele_warn_top) {
		this.fini_ele_warn_top = fini_ele_warn_top;
	}
	public double getFini_vol_warn_top() {
		return fini_vol_warn_top;
	}
	public void setFini_vol_warn_top(double fini_vol_warn_top) {
		this.fini_vol_warn_top = fini_vol_warn_top;
	}
	public double getFini_ele_warn_bottom() {
		return fini_ele_warn_bottom;
	}
	public void setFini_ele_warn_bottom(double fini_ele_warn_bottom) {
		this.fini_ele_warn_bottom = fini_ele_warn_bottom;
	}
	public double getFini_vol_warn_bottom() {
		return fini_vol_warn_bottom;
	}
	public void setFini_vol_warn_bottom(double fini_vol_warn_bottom) {
		this.fini_vol_warn_bottom = fini_vol_warn_bottom;
	}
	public double getFarc_ele_warn_top() {
		return farc_ele_warn_top;
	}
	public void setFarc_ele_warn_top(double farc_ele_warn_top) {
		this.farc_ele_warn_top = farc_ele_warn_top;
	}
	public double getFarc_vol_warn_top() {
		return farc_vol_warn_top;
	}
	public void setFarc_vol_warn_top(double farc_vol_warn_top) {
		this.farc_vol_warn_top = farc_vol_warn_top;
	}
	public double getFarc_ele_warn_bottom() {
		return farc_ele_warn_bottom;
	}
	public void setFarc_ele_warn_bottom(double farc_ele_warn_bottom) {
		this.farc_ele_warn_bottom = farc_ele_warn_bottom;
	}
	public double getFarc_vol_warn_bottom() {
		return farc_vol_warn_bottom;
	}
	public void setFarc_vol_warn_bottom(double farc_vol_warn_bottom) {
		this.farc_vol_warn_bottom = farc_vol_warn_bottom;
	}
	public double getFarc_delay_time() {
		return farc_delay_time;
	}
	public void setFarc_delay_time(double farc_delay_time) {
		this.farc_delay_time = farc_delay_time;
	}
	public double getFwarn_delay_time() {
		return fwarn_delay_time;
	}
	public void setFwarn_delay_time(double fwarn_delay_time) {
		this.fwarn_delay_time = fwarn_delay_time;
	}
	public double getFwarn_stop_time() {
		return fwarn_stop_time;
	}
	public void setFwarn_stop_time(double fwarn_stop_time) {
		this.fwarn_stop_time = fwarn_stop_time;
	}
	public double getFflow_top() {
		return fflow_top;
	}
	public void setFflow_top(double fflow_top) {
		this.fflow_top = fflow_top;
	}
	public double getFflow_bottom() {
		return fflow_bottom;
	}
	public void setFflow_bottom(double fflow_bottom) {
		this.fflow_bottom = fflow_bottom;
	}
	public double getFdelay_time() {
		return fdelay_time;
	}
	public void setFdelay_time(double fdelay_time) {
		this.fdelay_time = fdelay_time;
	}
	public double getFover_time() {
		return fover_time;
	}
	public void setFover_time(double fover_time) {
		this.fover_time = fover_time;
	}
	public double getFfixed_cycle() {
		return ffixed_cycle;
	}
	public void setFfixed_cycle(double ffixed_cycle) {
		this.ffixed_cycle = ffixed_cycle;
	}
	public int getFprocessid() {
		return fprocessid;
	}
	public void setFprocessid(int fprocessid) {
		this.fprocessid = fprocessid;
	}
	public String getFprocessname() {
		return fprocessname;
	}
	public void setFprocessname(String fprocessname) {
		this.fprocessname = fprocessname;
	}
	public int getFtorch() {
		return ftorch;
	}
	public void setFtorch(int ftorch) {
		this.ftorch = ftorch;
	}
	public double getFwarn_ele_up() {
		return fwarn_ele_up;
	}
	public void setFwarn_ele_up(double fwarn_ele_up) {
		this.fwarn_ele_up = fwarn_ele_up;
	}
	public double getFwarn_ele_down() {
		return fwarn_ele_down;
	}
	public void setFwarn_ele_down(double fwarn_ele_down) {
		this.fwarn_ele_down = fwarn_ele_down;
	}
	public double getFwarn_vol_up() {
		return fwarn_vol_up;
	}
	public void setFwarn_vol_up(double fwarn_vol_up) {
		this.fwarn_vol_up = fwarn_vol_up;
	}
	public double getFwarn_vol_down() {
		return fwarn_vol_down;
	}
	public void setFwarn_vol_down(double fwarn_vol_down) {
		this.fwarn_vol_down = fwarn_vol_down;
	}
	
}
