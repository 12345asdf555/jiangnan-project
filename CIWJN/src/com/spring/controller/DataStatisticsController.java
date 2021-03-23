package com.spring.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.spring.dto.ModelDto;
import com.spring.dto.WeldDto;
import com.spring.model.DataStatistics;
import com.spring.model.Dictionarys;
import com.spring.model.LiveData;
import com.spring.page.Page;
import com.spring.service.DataStatisticsService;
import com.spring.service.DictionaryService;
import com.spring.service.InsframeworkService;
import com.spring.service.LiveDataService;
import com.spring.util.IsnullUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/datastatistics", produces = { "text/json;charset=UTF-8" })
public class DataStatisticsController {
	private Page page;
	private int pageIndex = 1;
	private int pageSize = 10;
	private int total = 0;
	
	@Autowired
	private DataStatisticsService dss;
	@Autowired
	private DictionaryService dm;
	@Autowired
	private LiveDataService ls;
	@Autowired
	private InsframeworkService im;

	IsnullUtil iutil = new IsnullUtil();
	
	@RequestMapping("openMachineTask")
	public String openMachineTask(){
		return "datastatistics/machinetask";
	}
	
	/**
	 * 跳转班组生产数据页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/goItemData")
	public String goItemProductionData(HttpServletRequest request){
		return "datastatistics/itemdata";
	}

	/**
	 * 跳转设备生产数据页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/goMachineData")
	public String goMachineProductionData(HttpServletRequest request){
		return "datastatistics/machinedata";
	}
	
	@RequestMapping("/goTaskDetail")
	public String goTaskDetail(HttpServletRequest request){
		return "datastatistics/taskdetail";
	}
	
	/**
	 * 跳转人员生产数据页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/goPersonData")
	public String goPersonProductionData(HttpServletRequest request){
		return "datastatistics/persondata";
	}
	
	
	/**
	 * 跳转人员生产数据页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/goWorkpieceData")
	public String goWorkpieceProductionData(HttpServletRequest request){
		return "datastatistics/workpiecedata";
	}
	
	/**
	 * 跳转班组焊接数据页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/goWeldItemData")
	public String goWeldItemProductionData(HttpServletRequest request){
		return "welddatastatistics/itemdata";
	}

	/**
	 * 跳转设备焊接数据页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/goWeldMachineData")
	public String goWeldMachineProductionData(HttpServletRequest request){
		return "welddatastatistics/machinedata";
	}
	
	/**
	 * 跳转人员焊接数据页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/goWeldPersonData")
	public String goWeldPersonProductionData(HttpServletRequest request){
		return "welddatastatistics/persondata";
	}
	
	
	/**
	 * 跳转人员焊接数据页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/goWeldWorkpieceData")
	public String goWeldWorkpieceProductionData(HttpServletRequest request){
		return "welddatastatistics/workpiecedata";
	}
	
	/**
	 * 跳转故障报表页面
	 * @return
	 */
	@RequestMapping("/goFauit")
	public String goFauit(HttpServletRequest request){
		request.setAttribute("t1",request.getParameter("t1"));
		request.setAttribute("t2",request.getParameter("t2"));
		request.setAttribute("fauit",request.getParameter("fauit"));
		return "datastatistics/fauit";
	}
	
	/**
	 * 跳转故障报表明细页面
	 * @return
	 */
	@RequestMapping("/goFauitDetail")
	public String goFauitDetail(HttpServletRequest request){
		request.setAttribute("id",request.getParameter("id"));
		request.setAttribute("parenttime1",request.getParameter("time1"));
		request.setAttribute("parenttime2",request.getParameter("time2"));
		request.setAttribute("fauit",request.getParameter("fauit"));
		return "datastatistics/fauitdetail";
	}
	
