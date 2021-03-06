package com.spring.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.model.Dictionarys;
import com.spring.model.Gather;
import com.spring.model.Insframework;
import com.spring.model.MaintenanceRecord;
import com.spring.model.MyUser;
import com.spring.model.Person;
import com.spring.model.WeldedJunction;
import com.spring.model.WeldingMachine;
import com.spring.model.WeldingMaintenance;
import com.spring.service.DictionaryService;
import com.spring.service.GatherService;
import com.spring.service.MaintainService;
import com.spring.service.PersonService;
import com.spring.service.WeldedJunctionService;
import com.spring.service.WeldingMachineService;
import com.spring.util.IsnullUtil;
import com.spring.util.UploadUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * excel???????????????
 * @author gpyf16
 *
 */

@Controller
@RequestMapping(value = "/import", produces = { "text/json;charset=UTF-8" })
public class ImportExcelController {
	@Autowired
	private WeldingMachineService wmm;
	@Autowired
	private MaintainService mm;
	@Autowired
	private GatherService gs;
	@Autowired
	private PersonService ps;
	@Autowired
	private DictionaryService dm;
	@Autowired
	private WeldedJunctionService wjs;
	
	IsnullUtil iutil = new IsnullUtil();
	
	@RequestMapping("importGather")
	@ResponseBody
	public String importGather(HttpServletRequest request,HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		String path = "";
		try{
			path = u.uploadFile(request, response);
			List<Gather> list = xlsxGather(path);
			//??????????????????excel??????
			File file  = new File(path);
			file.delete();
			for(Gather g : list){
				g.setItemid(wmm.getInsframeworkByName(g.getItemname()));
				//????????????
				int count1 = gs.getGatherNoCount(g.getGatherNo(),g.getItemid());
				if(count1>0){
					/*obj.put("msg","????????????????????????????????????????????????????????????");
					obj.put("success",false);
					return obj.toString();*/
					continue;
				}else{
					gs.addGather(g);
				}
			};
			obj.put("success",true);
			obj.put("msg","???????????????");
		}catch(Exception e){
			e.printStackTrace();
			obj.put("msg","???????????????????????????????????????????????????????????????????????????");
			obj.put("success",false);
		}
		return obj.toString();
	}
	
