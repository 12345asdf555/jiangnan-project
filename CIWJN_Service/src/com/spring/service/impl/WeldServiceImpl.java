package com.spring.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.WeldMapper;
import com.spring.model.Weld;
import com.spring.service.WeldService;

import net.sf.json.JSONObject;

@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class WeldServiceImpl implements WeldService {
	
	@Autowired
	private WeldMapper mapper;

	public BigInteger AddWeld(String aweld) {
		try{
		JSONObject json = JSONObject.fromObject(aweld);
		Weld user = new Weld();
		user.setFitemid(new BigInteger(json.getString("ITEM")));
		user.setFname(json.getString("FNAME"));
		user.setFwelder_no(json.getString("FWELDER_NO"));
//		user.setCreator(json.getString("CREATOR"));
		if(mapper.AddWeld(user)){
			return user.getId();
		}else{
			return null;
		}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public Boolean UpdateWeld(String uweld) {
		try{
		JSONObject json = JSONObject.fromObject(uweld);
		Weld user = new Weld();
		user.setFitemid(new BigInteger(json.getString("ITEM")));
		user.setFname(json.getString("FNAME"));
		user.setFwelder_no(json.getString("FWELDER_NO"));
//		user.setModifiter(json.getString("MODIFIER"));
		return mapper.UpdateWeld(user);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}	
	}

	@Override
	public BigInteger AddJunction(String ajunction) {
		try{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		JSONObject json = JSONObject.fromObject(ajunction);
		Weld user = new Weld();
		user.setFitemid(new BigInteger(json.getString("ITEM")));
		String serial = json.getString("FSERIAL_NO");
		if(json.getString("FSERIAL_NO").length()<8){
			for(int ser=json.getString("FSERIAL_NO").length();ser<8;ser++){
				serial = "0"+serial;
			}
		}
		user.setFwjn(serial);
		user.setFsn(json.getString("FWELDED_JUNCTION_NO"));
		user.setFpn(json.getString("FPIPELINE_NO"));
		user.setFrn(json.getString("FROOM_NO"));
		user.setFunit(json.getString("FUNIT"));
		user.setFarea(json.getString("FAREA"));
		user.setFsystems(json.getString("FSYSTEMS"));
		user.setFchildren(json.getString("FCHILDREN"));
		user.setFed(json.getString("FEXTERNAL_DIAMETER"));
		user.setFwt(json.getString("FWALL_THICKNESS"));
		user.setFdyne(Integer.parseInt(json.getString("FDYNE")));
		user.setFspecification(json.getString("FSPECIFICATION"));
		user.setFmaxele(new BigDecimal(json.getString("FMAX_ELECTRICITY")));
		user.setFminele(new BigDecimal(json.getString("FMIN_ELECTRICITY")));
		user.setFmaxval(new BigDecimal(json.getString("FMAX_VALTAGE")));
		user.setFminval(new BigDecimal(json.getString("FMIN_VALTAGE")));
		user.setFele_unit(json.getString("FELECTRICITY_UNIT"));
		user.setFval_unit(json.getString("FVALTAGE_UNIT"));
		user.setFmaterial(json.getString("FMETERIAL"));
		user.setFnd(json.getString("FNEXTEXTERNAL_DIAMETER"));
		user.setFnt(json.getString("FNEXTWALL_THICKNESS"));
		user.setFnm(json.getString("FNEXTMETERIAL"));
		user.setFstart_time(sdf.parse(json.getString("FSTART_TIME")));
//		user.setCreator(json.getString("CREATOR"));
		if(json.getString("FEND_TIME").isEmpty()||json.getString("FEND_TIME")==null){
		user.setFend_time(null);
		}else{
		user.setFend_time(sdf.parse(json.getString("FEND_TIME")));
		}
/*		user.setFcreatetime(sdf.parse(json.getString("fcreatetime")));
		user.setFupdatetime(sdf.parse(json.getString("fupdatetime")));
		user.setFupdatecount(Integer.parseInt(json.getString("fupdatecount")));*/
		if(mapper.AddJunction(user)){
			return user.getId();
		}else{
			return null;
		}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}	
	}

	@Override
	public Boolean UpdateJunction(String ujunction) {
		try{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		JSONObject json = JSONObject.fromObject(ujunction);
		Weld user = new Weld();
		user.setFitemid(new BigInteger(json.getString("ITEM")));
		String serial = json.getString("FSERIAL_NO");
		if(json.getString("FSERIAL_NO").length()<8){
			for(int ser=json.getString("FSERIAL_NO").length();ser<8;ser++){
				serial = "0"+serial;
			}
		}
		user.setFwjn(serial);
		user.setFsn(json.getString("FWELDED_JUNCTION_NO"));
		user.setFpn(json.getString("FPIPELINE_NO"));
		user.setFrn(json.getString("FROOM_NO"));
		user.setFunit(json.getString("FUNIT"));
		user.setFarea(json.getString("FAREA"));
		user.setFsystems(json.getString("FSYSTEMS"));
		user.setFchildren(json.getString("FCHILDREN"));
		user.setFed(json.getString("FEXTERNAL_DIAMETER"));
		user.setFwt(json.getString("FWALL_THICKNESS"));
		user.setFdyne(Integer.parseInt(json.getString("FDYNE")));
		user.setFspecification(json.getString("FSPECIFICATION"));
		user.setFmaxele(new BigDecimal(json.getString("FMAX_ELECTRICITY")));
		user.setFminele(new BigDecimal(json.getString("FMIN_ELECTRICITY")));
		user.setFele_unit(json.getString("FELECTRICITY_UNIT"));
		user.setFval_unit(json.getString("FVALTAGE_UNIT"));
		user.setFmaxval(new BigDecimal(json.getString("FMAX_VALTAGE")));
		user.setFminval(new BigDecimal(json.getString("FMIN_VALTAGE")));
		user.setFmaterial(json.getString("FMETERIAL"));
		user.setFnd(json.getString("FNEXTEXTERNAL_DIAMETER"));
		user.setFnt(json.getString("FNEXTWALL_THICKNESS"));
		user.setFnm(json.getString("FNEXTMETERIAL"));
		user.setFstart_time(sdf.parse(json.getString("FSTART_TIME")));
//		user.setModifiter(json.getString("MODIFIER"));
//		user.setFend_time(sdf.parse(json.getString("FEND_TIME")));
		if(json.getString("FEND_TIME").isEmpty()||json.getString("FEND_TIME")==null){
		user.setFend_time(null);
		}else{
		user.setFend_time(sdf.parse(json.getString("FEND_TIME")));
		}
/*		user.setFcreatetime(sdf.parse(json.getString("fcreatetime")));
		user.setFupdatetime(sdf.parse(json.getString("fupdatetime")));
		user.setFupdatecount(Integer.parseInt(json.getString("fupdatecount")));*/
		return mapper.UpdateJunction(user);
		}catch(Exception e){
			return false;
		}	
	}

	@Override
	public Boolean DeleteJunction(String djunction) {
		try{
		JSONObject json = JSONObject.fromObject(djunction);
		Weld user = new Weld();
		user.setFitemid(new BigInteger(json.getString("ITEM")));
		String serial = json.getString("FSERIAL_NO");
		if(json.getString("FSERIAL_NO").length()<8){
			for(int ser=json.getString("FSERIAL_NO").length();ser<8;ser++){
				serial = "0"+serial;
			}
		}
		user.setFwjn(serial);
		return mapper.DeleteJunction(user);
		}catch(Exception e){
			return false;
		}	
	}

	public BigInteger FindIns_Id(String insname) {
		try{
			JSONObject json = JSONObject.fromObject(insname);
			Weld user = new Weld();
			user.setFname(json.getString("FNAME"));
			return mapper.FindIns_Id(user);
		}catch(Exception e){
			return null;
		}
	}

}