	/**
	 * 跳转班组生产数据报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getItemData")
	@ResponseBody
	public String getItemProductionData(HttpServletRequest request){
		if(iutil.isNull(request.getParameter("page"))){
			pageIndex = Integer.parseInt(request.getParameter("page"));
		}
		if(iutil.isNull(request.getParameter("rows"))){
			pageSize = Integer.parseInt(request.getParameter("rows"));
		}
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		page = new Page(pageIndex,pageSize,total);
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject title = new JSONObject();
		WeldDto dto = new WeldDto();
		JSONArray titleary = new JSONArray();
		long total = 0;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			List<DataStatistics> list = dss.getItemData(page,im.getUserInsframework(),dto);
			List<DataStatistics> listMachineCount = dss.getItemMachineCount(im.getUserInsframework());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(time2));
			calendar.add(Calendar.HOUR, -1);
			String totime = sdf.format(calendar.getTime());
			List<DataStatistics> mNoTask = dss.getMachineNoTask(im.getUserInsframework(),time1,totime,time2);
			if(list != null){
				PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(list);
				total = pageinfo.getTotal();
			}
			for(DataStatistics i:list){
				json.put("t0", i.getName());//所属班组
				for(DataStatistics mc:listMachineCount) {
					if(mc.getName().equals(i.getName())) {
						json.put("t1", mc.getTotal());//设备总数
						break;
					}
				}
				json.put("t2", i.getWelderid());//开机设备数
				json.put("t3", i.getMachineid());//实焊设备数
				String machineStr = "";
				int macNum = 0;
				for(DataStatistics m:mNoTask){
				if(!("").equals(m.getId()) && m.getId()!=null){
					if(i.getTime().toString().equals(m.getId().toString())){
						machineStr = machineStr + m.getName()+"~";
						macNum++;
					}
				}
				}
				json.put("t4", macNum);
				json.put("t5", new BigDecimal(i.getMachineid()).divide(new BigDecimal(i.getMachineid().add(i.getWelderid())),2,RoundingMode.CEILING).multiply(new BigDecimal(100)));//设备利用率
				json.put("t6", i.getTaskid());//焊接焊缝数
				json.put("t7", getTimeStrBySecond(i.getId()));//焊接时间
				json.put("t8", getTimeStrBySecond(i.getId().add(i.getInsid())));//工作时间
				json.put("t9", new BigDecimal(i.getId()).divide(new BigDecimal(i.getId().add(i.getInsid())),2,RoundingMode.CEILING).multiply(new BigDecimal(100)));//焊接效率
				json.put("t13", machineStr);
				ary.add(json);
//				json.put("t0", i.getName());//所属班组
//				json.put("t1", i.getTotal());//设备总数
//				int machinenum = 0;
//				BigInteger starttime = null;
//				DataStatistics weldtime = null;
//				DataStatistics junction = dss.getWorkJunctionNum(i.getId(), dto);//获取工作(焊接)的焊口数
//				DataStatistics parameter = dss.getParameter();//获取参数
//				BigInteger standytime = null;
//				machinenum = dss.getStartingUpMachineNum(i.getId(),dto);//获取开机焊机总数
//				starttime = dss.getStaringUpTime(i.getId(), dto);//获取开机总时长
//				standytime = dss.getStandytime(i.getId(), dto);//获取待机总时长
//				weldtime = dss.getWorkTimeAndEleVol(i.getId(),dto);//获取焊接时长，平均电流电压
//				DataStatistics machine = dss.getWorkMachineNum(i.getId(), dto);//获取工作(焊接)的焊机数
//				if(!("").equals(starttime) && starttime!=null){
//					json.put("t8", getTimeStrBySecond(starttime));//工作时间
//				}else{
//					json.put("t8", "00:00:00");
//				}
//				json.put("t2", machinenum);//开机设备数
//				if(machine!=null){
//					json.put("t3",machine.getMachinenum() );//实焊设备数
//				}else{
//					json.put("t3",0);//实焊设备数
//				}
//				double standytimes = 0,time=0,electric=0;
//				if(standytime!=null){
//					standytimes = standytime.doubleValue()/60/60;
//				}
//				if(weldtime!=null){
//					electric = (double)Math.round((weldtime.getWorktime().doubleValue()/60/60*(weldtime.getElectricity()*weldtime.getVoltage())/1000+standytimes*parameter.getStandbypower()/1000)*100)/100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
//					json.put("t7", getTimeStrBySecond(weldtime.getWorktime()));//焊接时间
//					double weldingproductivity = (double)Math.round(weldtime.getWorktime().doubleValue()/starttime.doubleValue()*100*100)/100;
//					json.put("t9", weldingproductivity);//焊接效率
//				}else{
//					electric = (double)Math.round((time+standytimes*parameter.getStandbypower()/1000)*100)/100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
//					json.put("t7", "00:00:00");//焊接时间
//					json.put("t9", 0);//焊接效率
//				}
//				json.put("t11", electric);//电能消耗
//				if(junction.getJunctionnum()!=0){
//					json.put("t6", junction.getJunctionnum());//焊接焊缝数
//				}else{
//					json.put("t6", 0);
//				}
//				if(i.getTotal()!=0 && weldtime!=null){
//					if(machine!=null && junction!=null){
//						String machineStr = "";
//						int macNum = 0;
//						for(DataStatistics m:mNoTask){
//							if(!("").equals(m.getId()) && m.getId()!=null){
//								if(i.getId().toString().equals(m.getId().toString())){
//									machineStr = machineStr + m.getName()+"~";
//									macNum++;
//								}
//							}
//						}
//						json.put("t4", macNum);
//						json.put("t13", machineStr);
//						double useratio =(double)Math.round(Double.valueOf(machinenum)/Double.valueOf(i.getTotal())*100*100)/100;
//						json.put("t5", useratio);//设备利用率
//					}
//					if(parameter!=null){
//						time = weldtime.getWorktime().doubleValue()/60;
//						String[] str = parameter.getWireweight().split(",");
//						double wireweight =Double.valueOf(str[0]);
//						double wire = (double)Math.round(wireweight*parameter.getSpeed()*time*100)/100;//焊丝消耗量=焊丝|焊丝重量*送丝速度*焊接时间
//						double air = (double)Math.round(parameter.getAirflow()*time*100)/100;//气体消耗量=气体流量*焊接时间
//						json.put("t10", wire);//焊丝消耗
//						json.put("t12", air);//气体消耗
//					}
//				}else{
//					String machineStr = "";
//					int macNum = 0;
//					for(DataStatistics m:mNoTask){
//						if(!("").equals(m.getId()) && m.getId()!=null){
//							if(i.getId().toString().equals(m.getId().toString())){
//								machineStr += m.getName()+"~";
//								macNum++;
//							}
//						}
//					}
//					json.put("t4", macNum);
//					json.put("t13", machineStr);
//					json.put("t5", 0);//设备利用率
//					json.put("t2", 0);//开机设备数
//					json.put("t10", 0);//焊丝消耗
//					json.put("t12", 0);//气体消耗
//				}
//				ary.add(json);
			}
			//表头
//			String [] str = {"所属班组","设备总数","开机设备数","实焊设备数","未绑定设备数","设备利用率(%)","焊接任务数","焊接时间","工作时间","焊接效率(%)","焊丝消耗(KG)","电能消耗(KWH)","气体消耗(L)","未绑定设备明细"};
//			for(int i=0;i<str.length;i++){
//				title.put("title", str[i]);
//				titleary.add(title);
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
//		obj.put("ary", titleary);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	
	/**
	 * 跳转班组焊接数据报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getWeldItemData")
	@ResponseBody
	public String getWeldItemProductionData(HttpServletRequest request){
		if(iutil.isNull(request.getParameter("page"))){
			pageIndex = Integer.parseInt(request.getParameter("page"));
		}
		if(iutil.isNull(request.getParameter("rows"))){
			pageSize = Integer.parseInt(request.getParameter("rows"));
		}
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		page = new Page(pageIndex,pageSize,total);
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject title = new JSONObject();
		WeldDto dto = new WeldDto();
		JSONArray titleary = new JSONArray();
		long total = 0;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			dto.setParent(im.getUserInsframework());
			List<DataStatistics> ilist = dss.getWeldItemInCount(page,dto);
//			List<DataStatistics> olist = dss.getWeldItemOutCount(page,dto);
			
			if(ilist != null){
				PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(ilist);
				total = pageinfo.getTotal();
			}
			for(DataStatistics i:ilist){
/*				for(DataStatistics o:olist){
					if(i.getName().equals(o.getName())){*/
						json.put("t0", i.getName());//所属班组
						json.put("t1", getTimeStrBySecond(i.getInsid()));//累计焊接时间
						json.put("t2", getTimeStrBySecond(i.getInsid().subtract(i.getWorktime())));//正常焊接时长
						json.put("t3", getTimeStrBySecond(i.getWorktime()));//超规范焊接时长
						if(Integer.valueOf(i.getInsid().toString())+Integer.valueOf(i.getWorktime().toString())!=0){
							json.put("t4", new DecimalFormat("0.00").format((float)Integer.valueOf(i.getInsid().subtract(i.getWorktime()).toString())/(Integer.valueOf(i.getInsid().toString()))*100));//规范符合率
						}else{
							json.put("t4",0);
						}
						ary.add(json);
/*					}
				}*/
			}
			//表头
			String [] str = {"所属班组","累计焊接时间","正常段时长","超规范时长","规范符合率(%)"};
			for(int i=0;i<str.length;i++){
				title.put("title", str[i]);
				titleary.add(title);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("ary", titleary);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	
	/**
	 * 跳转设备生产数据报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMachineData")
	@ResponseBody
	public String getMachineProductionData(HttpServletRequest request){
		if(iutil.isNull(request.getParameter("page"))){
			pageIndex = Integer.parseInt(request.getParameter("page"));
		}
		if(iutil.isNull(request.getParameter("rows"))){
			pageSize = Integer.parseInt(request.getParameter("rows"));
		}
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String item = request.getParameter("item");
		page = new Page(pageIndex,pageSize,total);
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject title = new JSONObject();
		WeldDto dto = new WeldDto();
		JSONArray titleary = new JSONArray();
		BigInteger itemid = null;
		long total = 0;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			if(iutil.isNull(item)){
				itemid = new BigInteger(item);
			}else{
				itemid = im.getUserInsframework();
			}
//			List<DataStatistics> list = dss.getAllMachine(page,itemid);
			List<DataStatistics> list = dss.getMachineData(page,im.getUserInsframework(),dto);
			if(list != null){
				PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(list);
				total = pageinfo.getTotal();
			}
			for(DataStatistics i:list){
				json.put("t1", i.getMachineno());
				json.put("t0", i.getName());
				json.put("t2", i.getTaskid());//焊接焊缝数
				json.put("t3", getTimeStrBySecond(i.getId()));//焊接时间
				json.put("t4", getTimeStrBySecond(i.getId().add(i.getInsid())));//工作时间
				json.put("t5", new BigDecimal(i.getId()).divide(new BigDecimal(i.getId().add(i.getInsid())),2,RoundingMode.CEILING).multiply(new BigDecimal(100)));//焊接效率
//				dto.setMachineid(i.getId());
//				json.put("t0", i.getInsname());
//				json.put("t1", i.getName());
//				DataStatistics junctionnum = dss.getWorkJunctionNum(null, dto);
//				DataStatistics parameter = dss.getParameter();
//				BigInteger worktime = null,standytime=null;
//				DataStatistics weld = null;
//				if(junctionnum.getJunctionnum()!=0){
//					json.put("t2", junctionnum.getJunctionnum());//焊接焊缝数
//					worktime = dss.getStaringUpTime(null, dto);
//					json.put("t4", getTimeStrBySecond(worktime));//工作时间
//					standytime = dss.getStandytime(i.getInsid(), dto);//获取待机总时长
//					weld = dss.getWorkTimeAndEleVol(i.getInsid(), dto);
//					double standytimes = 0,time=0,electric=0;
//					if(standytime!=null){
//						standytimes = standytime.doubleValue()/60/60;
//					}
//					if(weld!=null){
//						electric = (double)Math.round((weld.getWorktime().doubleValue()/60/60*(weld.getElectricity()*weld.getVoltage())/1000+standytimes*parameter.getStandbypower()/1000)*100)/100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
//					}else{
//						electric = (double)Math.round((time+standytimes*parameter.getStandbypower()/1000)*100)/100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
//					}
//					json.put("t7", electric);//电能消耗
//				}else{
//					json.put("t2", 0);
//					json.put("t4", "00:00:00");
//					json.put("t7", 0);
//				}
//				if(weld!=null){
//					json.put("t3", getTimeStrBySecond(weld.getWorktime()));//焊接时间
//					json.put("t4", getTimeStrBySecond(worktime));//工作时间
//					double weldingproductivity = (double)Math.round(weld.getWorktime().doubleValue()/worktime.doubleValue()*100*100)/100;
//					json.put("t5", weldingproductivity);//焊接效率
//					if(parameter!=null){
//						double  time = weld.getWorktime().doubleValue()/60;
//						String[] str = parameter.getWireweight().split(",");
//						double wireweight =Double.valueOf(str[0]);
//						double wire = (double)Math.round(wireweight*parameter.getSpeed()*time*100)/100;//焊丝消耗量=焊丝|焊丝重量*送丝速度*焊接时间
//						double air = (double)Math.round(parameter.getAirflow()*time*100)/100;//气体消耗量=气体流量*焊接时间
//						json.put("t6", wire);//焊丝消耗
//						json.put("t8", air);//气体消耗
//					}
//				}else{
//					json.put("t3", "00:00:00");
//					json.put("t5", 0);
//					json.put("t6", 0);
//					json.put("t8", 0);
//				}
				ary.add(json);
			}
			//表头
//			String [] str = {"所属班组","设备编号","焊接任务数","焊接时间","工作时间","焊接效率(%)","焊丝消耗(KG)","电能消耗(KWH)","气体消耗(L)"};
//			for(int i=0;i<str.length;i++){
//				title.put("title", str[i]);
//				titleary.add(title);
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
//		obj.put("ary", titleary);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	/**
	 * 跳转设备焊接数据报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getWeldMachineData")
	@ResponseBody
	public String getWeldMachineProductionData(HttpServletRequest request){
		if(iutil.isNull(request.getParameter("page"))){
			pageIndex = Integer.parseInt(request.getParameter("page"));
		}
		if(iutil.isNull(request.getParameter("rows"))){
			pageSize = Integer.parseInt(request.getParameter("rows"));
		}
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String item = request.getParameter("item");
		page = new Page(pageIndex,pageSize,total);
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject title = new JSONObject();
		WeldDto dto = new WeldDto();
		JSONArray titleary = new JSONArray();
		BigInteger itemid = null;
		long total = 0;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			if(iutil.isNull(item)){
				itemid = new BigInteger(item);
			}else{
				itemid = im.getUserInsframework();
			}
			List<DataStatistics> ilist = dss.getWeldMachineInCount(page,dto,itemid);
//			List<DataStatistics> olist = dss.getWeldMachineOutCount(page,dto,itemid);
			
			if(ilist != null){
				PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(ilist);
				total = pageinfo.getTotal();
			}
			for(DataStatistics i:ilist){
/*				for(DataStatistics o:olist){
					if(i.getName().equals(o.getName())){*/					
						json.put("t0", i.getName());//焊机编号
						json.put("t1", i.getInsname());//所属班组	
						json.put("t2", getTimeStrBySecond(i.getInsid()));//累计焊接时间
						json.put("t3", getTimeStrBySecond(i.getInsid().subtract(i.getWorktime())));//正常焊接时长
						json.put("t4", getTimeStrBySecond(i.getWorktime()));//超规范焊接时长
						if(Integer.valueOf(i.getInsid().toString())+Integer.valueOf(i.getWorktime().toString())!=0){
							json.put("t5", new DecimalFormat("0.00").format((float)Integer.valueOf(i.getInsid().subtract(i.getWorktime()).toString())/(Integer.valueOf(i.getInsid().toString()))*100));//规范符合率
						}else{
							json.put("t5",0);
						}
						ary.add(json);
/*					}
				}*/
			}
			//表头
			String [] str = {"所属班组","设备编码","累计焊接时间","正常段时长","超规范时长","规范符合率(%)"};
			for(int i=0;i<str.length;i++){
				title.put("title", str[i]);
				titleary.add(title);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("ary", titleary);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	/**
	 * 跳转人员生产数据报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getPersonData")
	@ResponseBody
	public String getPersonProductionData(HttpServletRequest request){
		if(iutil.isNull(request.getParameter("page"))){
			pageIndex = Integer.parseInt(request.getParameter("page"));
		}
		if(iutil.isNull(request.getParameter("rows"))){
			pageSize = Integer.parseInt(request.getParameter("rows"));
		}
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		page = new Page(pageIndex,pageSize,total);
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject title = new JSONObject();
		WeldDto dto = new WeldDto();
		JSONArray titleary = new JSONArray();
		long total = 0;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
//			List<DataStatistics> list = dss.getAllWelder(page,im.getUserInsframework());
			List<DataStatistics> list = dss.getWelderData(page,im.getUserInsframework(),dto);
			if(list != null){
				PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(list);
				total = pageinfo.getTotal();
			}
			for(DataStatistics i:list){
				json.put("t0", i.getWelderno());
				json.put("t1", i.getName());
				json.put("t2", i.getTaskid());//焊接焊缝数
				json.put("t3", getTimeStrBySecond(i.getId()));//焊接时间
				json.put("t4", getTimeStrBySecond(i.getId().add(i.getInsid())));//工作时间
				json.put("t5", new BigDecimal(i.getId()).divide(new BigDecimal(i.getId().add(i.getInsid())),2,RoundingMode.CEILING).multiply(new BigDecimal(100)));//焊接效率
//				dto.setWelderno(i.getSerialnumber());
//				json.put("t0", i.getSerialnumber());
//				json.put("t1", i.getName());
//				DataStatistics weld = null;
//				BigInteger worktime = null,standytime=null;
//				DataStatistics junctionnum = dss.getWorkJunctionNumByWelder(null, dto);
//				DataStatistics parameter = dss.getParameter();
//				if(junctionnum.getJunctionnum()!=0){
//					json.put("t2", junctionnum.getJunctionnum());//焊接焊缝数
//					worktime = dss.getStaringUpTimeByWelder(null, dto);
//					json.put("t4", getTimeStrBySecond(worktime));//工作时间
//					standytime = dss.getStandytimeByWelder(null, dto);
//					weld = dss.getWorkTimeAndEleVolByWelder(null, dto);
//					double standytimes = 0,time=0,electric=0;
//					if(standytime!=null){
//						standytimes = standytime.doubleValue()/60/60;
//					}
//					if(weld!=null){
//						time = weld.getWorktime().doubleValue()/60/60;
//						electric = (double)Math.round((time*(weld.getElectricity()*weld.getVoltage())/1000+standytimes*parameter.getStandbypower()/1000)*100)/100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
//					}else{
//						electric = (double)Math.round((time+standytimes*parameter.getStandbypower()/1000)*100)/100;
//					}
//					json.put("t7", electric);//电能消耗
//				}else{
//					json.put("t2", 0);
//					json.put("t4", "00:00:00");//工作时间
//					json.put("t7", 0);
//				}
//				if(weld!=null){
//					json.put("t3", getTimeStrBySecond(weld.getWorktime()));//焊接时间
//					double weldingproductivity = (double)Math.round(weld.getWorktime().doubleValue()/worktime.doubleValue()*100*100)/100;
//					json.put("t5", weldingproductivity);//焊接效率
//					if(parameter!=null){
//						double  time = weld.getWorktime().doubleValue()/60;
//						String[] str = parameter.getWireweight().split(",");
//						double wireweight =Double.valueOf(str[0]);
//						double wire = (double)Math.round(wireweight*parameter.getSpeed()*time*100)/100;//焊丝消耗量=焊丝|焊丝重量*送丝速度*焊接时间
//						double air = (double)Math.round(parameter.getAirflow()*time*100)/100;//气体消耗量=气体流量*焊接时间
//						if(String.valueOf(weld.getWorktime()).equals("0")){
//							json.put("t9", 0);//规范符合率
//						}else{
//							String sperate = new DecimalFormat("0.00").format((float)Integer.valueOf(weld.getWorktime().subtract(new BigInteger(weld.getTime())).toString())/(Integer.valueOf(weld.getWorktime().toString()))*100);
//							json.put("t9", sperate);//规范符合率
//						}
//						json.put("t6", wire);//焊丝消耗
//						json.put("t8", air);//气体消耗
//					}
//				}else{
//					json.put("t3", "00:00:00");
//					json.put("t5", 0);
//					json.put("t6", 0);
//					json.put("t8", 0);
//					json.put("t9", 0);//规范符合率
//				}
				ary.add(json);
			}
			//表头
//			String [] str = {"焊工编号","焊工名称","焊接任务数","焊接时间","工作时间","焊接效率(%)","焊丝消耗(KG)","电能消耗(KWH)","气体消耗(L)","规范符合率(%)"};
//			for(int i=0;i<str.length;i++){
//				title.put("title", str[i]);
//				titleary.add(title);
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
//		obj.put("ary", titleary);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	/**
	 * 跳转人员焊接数据报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getWeldPersonData")
	@ResponseBody
	public String getWeldPersonProductionData(HttpServletRequest request){
		if(iutil.isNull(request.getParameter("page"))){
			pageIndex = Integer.parseInt(request.getParameter("page"));
		}
		if(iutil.isNull(request.getParameter("rows"))){
			pageSize = Integer.parseInt(request.getParameter("rows"));
		}
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		page = new Page(pageIndex,pageSize,total);
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject title = new JSONObject();
		WeldDto dto = new WeldDto();
		JSONArray titleary = new JSONArray();
		long total = 0;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			dto.setParent(im.getUserInsframework());
			List<DataStatistics> ilist = dss.getWeldPersonInCount(page,dto);
//			List<DataStatistics> olist = dss.getWeldPersonOutCount(page,dto);
			
			if(ilist != null){
				PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(ilist);
				total = pageinfo.getTotal();
			}
			for(DataStatistics i:ilist){
//				for(DataStatistics o:olist){
//					if((i.getInsname()).equals(o.getInsname())){
						json.put("t0", i.getInsname());//焊工编号
						json.put("t1", i.getName());//焊工 姓名
						json.put("t2", getTimeStrBySecond(i.getInsid()));//累计焊接时间
						json.put("t3", getTimeStrBySecond(i.getInsid().subtract(i.getWorktime())));//正常焊接时长
						json.put("t4", getTimeStrBySecond(i.getWorktime()));//超规范焊接时长
						if(Integer.valueOf(i.getInsid().toString())+Integer.valueOf(i.getWorktime().toString())!=0){
							json.put("t5", new DecimalFormat("0.00").format((float)Integer.valueOf(i.getInsid().subtract(i.getWorktime()).toString())/(Integer.valueOf(i.getInsid().toString()))*100));//规范符合率
						}else{
							json.put("t5",0);
						}
						ary.add(json);
//					}
//				}
			}
			//表头
			String [] str = {"焊工编号","焊工姓名","累计焊接时间","正常段时长","超规范时长","规范符合率(%)"};
			for(int i=0;i<str.length;i++){
				title.put("title", str[i]);
				titleary.add(title);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("ary", titleary);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	/**
	 * 跳转工件生产数据报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getWorkpieceData")
	@ResponseBody
	public String getWorkpieceProductionData(HttpServletRequest request){
		if(iutil.isNull(request.getParameter("page"))){
			pageIndex = Integer.parseInt(request.getParameter("page"));
		}
		if(iutil.isNull(request.getParameter("rows"))){
			pageSize = Integer.parseInt(request.getParameter("rows"));
		}
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String junctionno = request.getParameter("junctionno");
		page = new Page(pageIndex,pageSize,total);
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject title = new JSONObject();
		WeldDto dto = new WeldDto();
		JSONArray titleary = new JSONArray();
		long total = 0;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
//			List<DataStatistics> list = dss.getAllJunction(page,"%"+ junctionno+"%");
			List<DataStatistics> list = dss.getJunctionData(page,im.getUserInsframework(),dto);
			if(list != null){
				PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(list);
				total = pageinfo.getTotal();
			}
			for(DataStatistics i:list){
				json.put("t0", i.getTaskno());
				json.put("t1", getTimeStrBySecond(i.getId()));//焊接时间
				json.put("t2", getTimeStrBySecond(i.getId().add(i.getInsid())));//工作时间
				json.put("t3", new BigDecimal(i.getId()).divide(new BigDecimal(i.getId().add(i.getInsid())),2,RoundingMode.CEILING).multiply(new BigDecimal(100)));//焊接效率
//				dto.setJunctionno(i.getSerialnumber());
//				json.put("t0", i.getSerialnumber());
//				BigInteger worktime = dss.getStaringUpTimeByJunction(null, dto);
//				DataStatistics parameter = dss.getParameter();
//				BigInteger standytime = null;
//				DataStatistics weld = null;
//				if(worktime!=null){
//					json.put("t2", getTimeStrBySecond(worktime));//工作时间
//					weld = dss.getWorkTimeAndEleVolByJunction(null, dto);
//					standytime = dss.getStandytimeByJunction(null, dto);
//
//					double standytimes = 0,time=0,electric=0;
//					if(standytime!=null){
//						standytimes = standytime.doubleValue()/60/60;
//					}
//					if(weld!=null){
//						time = weld.getWorktime().doubleValue()/60/60;
//						electric = (double)Math.round((time*(weld.getElectricity()*weld.getVoltage())/1000+standytimes*parameter.getStandbypower()/1000)*100)/100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
//						
//					}else{
//						electric = (double)Math.round((time+standytimes*parameter.getStandbypower()/1000)*100)/100;
//					}
//					json.put("t5", electric);//电能消耗
//				}else{
//					json.put("t2", "00:00:00");
//					json.put("t5", 0);
//				}
//				if(worktime!=null && weld!=null){
//					json.put("t1", getTimeStrBySecond(weld.getWorktime()));//焊接时间
//					double weldingproductivity = (double)Math.round(weld.getWorktime().doubleValue()/worktime.doubleValue()*100*100)/100;
//					json.put("t3", weldingproductivity);//焊接效率
//					if(parameter!=null){
//						double  time = weld.getWorktime().doubleValue()/60;
//						String[] str = parameter.getWireweight().split(",");
//						double wireweight =Double.valueOf(str[0]);
//						double wire = (double)Math.round(wireweight*parameter.getSpeed()*time*100)/100;//焊丝消耗量=焊丝|焊丝重量*送丝速度*焊接时间
//						double air = (double)Math.round(parameter.getAirflow()*time*100)/100;//气体消耗量=气体流量*焊接时间
//						if(String.valueOf(weld.getWorktime()).equals("0")){
//							json.put("t7", 0);
//						}else{
//							String sperate = new DecimalFormat("0.00").format((float)Integer.valueOf(weld.getWorktime().subtract(new BigInteger(weld.getTime())).toString())/(Integer.valueOf(weld.getWorktime().toString()))*100);
//							json.put("t7", sperate);
//						}
//						json.put("t4", wire);//焊丝消耗
//						json.put("t6", air);//气体消耗
//						
//					}
//				}else{
//					json.put("t1", "00:00:00");
//					json.put("t3", 0);
//					json.put("t4", 0);
//					json.put("t6", 0);
//					json.put("t7", 0);
//				}
				ary.add(json);
			}
			//表头
//			String [] str = {"焊缝编号","焊接时间","工作时间","焊接效率(%)","焊丝消耗(KG)","电能消耗(KWH)","气体消耗(L)","规范符合率(%)"};
//			for(int i=0;i<str.length;i++){
//				title.put("title", str[i]);
//				titleary.add(title);
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
//		obj.put("ary", titleary);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	/**
	 * 跳转工件焊接数据报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getWeldWorkpieceData")
	@ResponseBody
	public String getWeldWorkpieceProductionData(HttpServletRequest request){
		if(iutil.isNull(request.getParameter("page"))){
			pageIndex = Integer.parseInt(request.getParameter("page"));
		}
		if(iutil.isNull(request.getParameter("rows"))){
			pageSize = Integer.parseInt(request.getParameter("rows"));
		}
		String junctionno = request.getParameter("junctionno");
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		page = new Page(pageIndex,pageSize,total);
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject title = new JSONObject();
		WeldDto dto = new WeldDto();
		JSONArray titleary = new JSONArray();
		long total = 0;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			dto.setParent(im.getUserInsframework());
			List<DataStatistics> ilist = dss.getWeldPieceInCount(page,dto,"%"+ junctionno+"%");
//			List<DataStatistics> olist = dss.getWeldPieceOutCount(page,dto,"%"+ junctionno+"%");
			
			if(ilist != null){
				PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(ilist);
				total = pageinfo.getTotal();
			}
			for(DataStatistics i:ilist){
//				for(DataStatistics o:olist){
//					if((i.getInsname()).equals(o.getInsname())){
						json.put("t0", i.getInsname());//工件编号
						json.put("t1", getTimeStrBySecond(i.getInsid()));//累计焊接时间
						json.put("t2", getTimeStrBySecond(i.getInsid().subtract(i.getWorktime())));//正常焊接时长
						json.put("t3", getTimeStrBySecond(i.getWorktime()));//超规范焊接时长
						if(Integer.valueOf(i.getInsid().toString())+Integer.valueOf(i.getWorktime().toString())!=0){
							json.put("t4", new DecimalFormat("0.00").format((float)Integer.valueOf(i.getInsid().subtract(i.getWorktime()).toString())/(Integer.valueOf(i.getInsid().toString()))*100));//规范符合率
						}else{
							json.put("t4",0);
						}
						ary.add(json);
//					}
//				}
			}
			//表头
			String [] str = {"焊缝编号","累计焊接时间","正常段时长","超规范时长","规范符合率(%)"};
			for(int i=0;i<str.length;i++){
				title.put("title", str[i]);
				titleary.add(title);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("ary", titleary);
		obj.put("rows", ary);
		return obj.toString();
	}

	
	/**
	 * 跳转故障报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getFauit")
	@ResponseBody
	public String getFauit(HttpServletRequest request){
		if(iutil.isNull(request.getParameter("page"))){
			pageIndex = Integer.parseInt(request.getParameter("page"));
		}
		if(iutil.isNull(request.getParameter("rows"))){
			pageSize = Integer.parseInt(request.getParameter("rows"));
		}
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		int fauit = 0 ;
		if(iutil.isNull(request.getParameter("fauit"))){
			fauit  = Integer.parseInt(request.getParameter("fauit"));
		}
		page = new Page(pageIndex,pageSize,total);
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject title = new JSONObject();
		WeldDto dto = new WeldDto();
		JSONArray titleary = new JSONArray();
		long total = 0;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			dto.setParent(im.getUserInsframework());
			List<DataStatistics> list = dss.getFauit(page, dto, fauit);
			if(list != null){
				PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(list);
				total = pageinfo.getTotal();
			}
			for(DataStatistics i:list){
				json.put("t0", i.getId());
				json.put("t1", i.getName());
				json.put("t2", i.getInsname());
				json.put("t3", i.getValuename());
				json.put("t4", i.getNum());
				ary.add(json);
			}
			//表头
			String [] str = {"序号","焊机编号","焊机归属","故障类型","故障次数"};
			for(int i=0;i<str.length;i++){
				title.put("title", str[i]);
				titleary.add(title);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("ary", titleary);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	/**
	 * 跳转故障明细报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getFauitDeatil")
	@ResponseBody
	public String getFauitDeatil(HttpServletRequest request){
		if(iutil.isNull(request.getParameter("page"))){
			pageIndex = Integer.parseInt(request.getParameter("page"));
		}
		if(iutil.isNull(request.getParameter("rows"))){
			pageSize = Integer.parseInt(request.getParameter("rows"));
		}
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		BigInteger id = null;
		int fauit = 0 ;
		if(iutil.isNull(request.getParameter("fauit"))){
			fauit  = Integer.parseInt(request.getParameter("fauit"));
		}
		if(iutil.isNull(request.getParameter("id"))){
			id  = new BigInteger(request.getParameter("id"));
		}
		page = new Page(pageIndex,pageSize,total);
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject title = new JSONObject();
		WeldDto dto = new WeldDto();
		JSONArray titleary = new JSONArray();
		long total = 0;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			dto.setParent(im.getUserInsframework());
			List<DataStatistics> list = dss.getFauitDetail(page, dto, id, fauit);
			if(list != null){
				PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(list);
				total = pageinfo.getTotal();
			}
			for(DataStatistics i:list){
				json.put("t0", i.getName());
				json.put("t1", i.getInsname());
				json.put("t2", i.getValuename());
				json.put("t3", i.getTime());
				ary.add(json);
			}
			//表头
			String [] str = {"焊机编号","焊机归属","故障类型","故障发生时间"};
			for(int i=0;i<str.length;i++){
				title.put("title", str[i]);
				titleary.add(title);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("ary", titleary);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	/**
	 * 获取组织机构
	 * @return
	 */
	@RequestMapping("/getAllInsframework")
	@ResponseBody
	public String getAllInsframework(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			List<DataStatistics> list = dss.getAllInsframe(im.getUserInsframework());
			json.put("id", 0);
			json.put("name", "全部");
			ary.add(json);
			for(DataStatistics i:list){
				json.put("id", i.getId());
				json.put("name", i.getName());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	/**
	 * 获取故障类型
	 * @return
	 */
	@RequestMapping("/getAllFauit")
	@ResponseBody
	public String getAllFauit(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			List<Dictionarys> dictionary = dm.getDictionaryValue(15);
			json.put("id", 0);
			json.put("name", "全部");
			ary.add(json);
			for(Dictionarys d:dictionary){
				json.put("id", d.getValue());
				json.put("name", d.getValueName());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	public String getTimeStrBySecond(BigInteger timeParam ) {
		if(timeParam == null){
			return "00:00:00";
		}
		BigInteger[] str = timeParam.divideAndRemainder(new BigInteger("60"));//divideAndRemainder返回数组。第一个是商第二个时取模
		BigInteger second = str[1];
		BigInteger minuteTemp = timeParam.divide(new BigInteger("60"));//subtract：BigInteger相减，multiply：BigInteger相乘，divide : BigInteger相除
        if (minuteTemp.compareTo(new BigInteger("0"))>0) {//compareTo：比较BigInteger类型的大小，大则返回1，小则返回-1 ，等于则返回0
        	BigInteger[] minstr = minuteTemp.divideAndRemainder(new BigInteger("60"));
    		BigInteger minute = minstr[1];
    		BigInteger hour = minuteTemp.divide(new BigInteger("60"));
            if (hour.compareTo(new BigInteger("0"))>0) {
                return (hour.compareTo(new BigInteger("9"))>0 ? (hour + "") : ("0" + hour)) + ":" + (minute.compareTo(new BigInteger("9"))>0 ? (minute + "") : ("0" + minute))
                        + ":" + (second .compareTo(new BigInteger("9"))>0 ? (second + "") : ("0" + second));
            } else {
                return "00:" + (minute.compareTo(new BigInteger("9"))>0 ? (minute + "") : ("0" + minute)) + ":"
                        + (second .compareTo(new BigInteger("9"))>0 ? (second + "") : ("0" + second));
            }
        } else {
            return "00:00:" + (second .compareTo(new BigInteger("9"))>0 ? (second + "") : ("0" + second));
        }
	}
	

	@RequestMapping("getWorkRank")
	@ResponseBody
	public String getWorkRank(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		try{ 
			String parentid = request.getParameter("parent");
			BigInteger parent = null;
			if(iutil.isNull(parentid)){
				parent = new BigInteger(parentid);
			}
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<DataStatistics> list = dss.getWorkRank(parent, sdf.format(date));
			for(int i=0;i<list.size();i++){
				json.put("rownum", i+1);
				json.put("welderno", list.get(i).getWelderno());
				json.put("name", list.get(i).getName());
				json.put("item", list.get(i).getInsname());
				json.put("hour", (double)Math.round(list.get(i).getHour()*100)/100);
				ary.add(json);
			}
			obj.put("rows", ary);
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	/**
	 * 跳转班组生产数据报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getUseRatio")
	@ResponseBody
	public String getUseRatio(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		try{
			String parentid = request.getParameter("parent");
			BigInteger parent = null;
			if(iutil.isNull(parentid)){
				parent = new BigInteger(parentid);
			}
			List<DataStatistics> list = dss.getItemMachineCount(parent);
			for(DataStatistics i:list){
				json.put("itemname", i.getName());//班组
				json.put("machinenum", i.getTotal());//设备总数
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				DataStatistics machine = dss.getWorkMachineCount(i.getId(), sdf.format(date));
				if(machine!=null){
					json.put("worknum", machine.getMachinenum());//工作设备数
					double useratio =(double)Math.round(Double.valueOf(machine.getMachinenum())/Double.valueOf(i.getTotal())*100*100)/100;
					json.put("useratio", useratio);//设备利用率
				}else{
					json.put("worknum", 0);//工作设备数
					json.put("useratio", 0);//设备利用率
				}
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	
	/**
	 * 跳转班组生产数据报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getLoadRate")
	@ResponseBody
	public String getLoadRate(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONArray timeary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject timejson = new JSONObject();
		try{
			String parentid = request.getParameter("parent");
			String time1 = request.getParameter("time1");
			String time2 = request.getParameter("time2");
			WeldDto dto = new WeldDto();
			BigInteger parent = null;
			if(iutil.isNull(parentid)){
				parent = new BigInteger(parentid);
				dto.setParent(parent);
			}
			dto.setDtoTime1(time1.substring(0, 10));
			dto.setDtoTime2(time2.substring(0, 10));
			dto.setDay("day");
			List<DataStatistics> ilist = dss.getItemWeldTime(dto);
			List<DataStatistics> olist = dss.getItemOverProofTime(dto);
			List<ModelDto> time =  ls.getAllTimes(dto);
			List<LiveData> insf = ls.getAllInsf(parent, 23);
			List<DataStatistics> temp = ilist;
			for(int i=0;i<ilist.size();i++){
				double num = 100;
				temp.get(i).setInsname(ilist.get(i).getName());
				temp.get(i).setTime(ilist.get(i).getTime());
				for(int o=0;o<olist.size();o++){
					if(ilist.get(i).getId().equals(olist.get(o).getId()) && ilist.get(i).getTime().equals(olist.get(o).getTime())){
						num = (double)Math.round(((ilist.get(i).getHour()-olist.get(o).getHour())/ilist.get(i).getHour())*100*100)/100;
					}
				}
				temp.get(i).setHour(num);
			}
			for(ModelDto t:time){
				timejson.put("weldtime", t.getWeldTime());//日期
				timeary.add(timejson);
			}
			for(LiveData item:insf){
				json.put("itemname", item.getFname());//班组
				double[] num = new double[time.size()];
				for(int i=0;i<time.size();i++){
					num[i] = 0;
					for(DataStatistics t:temp){
						if(time.get(i).getWeldTime().equals(t.getTime()) && item.getFname().equals(t.getName())){
							num[i] = t.getHour();
						}
					}
				}
				json.put("hour", num);
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("time", timeary);
		obj.put("ary", ary);
		return obj.toString();
	}
	
	/**
	 * 跳转生产任务详情报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getTaskDetail")
	@ResponseBody
	public String getTaskDetail(HttpServletRequest request){
		if(iutil.isNull(request.getParameter("page"))){
			pageIndex = Integer.parseInt(request.getParameter("page"));
		}
		if(iutil.isNull(request.getParameter("rows"))){
			pageSize = Integer.parseInt(request.getParameter("rows"));
		}
		page = new Page(pageIndex,pageSize,total);
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject title = new JSONObject();
		JSONArray titleary = new JSONArray();
		BigInteger itemid = null;
		String welderno = "",taskno = "",dtoTime1 = "",dtoTime2 = "";
		try{
			if(iutil.isNull(request.getParameter("dtoTime1"))){
				dtoTime1 = request.getParameter("dtoTime1");
			}
			if(iutil.isNull(request.getParameter("dtoTime2"))){
				dtoTime2 = request.getParameter("dtoTime2");
			}
			if(iutil.isNull(request.getParameter("welderno"))){
				welderno = "'%" + request.getParameter("welderno") + "%'";
			}
			if(iutil.isNull(request.getParameter("taskno"))){
				taskno = "'%" + request.getParameter("taskno") + "%'";
			}
			if(iutil.isNull(request.getParameter("item"))){
				itemid = new BigInteger(request.getParameter("item"));
			}else{
				itemid = im.getUserInsframework();
			}
			List<DataStatistics> list = dss.getTask(itemid, welderno, taskno, dtoTime1, dtoTime2);
			List<DataStatistics> task = dss.getTaskDetail(itemid, welderno, taskno, dtoTime1, dtoTime2);
			for(int i=0;i<list.size();i++){
				if(i!=list.size()-1){
					if(list.get(i).getTaskid().equals(list.get(i+1).getTaskid())){
						if(list.get(i).getType()!=1){
							if(list.get(i+1).getType()==1){
								list.get(i).setEndtime(list.get(i+1).getEndtime());
							}else{
								list.get(i).setEndtime(list.get(i+1).getStarttime());
							}
						}
					}
				}
				if(list.get(i).getType()!=1){
					json.put("t0", list.get(i).getName());
					json.put("t1", list.get(i).getWelderno());
					json.put("t2", list.get(i).getWeldername());
					json.put("t3", list.get(i).getMachineno());
					json.put("t4", list.get(i).getTaskno());
					json.put("t5", list.get(i).getStarttime());
					json.put("t6", list.get(i).getEndtime());
					int t7 = 0;
					String t8 = "00:00:00",t9 = "00:00:00";
					double t10 = 0,t11 = 0,t12 = 0;
					for(int j=0;j<task.size();j++){
						if(list.get(i).getType()!=1){
							if(list.get(i).getTaskid().equals(task.get(j).getTaskid()) && list.get(i).getWelderid().equals(task.get(j).getWelderid()) && list.get(i).getMachineid().equals(task.get(j).getMachineid())){
								t7 = task.get(j).getChannel();
								if(task.get(j).getWorktime()!=null && !"".equals(task.get(j).getWorktime())){
									t8 = getTimeStrBySecond(task.get(j).getWorktime());
								}
								if(task.get(j).getWarntime()!=null && !"".equals(task.get(j).getWarntime())){
									t9 = getTimeStrBySecond(task.get(j).getWarntime());
								}
								t10 = (double)Math.round(task.get(j).getElectricity()*100)/100;
								t11 = (double)Math.round(task.get(j).getVoltage()*100)/100;
								if(task.get(j).getWorktime()!=null && !"".equals(task.get(j).getWorktime())){
									t12 = (double)Math.round(task.get(j).getWorktime().doubleValue()/(task.get(j).getWorktime().doubleValue()+task.get(j).getWarntime().doubleValue())*10000)/100;
								}
							}
						}
					}
					json.put("t7", t7);
					json.put("t8", t8);
					json.put("t9", t9);
					json.put("t10", t10);
					json.put("t11", t11);
					json.put("t12", t12);
					ary.add(json);
				}
			}
			//表头
			String [] str = {"焊工班组","焊工编号","焊工姓名","焊机编号","任务编号","开始时间","结束时间","使用通道","良好段","报警段","平均焊接电流","平均焊接电压","规范符合率(%)"};
			for(int i=0;i<str.length;i++){
				title.put("title", str[i]);
				titleary.add(title);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", ary.size());
		obj.put("ary", titleary);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	/**
	 * 跳转焊机任务报表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMachineTask")
	@ResponseBody
	public String getMachineTask(HttpServletRequest request){
		if(iutil.isNull(request.getParameter("page"))){
			pageIndex = Integer.parseInt(request.getParameter("page"));
		}
		if(iutil.isNull(request.getParameter("rows"))){
			pageSize = Integer.parseInt(request.getParameter("rows"));
		}
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String item = request.getParameter("item");
		int status = Integer.parseInt(request.getParameter("status"));
		page = new Page(pageIndex,pageSize,total);
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject title = new JSONObject();
		JSONArray titleary = new JSONArray();
		BigInteger itemid = null;
		long total = 0;
		try{
			if(iutil.isNull(item)){
				itemid = new BigInteger(item);
			}else{
				itemid = im.getUserInsframework();
			}
			List<DataStatistics> list = dss.getMachineTask(page, itemid, dss.getDay(time1, time2), status);
			if(list != null){
				PageInfo<DataStatistics> pageinfo = new PageInfo<DataStatistics>(list);
				total = pageinfo.getTotal();
			}
			for(int i=0;i<list.size();i++){
				json.put("t0", list.get(i).getInsname());
				json.put("t1", list.get(i).getMachineno());
				json.put("t2", list.get(i).getTime());
				json.put("t3", list.get(i).getTaskno());
				json.put("t4", list.get(i).getType()==1?"未分配":"已分配");
				ary.add(json);
			}
			//表头
			String [] str = {"所属班组","设备编号","日期","任务号","状态"};
			for(int i=0;i<str.length;i++){
				title.put("title", str[i]);
				titleary.add(title);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("total", total);
		obj.put("ary", titleary);
		obj.put("rows", ary);
		return obj.toString();
	}
}