	/**
	 * ??????????????????
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/importWeldingMachine")
	@ResponseBody
	public String importWeldingMachine(HttpServletRequest request,
			HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		String path = "";
		try{
			path = u.uploadFile(request, response);
			List<WeldingMachine> list = xlsxWm(path);
			//??????????????????excel??????
			File file  = new File(path);
			file.delete();
			for(WeldingMachine wm : list){
				wm.setTypeId(dm.getvaluebyname(4,wm.getTypename()));
				wm.setStatusId(dm.getvaluebyname(3,wm.getStatusname()));
				wm.setMvalueid(dm.getvaluebyname(14, wm.getMvaluename()));
				wm.setModel(String.valueOf(dm.getvaluebyname(17, wm.getModel())));
				String name = wm.getInsframeworkId().getName();
				wm.getInsframeworkId().setId(wmm.getInsframeworkByName(name));
				Gather gather = wm.getGatherId();
				int count2 = 0;
				if(gather!=null){
					int count3 = gs.getGatherNoByItemCount(gather.getGatherNo(), wm.getInsframeworkId().getId()+"");
					if(count3 == 0){
						obj.put("msg","?????????????????????????????????????????????????????????????????????????????????");
						obj.put("success",false);
						return obj.toString();
					}
					gather.setId(gs.getGatherByNo(gather.getGatherNo()));
					wm.setGatherId(gather);
					count2 = wmm.getGatheridCount(wm.getInsframeworkId().getId(),gather.getGatherNo());
				}
				if(isInteger(wm.getEquipmentNo())){
					wm.setEquipmentNo(wm.getEquipmentNo());
				}
				wm.setGatherId(gather);
				//????????????
				int count1 = wmm.getEquipmentnoCount(wm.getEquipmentNo());
				if(count2>0){
					obj.put("msg","??????????????????????????????????????????????????????????????????");
					obj.put("success",false);
					return obj.toString();
				}else if(count1>0){
					continue;
				}else{
					wmm.addWeldingMachine(wm);
				}
				List<Dictionarys> model = dm.getModelOfManu(wm.getMvalueid());
				boolean modelflag = true;
				for(int i=0;i<model.size();i++){
					if(wm.getModel().equals(model.get(i).getId().toString())){
						modelflag = false;
					}
				}
				if(modelflag){
					obj.put("msg","????????????????????????????????????????????????????????????????????????");
					obj.put("success",false);
					return obj.toString();
				}
			};
			obj.put("success",true);
			obj.put("msg","???????????????");
		}catch(Exception e){
			e.printStackTrace();
			obj.put("msg","???????????????????????????????????????????????????????????????????????????");
			obj.put("success",false);
		}
		return obj.toString();
	}
	
	/**
	 * ??????????????????
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/importMaintain")
	@ResponseBody
	public String importMaintain(HttpServletRequest request,
			HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		try{
			String path = u.uploadFile(request, response);
			List<WeldingMaintenance> wt = xlsxMaintain(path);
			//??????????????????excel??????
			File file  = new File(path);
			file.delete();
			for(int i=0;i<wt.size();i++){
				wt.get(i).getMaintenance().setTypeId(dm.getvaluebyname(5,wt.get(i).getMaintenance().getTypename()));
				BigInteger wmid = null;
				if(isInteger(wt.get(i).getWelding().getEquipmentNo())){
					wmid = wmm.getWeldingMachineByEno(wt.get(i).getWelding().getEquipmentNo());
				}else{
					wmid = wmm.getWeldingMachineByEno(wt.get(i).getWelding().getEquipmentNo());
				}
				wt.get(i).getWelding().setId(wmid);
				//???????????????
				mm.addMaintian( wt.get(i),wt.get(i).getMaintenance(),wmid);
			};
			obj.put("success",true);
			obj.put("msg","???????????????");
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success",false);
			obj.put("msg","???????????????????????????????????????????????????????????????????????????");
		}
		return obj.toString();
	}
	
	
	/**
	 * ??????????????????
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/importWelder")
	@ResponseBody
	public String importWelder(HttpServletRequest request,
			HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		try{
			String path = u.uploadFile(request, response);
			List<Person> we = xlsxWelder(path);
			//??????????????????excel??????
			File file  = new File(path);
			file.delete();
			for(Person w:we){
				if(w.getWelderno().length()>8){
					w.setWelderno(w.getWelderno().substring(0, 8));
				}else if(w.getWelderno().length()<8){
					for(int i=w.getWelderno().length();i<8;i++){
						w.setWelderno("0"+w.getWelderno());
					}
				}
				try {
					if(w.getLevename() != null && !"".equals(w.getLevename())) {
						w.setLeveid(dm.getvaluebyname(8,w.getLevename()));
					}
					if(w.getQualiname() != null && !"".equals(w.getQualiname())) {
						w.setQuali(dm.getvaluebyname(7, w.getQualiname()));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					obj.put("msg","????????????????????????????????????????????????????????????");
					obj.put("success",false);
					return obj.toString();
				}
				w.setOwner(wmm.getInsframeworkByName(w.getInsname()));
				MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				w.setCreater(new BigInteger(user.getId()+""));
				w.setUpdater(new BigInteger(user.getId()+""));
				w.setWelderno(w.getWelderno());
				String phone = w.getCellphone();
				if(iutil.isNull(phone)){
					if(!phone.matches("^1[3-8]\\d{9}$")){
						obj.put("msg","?????????????????????????????????????????????????????????");
						obj.put("success",false);
						return obj.toString();
					}
				}
				//????????????
				int count1 = ps.getUsernameCount(w.getWelderno());
				if(count1>0){
					/*
					 * obj.put("msg","????????????????????????????????????????????????????????????"); obj.put("success",false); return
					 * obj.toString();
					 */
//					continue;
					ps.updateByWelderno(w);
				}else{
					ps.save(w);
				}
			};
			obj.put("success",true);
			obj.put("msg","???????????????");
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success",false);
			obj.put("msg",e.getMessage());
		}
		return obj.toString();
	}
	

	/**
	 * ??????????????????
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/importWeldedJunction")
	@ResponseBody
	public String importWeldedJunction(HttpServletRequest request,
			HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		try{
			String path = u.uploadFile(request, response);
			List<WeldedJunction> we = xlsxWeldedJunction(path);
			//??????????????????excel??????
			File file  = new File(path);
			file.delete();
			for(WeldedJunction w:we){
				String wjno = w.getWeldedJunctionno();
				int num = wjno.length();
				if(num<=6){
					for(int i=0;i<6-num;i++){
						wjno = "0"+wjno;
					} 
				}else{
					obj.put("success",false);
					obj.put("msg","?????????????????????????????????????????????????????????????????????");
					return obj.toString();
				}
				w.setWeldedJunctionno(wjno);
				int count = wjs.getWeldedjunctionByNo(wjno);
				w.setInsfid(wmm.getInsframeworkByName(w.getIname()));
				MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				w.setCreater(new BigInteger(user.getId()+""));
				w.setUpdater(new BigInteger(user.getId()+""));
				w.setWeldedJunctionno(w.getWeldedJunctionno());
				//????????????
				if(count>0){
//					obj.put("msg","????????????????????????????????????????????????????????????");
//					obj.put("success",false);
//					return obj.toString();
					continue;
				}
				wjs.addJunction(w);
			};
			obj.put("success",true);
			obj.put("msg","???????????????");
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success",false);
			obj.put("msg","???????????????????????????????????????????????????????????????????????????");
		}
		return obj.toString();
	}
	
	/**
	 * ??????????????????
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/importWeldTask")
	@ResponseBody
	public String importWeldTask(HttpServletRequest request,
		HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		String str = "";
		int biaozhi = 0;
		try{
			String path = u.uploadFile(request, response);
			List<WeldedJunction> we = xlsxWeldTask(path);
			//??????????????????excel??????
			File file  = new File(path);
			file.delete();
			for(WeldedJunction w:we){
				String wjno = w.getWeldedJunctionno();
				w.setWeldedJunctionno(wjno);
				json.put("taskNo", w.getWeldedJunctionno());
				if(w.getWeldedJunctionno()==null||"".equals(w.getWeldedJunctionno())){
					str+="????????????????????????;";
					biaozhi=1;
				}else{
					int count = wjs.getWeldedjunctionByNo(wjno);
					if(count>0){
						str+="????????????????????????;";
						biaozhi=1;
					}
				}
				if("".equals(w.getSerialNo())||w.getSerialNo()==null){
					json.put("levelname", "");
				}else{
					json.put("levelname", w.getSerialNo());
					String lll = dm.getValueByNameAndType(8, w.getSerialNo());
					if(lll==null||lll=="null"){
						str+="???????????????????????????;";
						biaozhi=1;
					}else{
						json.put("levelid", String.valueOf(lll));
					}
				}
//				json.put("insName", w.getItemid().getName());
				BigInteger iii = null;
				if(w.getIname()==null){
					str+="??????????????????;";
					json.put("insName", "");
					biaozhi=1;
				}else{
					json.put("insName", w.getIname());
					iii = wmm.getInsframeworkByName(w.getIname());
					if(String.valueOf(iii)==null||String.valueOf(iii).equals("null")){
						str+="??????????????????;";
						biaozhi=1;
					}else{
						json.put("insId", iii);
					}
				}
/*				if((w.getPipelineNo()==null)||(w.getPipelineNo()=="")){
					json.put("welderNo", "");
				}else{
					json.put("welderNo", w.getPipelineNo());
					Person www = ps.getIdByWelderno(w.getPipelineNo());
					if(String.valueOf(www)==null||String.valueOf(www)=="null"){
						str+="???????????????;";
						biaozhi=1;
					}else{
						if(!www.getInsid().equals(iii)){
							str+="???????????????????????????????????????;";
							biaozhi=1;
						}else{
							json.put("welderId", www.getId());
						}
					}
				}
				if((w.getRoomNo()=="")||(w.getRoomNo()==null)){
					json.put("quali", "");
				}else{
					json.put("quali", w.getRoomNo());
					String qqq = dm.getValueByNameAndType(7, w.getRoomNo());
					if(qqq==null||qqq=="null"){
						str+="?????????????????????;";
						biaozhi=1;
					}else{
						json.put("qualiid", String.valueOf(qqq));
					}
				}*/
				json.put("start", w.getUnit());
				json.put("end", w.getArea());
				json.put("str", str);
/*				MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				w.setCreater(new BigInteger(user.getId()+""));
				w.setUpdater(new BigInteger(user.getId()+""));*/
/*				if(w.getInsfid()==null){
					continue;
				}
				if(w.getSerialNo()==null){
					w.setSerialNo("");
				}
				if(w.getSystems()==null){
					w.setSystems("");
				}
				if(w.getUnit()==null){
					w.setUnit("");
				}
				if(w.getArea()==null){
					w.setArea("");
				}
				if(w.getExternalDiameter()==null){
					w.setExternalDiameter("");
				}*/
				//?????????????????????
/*				JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
				Client client = dcf.createClient("http://121.196.222.216:8080/CIWJN_Service/cIWJNWebService?wsdl");
				iutil.Authority(client);
				String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"addJunction\"}";
				String obj2 = "{\"JUNCTIONNO\":\""+w.getWeldedJunctionno()+"\",\"SERIALNO\":\""+w.getSerialNo()+"\",\"DYNE\":\""+w.getSystems()+"\"," +
						"\"INSFID\":\""+w.getInsfid()+"\",\"STARTTIME\":\""+w.getUnit()+"\",\"ENDTIME\":\""+w.getArea()+"\",\"EXTERNALDIAMETER\":\""+w.getExternalDiameter()+"\"}";
				Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"), new Object[]{obj1,obj2});  
				if(objects[0].toString().equals("true")){
					obj.put("success",true);
					obj.put("msg","???????????????");
				}else if(!objects[0].toString().equals("false")){
					obj.put("success", true);
					obj.put("msg", objects[0].toString());
				}else{
					obj.put("success", false);
					obj.put("errorMsg", "???????????????");
				}*/
				ary.add(json);
				str="";
			};
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("rows", ary);
		obj.put("biaozhi", biaozhi);
		return obj.toString();
	}
	
	/**
	 * ??????WeldingMaintenance?????????
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<WeldingMaintenance> xlsxMaintain(String path) throws IOException, InvalidFormatException{
		List<WeldingMaintenance> wm = new ArrayList<WeldingMaintenance>();
		InputStream stream = new FileInputStream(path);
		Workbook workbook = create(stream);
		Sheet sheet = workbook.getSheetAt(0);
		
		int rowstart = sheet.getFirstRowNum()+1;
		int rowEnd = sheet.getLastRowNum();
	    
		for(int i=rowstart;i<=rowEnd;i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();
			WeldingMaintenance dit = new WeldingMaintenance();
			MaintenanceRecord mr = new MaintenanceRecord();
			for(int k = cellStart; k<= cellEnd;k++){
				Cell cell = row.getCell(k);
				if(null == cell){
					continue;
				}
				
				String cellValue = "";
				
				switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC://??????
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// ?????????????????????????????????  
		                SimpleDateFormat sdf = null;  
		                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
		                        .getBuiltinFormat("h:mm")) {  
		                    sdf = new SimpleDateFormat("HH:mm");  
		                } else {// ??????  
		                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                }  
		                Date date = cell.getDateCellValue();  
		                cellValue = sdf.format(date);  
		            } else if (cell.getCellStyle().getDataFormat() == 58) {  
		                // ??????????????????????????????m???d???(??????????????????????????????id?????????id?????????58)  
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                double value = cell.getNumericCellValue();  
		                Date date = org.apache.poi.ss.usermodel.DateUtil  
		                        .getJavaDate(value);  
		                cellValue = sdf.format(date);  
		            } else {
                        double value = cell.getNumericCellValue();
                        int intValue = (int) value;
                        cellValue = value - intValue == 0 ? String.valueOf(intValue) : String.valueOf(value);
                    }
					if(k == 0){
						WeldingMachine welding = new WeldingMachine();
						welding.setEquipmentNo(cellValue);
						dit.setWelding(welding);//????????????
						break;
					}
					else if(k == 2){
						mr.setStartTime(cellValue);//??????????????????
						break;
					}
					else if(k == 3){
						mr.setEndTime(cellValue);//??????????????????
						break;
	    			}
					break;
				case HSSFCell.CELL_TYPE_STRING://?????????
					cellValue = cell.getStringCellValue();
					if(k == 0){
						WeldingMachine welding = new WeldingMachine();
						welding.setEquipmentNo(cellValue);
						dit.setWelding(welding);//????????????
						break;
					}
					else if(k == 1){
						mr.setViceman(cellValue);//????????????
						break;
					}
					else if(k == 4){
						mr.setTypename(cellValue);
						break;
 					}
					else if(k == 5){
 						mr.setDesc(cellValue);//????????????
						break;
 					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // ??????
					cellValue = String.valueOf(cell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_BLANK: // ??????
					cellValue = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // ??????
					cellValue = "";
					break;
				default:
					cellValue = cell.toString().trim();
					break;
				}
			}
			dit.setMaintenance(mr);
			wm.add(dit);
		}
		
		return wm;
	}
	
	/**
	 * ??????Wedlingmachine?????????
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<WeldingMachine> xlsxWm(String path) throws IOException, InvalidFormatException{
		List<WeldingMachine> wm = new ArrayList<WeldingMachine>();
		InputStream stream = new FileInputStream(path);
		Workbook workbook = create(stream);
		for(int h=0;h<workbook.getNumberOfSheets();h++) {
		Sheet sheet = workbook.getSheetAt(h);
		int rowstart = sheet.getFirstRowNum()+1;
		int rowEnd = sheet.getLastRowNum();
		for(int i=rowstart;i<=rowEnd;i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();
			WeldingMachine dit = new WeldingMachine();
			for(int k = cellStart; k<= cellEnd;k++){
				Cell cell = row.getCell(k);
				if(null == cell){
					continue;
				}
				
				String cellValue = "";
				
				switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC://??????
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// ?????????????????????????????????  
		                SimpleDateFormat sdf = null;  
		                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
		                        .getBuiltinFormat("h:mm")) {  
		                    sdf = new SimpleDateFormat("HH:mm");  
		                } else {// ??????  
		                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                }  
		                Date date = cell.getDateCellValue();  
		                cellValue = sdf.format(date);  
		            } else if (cell.getCellStyle().getDataFormat() == 58) {  
		                // ??????????????????????????????m???d???(??????????????????????????????id?????????id?????????58)  
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                double value = cell.getNumericCellValue();  
		                Date date = org.apache.poi.ss.usermodel.DateUtil  
		                        .getJavaDate(value);  
		                cellValue = sdf.format(date);  
		            } else {
		            	 //???????????????????????????x.xxxE9
		            	 BigDecimal big=new BigDecimal(cell.getNumericCellValue());  
		            	 cellValue = big.toString();
                    }
					if(k == 0){
						dit.setEquipmentNo(cellValue);//????????????
						break;
					}
					else if(k == 2){
						dit.setJoinTime(cellValue);//????????????
						break;
					}
					//??????????????????????????????????????????
					else if(k == 7){
						Gather g = new Gather();
						g.setGatherNo(cellValue);
						dit.setGatherId(g);//????????????
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_STRING://?????????
					cellValue = cell.getStringCellValue();
					if(k == 0){
						dit.setEquipmentNo(cellValue);//????????????
						break;
					}
					else if(k == 1){
						dit.setTypename(cellValue);//????????????
						break;
					}
					else if(k == 3){
 						Insframework ins = new Insframework();
 						ins.setName(cellValue);
 						dit.setInsframeworkId(ins);//????????????
						break;
	    			}
					else if(k == 4){
			        	dit.setStatusname(cellValue);//??????
						break;
 					}
					else if(k == 5){
 						dit.setMvaluename(cellValue);//??????
						break;
 					}
					else if(k == 6){
						if(cellValue.equals("???")){
	 						dit.setIsnetworking(0);//????????????
						}else{
	 						dit.setIsnetworking(1);
						}
						break;
 					}
					//??????????????????????????????????????????
					else if(k == 7){
						Gather g = new Gather();
						g.setGatherNo(cellValue);
						dit.setGatherId(g);//????????????
						break;
					}
					else if(k == 8){
						dit.setPosition(cellValue);//??????
						break;
					}
					else if(k == 9){
						dit.setIp(cellValue);//ip??????
						break;
					}
					else if(k == 10){
						dit.setModel(cellValue);//????????????
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // ??????
					cellValue = String.valueOf(cell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_BLANK: // ??????
					cellValue = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // ??????
					cellValue = "";
					break;
				default:
					cellValue = cell.toString().trim();
					break;
				}
			}
			wm.add(dit);
		}
		}	
		return wm;
	}

	
	/**
	 * ??????Welder?????????
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<Person> xlsxWelder(String path) throws IOException, InvalidFormatException{
		List<Person> welder = new ArrayList<Person>();
		InputStream stream = new FileInputStream(path);
		Workbook workbook = create(stream);
		Sheet sheet = workbook.getSheetAt(0);
		
		int rowstart = sheet.getFirstRowNum()+1;
		int rowEnd = sheet.getLastRowNum();
	    
		for(int i=rowstart;i<=rowEnd;i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();
			Person p = new Person();
			for(int k = cellStart; k<= cellEnd;k++){
				Cell cell = row.getCell(k);
				if(null == cell){
					continue;
				}
				
				String cellValue = "";
				
				switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC://??????
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// ?????????????????????????????????  
		                SimpleDateFormat sdf = null;  
		                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
		                        .getBuiltinFormat("h:mm")) {  
		                    sdf = new SimpleDateFormat("HH:mm");  
		                } else {// ??????  
		                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                }  
		                Date date = cell.getDateCellValue();  
		                cellValue = sdf.format(date);  
		            } else if (cell.getCellStyle().getDataFormat() == 58) {  
		                // ??????????????????????????????m???d???(??????????????????????????????id?????????id?????????58)  
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                double value = cell.getNumericCellValue();  
		                Date date = org.apache.poi.ss.usermodel.DateUtil  
		                        .getJavaDate(value);  
		                cellValue = sdf.format(date);  
		            } else {
		            	 //???????????????????????????x.xxxE9
		            	 BigDecimal big=new BigDecimal(cell.getNumericCellValue());  
		            	 cellValue = big.toString();
                   }
					if(k == 1){
						p.setWelderno(cellValue);//????????????
						break;
					}
					else if(k == 2){
						p.setCellphone(cellValue);//??????
						break;
 					}
					else if(k == 4){
						p.setCardnum(cellValue);//??????
						break;
 					}
					break;
				case HSSFCell.CELL_TYPE_STRING://?????????
					cellValue = cell.getStringCellValue();
					if(k == 0){
						p.setName(cellValue);//??????
						break;
					}
					else if(k == 1){
						p.setWelderno(cellValue);//????????????
						break;
					}
					else if(k == 2){
						p.setCellphone(cellValue);//??????
						break;
 					}
					else if(k == 3){
						p.setLevename(cellValue);//??????
						break;
 					}
					else if(k == 4){
						p.setCardnum(cellValue);//??????
						break;
 					}
					else if(k == 5){
						p.setQualiname(cellValue);//??????
						break;
 					}
					else if(k == 6){
						p.setInsname(cellValue);//??????
						break;
 					}
					else if(k == 7){
						p.setBack(cellValue);//??????
						break;
 					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // ??????
					cellValue = String.valueOf(cell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_BLANK: // ??????
					cellValue = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // ??????
					cellValue = "";
					break;
				default:
					cellValue = cell.toString().trim();
					break;
				}
			}
			welder.add(p);
		}
		
		return welder;
	}
	
	
	/**
	 * ??????Weldedjunction?????????
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<WeldedJunction> xlsxWeldedJunction(String path) throws IOException, InvalidFormatException{
		List<WeldedJunction> junction = new ArrayList<WeldedJunction>();
		InputStream stream = new FileInputStream(path);
		Workbook workbook = create(stream);
		Sheet sheet = workbook.getSheetAt(0);
		
		int rowstart = sheet.getFirstRowNum()+1;
		int rowEnd = sheet.getLastRowNum();
	    
		for(int i=rowstart;i<=rowEnd;i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();
			WeldedJunction p = new WeldedJunction();
			for(int k = cellStart; k<= cellEnd;k++){
				Cell cell = row.getCell(k);
				if(null == cell){
					continue;
				}
				
				String cellValue = "";
				
				switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC://??????
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// ?????????????????????????????????  
		                SimpleDateFormat sdf = null;  
		                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
		                        .getBuiltinFormat("h:mm")) {  
		                    sdf = new SimpleDateFormat("HH:mm");  
		                } else {// ??????  
		                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                }  
		                Date date = cell.getDateCellValue();  
		                cellValue = sdf.format(date);  
		            } else if (cell.getCellStyle().getDataFormat() == 58) {  
		                // ??????????????????????????????m???d???(??????????????????????????????id?????????id?????????58)  
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                double value = cell.getNumericCellValue();  
		                Date date = org.apache.poi.ss.usermodel.DateUtil  
		                        .getJavaDate(value);  
		                cellValue = sdf.format(date);  
		            } else {
		            	String num = String.valueOf(cell.getNumericCellValue());
		            	 //???????????????????????????x.xxxE9
		            	BigDecimal big=new BigDecimal(cell.getNumericCellValue());
		            	//???????????????????????????
		            	Pattern pattern = Pattern.compile("^\\d+\\.\\d+$");
		            	Matcher isNum = pattern.matcher(big+"");
		            	if(isNum.matches()){
		            		//????????????????????????????????????????????????????????????????????????21.3??????21.39999999999999857891452847979962825775146484375
		            		cellValue = num;
		            	}else{
		            		cellValue = big.toString();
		            	}
//		            	 BigDecimal big=new BigDecimal(cell.getNumericCellValue());  
//		            	 cellValue = big.toString();
                   }
					if(k == 0){
						p.setWeldedJunctionno(cellValue);//??????
						break;
					}
					else if(k == 1){
						p.setSerialNo(cellValue);//?????????
						break;
					}
					else if(k == 6){
						p.setDyne(Integer.parseInt(cellValue));//??????
						break;
					}
					else if(k == 8){
						p.setPipelineNo(cellValue);//?????????
						break;
					}
					else if(k == 9){
						p.setRoomNo(cellValue);//?????????
						break;
					}
					else if(k == 10){
						p.setExternalDiameter(cellValue);//????????????
						break;
					}
					else if(k == 11){
						p.setNextexternaldiameter(cellValue);//????????????
						break;
					}
					else if(k == 12){
						p.setWallThickness(cellValue);//????????????
						break;
					}
					else if(k == 13){
						p.setNextwall_thickness(cellValue);//????????????
						break;
					}
					else if(k == 16){
						p.setMaxElectricity(Double.valueOf(cellValue));//????????????
						break;
					}
					else if(k == 17){
						p.setMinElectricity(Double.valueOf(cellValue));//????????????
						break;
					}
					else if(k == 18){
						p.setMaxValtage(Double.valueOf(cellValue));//????????????
						break;
					}
					else if(k == 19){
						p.setMinValtage(Double.valueOf(cellValue));//????????????
						break;
					}
					else if(k == 22){
						p.setStartTime(cellValue);//????????????
						break;
					}
					else if(k == 23){
						p.setEndTime(cellValue);//????????????
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_STRING://?????????
					cellValue = cell.getStringCellValue();
					if(k == 0){
						p.setWeldedJunctionno(cellValue);//??????
						break;
					}
					else if(k == 1){
						p.setSerialNo(cellValue);//?????????
						break;
					}
					else if(k == 2){
						p.setUnit(cellValue);//??????
						break;
					}
					else if(k == 3){
						p.setArea(cellValue);//??????
						break;
					}
					else if(k == 4){
						p.setSystems(cellValue);//??????
						break;
					}
					else if(k == 5){
						p.setChildren(cellValue);//??????
						break;
					}
					else if(k == 7){
						p.setSpecification(cellValue);//??????
						break;
					}
					else if(k == 8){
						p.setPipelineNo(cellValue);//?????????
						break;
					}
					else if(k == 9){
						p.setRoomNo(cellValue);//?????????
						break;
					}
					else if(k == 14){
						p.setMaterial(cellValue);//????????????
						break;
					}
					else if(k == 15){
						p.setNext_material(cellValue);//????????????
						break;
					}
					else if(k == 20){
						p.setElectricity_unit(cellValue);//????????????
						break;
					}
					else if(k == 21){
						p.setValtage_unit(cellValue);//????????????
						break;
					}
					else if(k == 24){
/*						Insframework insf = new Insframework();
						insf.setName(cellValue);*/
						p.setIname(cellValue);//????????????
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // ??????
					cellValue = String.valueOf(cell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_BLANK: // ??????
					cellValue = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // ??????
					cellValue = "";
					break;
				default:
					cellValue = cell.toString().trim();
					break;
				}
			}
			junction.add(p);
		}
		
		return junction;
	}
	
	
	/**
	 * ??????WeldTask?????????
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<WeldedJunction> xlsxWeldTask(String path) throws IOException, InvalidFormatException{
		List<WeldedJunction> junction = new ArrayList<WeldedJunction>();
		InputStream stream = new FileInputStream(path);
		Workbook workbook = create(stream);
		Sheet sheet = workbook.getSheetAt(0);
		
		int rowstart = sheet.getFirstRowNum()+1;
		int rowEnd = sheet.getLastRowNum();
	    
		for(int i=rowstart;i<=rowEnd;i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();
			WeldedJunction p = new WeldedJunction();
			for(int k = cellStart; k<= cellEnd;k++){
				Cell cell = row.getCell(k);
				if(null == cell){
					continue;
				}
				
				String cellValue = "";
				
				switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC://??????
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// ?????????????????????????????????  
		                SimpleDateFormat sdf = null;  
		                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
		                        .getBuiltinFormat("h:mm")) {  
		                    sdf = new SimpleDateFormat("HH:mm");  
		                } else {// ??????  
		                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                }  
		                Date date = cell.getDateCellValue();  
		                cellValue = sdf.format(date);  
		            } else if (cell.getCellStyle().getDataFormat() == 58) {  
		                // ??????????????????????????????m???d???(??????????????????????????????id?????????id?????????58)  
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                double value = cell.getNumericCellValue();  
		                Date date = org.apache.poi.ss.usermodel.DateUtil  
		                        .getJavaDate(value);  
		                cellValue = sdf.format(date);  
		            } else {
		            	String num = String.valueOf(cell.getNumericCellValue());
		            	 //???????????????????????????x.xxxE9
		            	BigDecimal big=new BigDecimal(cell.getNumericCellValue());
		            	//???????????????????????????
		            	Pattern pattern = Pattern.compile("^\\d+\\.\\d+$");
		            	Matcher isNum = pattern.matcher(big+"");
		            	if(isNum.matches()){
		            		//????????????????????????????????????????????????????????????????????????21.3??????21.39999999999999857891452847979962825775146484375
		            		cellValue = num;
		            	}else{
		            		cellValue = big.toString();
		            	}
//		            	 BigDecimal big=new BigDecimal(cell.getNumericCellValue());  
//		            	 cellValue = big.toString();
                   }
					if(k == 0){
						p.setWeldedJunctionno(cellValue);//????????????
						break;
					}
					else if(k == 1){
						p.setSerialNo(cellValue);//????????????
						break;
					}
/*					else if(k == 2){
						p.setPipelineNo(cellValue);//????????????
						break;
					}
					else if(k == 3){
						p.setRoomNo(cellValue);//????????????
						break;
					}*/
					else if(k == 2){
/*						Insframework insf = new Insframework();
						insf.setName(cellValue);*/
						p.setIname(cellValue);//????????????
						break;
					}
					else if(k == 3){
						p.setUnit(cellValue);//????????????
						break;
					}
					else if(k == 4){
						p.setArea(cellValue);//????????????
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_STRING://?????????
					cellValue = cell.getStringCellValue();
					if(k == 0){
						p.setWeldedJunctionno(cellValue);//????????????
						break;
					}
					else if(k == 1){
						p.setSerialNo(cellValue);//????????????
						break;
					}
/*					else if(k == 2){
						p.setPipelineNo(cellValue);//????????????
						break;
					}
					else if(k == 3){
						p.setRoomNo(cellValue);//????????????
						break;
					}*/
					else if(k == 2){
/*						Insframework insf = new Insframework();
						insf.setName(cellValue);*/
						p.setIname(cellValue);//????????????
						break;
					}
					else if(k == 3){
						p.setUnit(cellValue);//????????????
						break;
					}
					else if(k == 4){
						p.setArea(cellValue);//????????????
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // ??????
					cellValue = String.valueOf(cell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_BLANK: // ??????
					cellValue = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // ??????
					cellValue = "";
					break;
				default:
					cellValue = cell.toString().trim();
					break;
				}
			}
			junction.add(p);
		}
		
		return junction;
	}
	
	/**
	 * ??????Wedlingmachine?????????
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<Gather> xlsxGather(String path) throws IOException, InvalidFormatException{
		List<Gather> gather = new ArrayList<Gather>();
		InputStream stream = new FileInputStream(path);
		Workbook workbook = create(stream);
		Sheet sheet = workbook.getSheetAt(0);
		
		int rowstart = sheet.getFirstRowNum()+1;
		int rowEnd = sheet.getLastRowNum();
	    
		for(int i=rowstart;i<=rowEnd;i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();
			Gather dit = new Gather();
			for(int k = cellStart; k<= cellEnd;k++){
				Cell cell = row.getCell(k);
				if(null == cell){
					continue;
				}
				
				String cellValue = "";
				
				switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC://??????
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// ?????????????????????????????????  
		                SimpleDateFormat sdf = null;  
		                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
		                        .getBuiltinFormat("h:mm")) {  
		                    sdf = new SimpleDateFormat("HH:mm");  
		                } else {// ??????  
		                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                }  
		                Date date = cell.getDateCellValue();  
		                cellValue = sdf.format(date);  
		            } else if (cell.getCellStyle().getDataFormat() == 58) {  
		                // ??????????????????????????????m???d???(??????????????????????????????id?????????id?????????58)  
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                double value = cell.getNumericCellValue();  
		                Date date = org.apache.poi.ss.usermodel.DateUtil  
		                        .getJavaDate(value);  
		                cellValue = sdf.format(date);  
		            } else {
		            	 //???????????????????????????x.xxxE9
		            	 BigDecimal big=new BigDecimal(cell.getNumericCellValue());  
		            	 cellValue = big.toString();
                    }
					if(k == 0){
						dit.setGatherNo(cellValue);//????????????
						break;
					}
					else if(k == 6){
						dit.setLeavetime(cellValue);//????????????
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_STRING://?????????
					cellValue = cell.getStringCellValue();
					if(k == 0){
						dit.setGatherNo(cellValue);//????????????
						break;
					}
					else if(k == 1){
 						Insframework ins = new Insframework();
 						ins.setName(cellValue);
 						dit.setItemname(cellValue);//????????????
						break;
					}
					else if(k == 2){
 						dit.setStatus(cellValue);//????????????
						break;
					}
					else if(k == 3){
 						dit.setProtocol(cellValue);//??????????????????
						break;
	    			}
					else if(k == 4){
			        	dit.setIpurl(cellValue);//ip??????
						break;
 					}
					else if(k == 5){
 						dit.setMacurl(cellValue);//mac??????
						break;
 					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // ??????
					cellValue = String.valueOf(cell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_BLANK: // ??????
					cellValue = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // ??????
					cellValue = "";
					break;
				default:
					cellValue = cell.toString().trim();
					break;
				}
			}
			gather.add(dit);
		}
		
		return gather;
	}
	
	
	public static Workbook create(InputStream in) throws IOException,InvalidFormatException {
		if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            return new HSSFWorkbook(in);
        }
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            return new XSSFWorkbook(OPCPackage.open(in));
        }
        throw new IllegalArgumentException("??????excel????????????poi????????????");
    }
	
	public static boolean isInteger(String str) {  
	     Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
	     return pattern.matcher(str).matches();  
	 }
}
