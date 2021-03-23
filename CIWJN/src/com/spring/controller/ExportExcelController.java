package com.spring.controller;

import java.io.File;
import java.math.BigInteger;
import java.nio.channels.GatheringByteChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.dto.WeldDto;
import com.spring.model.DataStatistics;
import com.spring.model.Gather;
import com.spring.model.Insframework;
import com.spring.model.MyUser;
import com.spring.model.Person;
import com.spring.model.WeldedJunction;
import com.spring.model.WeldingMachine;
import com.spring.model.WeldingMaintenance;
import com.spring.page.Page;
import com.spring.service.DataStatisticsService;
import com.spring.service.InsframeworkService;
import com.spring.service.MaintainService;
import com.spring.service.PersonService;
import com.spring.service.WeldedJunctionService;
import com.spring.service.WelderService;
import com.spring.service.WeldingMachineService;
import com.spring.service.GatherService;
import com.spring.util.CommonExcelUtil;
import com.spring.util.IsnullUtil;

@Controller
@RequestMapping(value = "/export", produces = { "text/json;charset=UTF-8" })
public class ExportExcelController {
	
	private static final long serialVersionUID = -4171187629012625142L;
	
	@Autowired
	private WeldingMachineService wmm;
	@Autowired
	private DataStatisticsService dss;
	@Autowired
	private MaintainService mm;
	@Autowired
	private InsframeworkService im;
	@Autowired
	private GatherService gs;
	@Autowired
	private PersonService ps;
	@Autowired
	private WeldedJunctionService wjm;
	
	private String filename;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
	IsnullUtil iutil = new IsnullUtil();
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@RequestMapping("/exporWeldingMachine")
	@ResponseBody
	public ResponseEntity<byte[]> exporWeldingMachine(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		try {
			String str=(String) request.getSession().getAttribute("searchStr");
			List<WeldingMachine> list = wmm.getWeldingMachine(str);
			String dtime = null;
			String[] titles = new String[]{"设备编码","设备类型","入厂时间","所属项目","状态","厂家","是否在网","采集序号","位置","ip地址","设备型号"};
			Object[][] data = new Object[list.size()][11];
			for(int i =0; i<list.size();i++){
				data[i][0] = list.get(i).getEquipmentNo();
				data[i][1] = list.get(i).getTypename();
				data[i][2] = list.get(i).getJoinTime();
				data[i][3] = list.get(i).getInsframeworkId().getName();
				data[i][4] = list.get(i).getStatusname();
				data[i][5] = list.get(i).getMvaluename();
				if(list.get(i).getIsnetworking()==0){
					data[i][6] = "是";
				}else{
					data[i][6] = "否";
				}
				Gather gather = list.get(i).getGatherId();
				if(gather!=null){
					data[i][7] = gather.getGatherNo();
				}else{
					data[i][7] = null;
				}
				data[i][8] = list.get(i).getPosition();
				data[i][9] = list.get(i).getIp();
				data[i][10] = list.get(i).getModelname();
			}
			filename = "焊机设备" + sdf.format(new Date()) + ".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			new CommonExcelUtil(dtime, titles, data, path, "焊机设备数据");
			
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
			
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		}catch (Exception e) {
	    	return null;
		}  finally {
			file.delete();
		}
	}
	
	@RequestMapping("/exporMaintain")
	@ResponseBody
	public ResponseEntity<byte[]> exporMaintain(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		try{
			String str=(String) request.getSession().getAttribute("searchStr");
			List<WeldingMaintenance> list = mm.getWeldingMaintenanceAll(im.getUserInsframework(),str);
			String dtime = null;
			String[] titles = new String[]{"设备编码","维修人员","维修起始时间","维修结束时间","维修类型","维修说明"};
			Object[][] data = new Object[list.size()][6];
			for(int i =0; i<list.size();i++){
				data[i][0] = list.get(i).getWelding().getEquipmentNo();
				data[i][1] = list.get(i).getMaintenance().getViceman();
				data[i][2] = list.get(i).getMaintenance().getStartTime();
				data[i][3] = list.get(i).getMaintenance().getEndTime();
				data[i][4] = list.get(i).getMaintenance().getTypename();
				data[i][5] = list.get(i).getMaintenance().getDesc();
			}
			filename = "焊机维修" + sdf.format(new Date())+".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			
			new CommonExcelUtil(dtime, titles, data, path, "焊机维修数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
		
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}
	
	/**
	 * 班组生产数据报表导出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportItemdata")
	@ResponseBody
	public ResponseEntity<byte[]> exporItemdata(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		WeldDto dto = new WeldDto();
		String dtime = "统计日期："+time1+"--"+time2;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			List<DataStatistics> list = dss.getAllItemData();
			String[] titles = new String[]{"所属班组","设备总数","开机设备数","实焊设备数","设备利用率(%)","焊接焊缝数","焊接时间","工作时间","焊接效率(%)","焊丝消耗(KG)","电能消耗(KWH)","气体消耗(L)"};
			Object[][] data = new Object[list.size()][12];
			int ii=0;
			for(DataStatistics i:list){
				if(ii<list.size()){
				data[ii][0]=i.getName();//所属班组
				data[ii][1]=i.getTotal();//设备总数
				int machinenum = 0;
				BigInteger starttime = null;
				DataStatistics weldtime = null;
				DataStatistics junction = dss.getWorkJunctionNum(i.getId(), dto);//获取工作(焊接)的焊口数
				DataStatistics parameter = dss.getParameter();//获取参数
				BigInteger standytime = null;
				if(junction.getJunctionnum()!=0){
					machinenum = dss.getStartingUpMachineNum(i.getId(),dto);//获取开机焊机总数
					starttime = dss.getStaringUpTime(i.getId(), dto);//获取开机总时长
					data[ii][2]=machinenum;//开机设备数
					data[ii][5]=junction.getJunctionnum();//焊接焊缝数
					data[ii][7]=getTimeStrBySecond(starttime);//工作时间
					standytime = dss.getStandytime(i.getId(), dto);//获取待机总时长
					weldtime = dss.getWorkTimeAndEleVol(i.getId(),dto);//获取焊接时长，平均电流电压
					double standytimes = 0,time=0,electric=0;
					if(standytime!=null){
						standytimes = standytime.doubleValue()/60/60;
					}
					if(weldtime!=null){
						electric = (double)Math.round((weldtime.getWorktime().doubleValue()/60/60*(weldtime.getElectricity()*weldtime.getVoltage())/1000+standytimes*parameter.getStandbypower()/1000)*100)/100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
					}else{
						electric = (double)Math.round((time+standytimes*parameter.getStandbypower()/1000)*100)/100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
					}
					data[ii][10]=electric;//电能消耗
				}else{
					data[ii][2]=0;
					data[ii][5]=0;
					data[ii][7]="00:00:00";
					data[ii][10]=0;
				}
				if(i.getTotal()!=0 && weldtime!=null){
					DataStatistics machine = dss.getWorkMachineNum(i.getId(), dto);//获取工作(焊接)的焊机数
					if(machine!=null && junction!=null){
						data[ii][3]=machine.getMachinenum();//实焊设备数
						data[ii][6]=getTimeStrBySecond(weldtime.getWorktime());//焊接时间
						double useratio =(double)Math.round(Double.valueOf(machinenum)/Double.valueOf(i.getTotal())*100*100)/100;
						double weldingproductivity = (double)Math.round(weldtime.getWorktime().doubleValue()/starttime.doubleValue()*100*100)/100;
						data[ii][4]=useratio;//设备利用率
						data[ii][8]=weldingproductivity;//焊接效率
					}
					if(parameter!=null){
						double  time = weldtime.getWorktime().doubleValue()/60;
						String[] str = parameter.getWireweight().split(",");
						double wireweight =Double.valueOf(str[0]);
						double wire = (double)Math.round(wireweight*parameter.getSpeed()*time*100)/100;//焊丝消耗量=焊丝|焊丝重量*送丝速度*焊接时间
						double air = (double)Math.round(parameter.getAirflow()*time*100)/100;//气体消耗量=气体流量*焊接时间
						data[ii][9]=wire;//焊丝消耗
						data[ii][11]=air;//气体消耗
					}
				}else{
					data[ii][3]=0;//实焊设备数
					data[ii][6]="00:00:00";//焊接时间
					data[ii][4]=0;//设备利用率
					data[ii][8]=0;//焊接效率
					data[ii][2]=0;//开机设备数
					data[ii][9]=0;//焊丝消耗
					data[ii][11]=0;//气体消耗
				}
				}
				ii++;
			}
			filename = "班组生产数据" + sdf.format(new Date())+".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			
			new CommonExcelUtil(dtime, titles, data, path, "班组生产数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
		
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}	
	
	/**
	 * 班组焊接数据报表导出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportItemWelddata")
	@ResponseBody
	public ResponseEntity<byte[]> exportItemWelddata(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		WeldDto dto = new WeldDto();
		String dtime = "统计日期："+time1+"--"+time2;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			List<DataStatistics> ilist = dss.getWeldItemInCountData(dto);
			String[] titles = new String[]{"所属班组","累计焊接时间","正常段时长","超规范时长","规范符合率(%)"};
			Object[][] data = new Object[ilist.size()][5];
			int ii = 0;
			for (DataStatistics i : ilist) {
				if (ii < ilist.size()) {
					data[ii][0] = i.getName();// 所属班组
					data[ii][1] = getTimeStrBySecond(i.getInsid());// 累计焊接时间
					data[ii][2] = getTimeStrBySecond(i.getInsid().subtract(i.getWorktime()));// 正常焊接时长
					data[ii][3] = getTimeStrBySecond(i.getWorktime());// 超规范焊接时长
					if (Integer.valueOf(i.getInsid().toString()) + Integer.valueOf(i.getWorktime().toString()) != 0) {
						data[ii][4] = new DecimalFormat("0.00")
								.format((float) Integer.valueOf(i.getInsid().subtract(i.getWorktime()).toString())
										/ (Integer.valueOf(i.getInsid().toString())) * 100);// 规范符合率
					} else {
						data[ii][4] = 0;
					}
				}
				ii++;
			}
			filename = "班组焊接数据" + sdf.format(new Date())+".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			
			new CommonExcelUtil(dtime, titles, data, path, "班组焊接数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
		
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}	
	
	/**
	 * 设备生产数据报表导出
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportMachineData")
	@ResponseBody
	public ResponseEntity<byte[]> exportMachineData(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String item = request.getParameter("item");
		WeldDto dto = new WeldDto();
		BigInteger itemid = null;
		String dtime = "统计日期："+time1+"--"+time2;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			if(iutil.isNull(item)){
				itemid = new BigInteger(item);
			}
			List<DataStatistics> list = dss.getAllMachineData(itemid);
			String[] titles = new String []{"所属班组","设备编号","焊接焊缝数","焊接时间","工作时间","焊接效率(%)","焊丝消耗(KG)","电能消耗(KWH)","气体消耗(L)"};
			Object[][] data = new Object[list.size()][9];
			int ii=0;
			for(DataStatistics i:list){
				if(ii<list.size()){
				dto.setMachineid(i.getId());
				data[ii][0]=i.getInsname();
				data[ii][1]=i.getName();
				DataStatistics junctionnum = dss.getWorkJunctionNum(null, dto);
				DataStatistics parameter = dss.getParameter();
				BigInteger worktime = null,standytime=null;
				DataStatistics weld = null;
				if(junctionnum.getJunctionnum()!=0){
					data[ii][2]=junctionnum.getJunctionnum();//焊接焊缝数
					worktime = dss.getStaringUpTime(i.getInsid(), dto);
					data[ii][4]=getTimeStrBySecond(worktime);//工作时间
					standytime = dss.getStandytime(i.getInsid(), dto);//获取待机总时长
					weld = dss.getWorkTimeAndEleVol(i.getInsid(), dto);
					double standytimes = 0,time=0,electric=0;
					if(standytime!=null){
						standytimes = standytime.doubleValue()/60/60;
					}
					if(weld!=null){
						electric = (double)Math.round((weld.getWorktime().doubleValue()/60/60*(weld.getElectricity()*weld.getVoltage())/1000+standytimes*parameter.getStandbypower()/1000)*100)/100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
					}else{
						electric = (double)Math.round((time+standytimes*parameter.getStandbypower()/1000)*100)/100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
					}
					data[ii][7]=electric;//电能消耗
				}else{
					data[ii][2]=0;
					data[ii][4]="00:00:00";
					data[ii][7]=0;
				}
				if(weld!=null){
					data[ii][3]=getTimeStrBySecond(weld.getWorktime());//焊接时间
					data[ii][4]=getTimeStrBySecond(worktime);//工作时间
					double weldingproductivity = (double)Math.round(weld.getWorktime().doubleValue()/worktime.doubleValue()*100*100)/100;
					data[ii][5]=weldingproductivity;//焊接效率
					if(parameter!=null){
						double  time = weld.getWorktime().doubleValue()/60;
						String[] str = parameter.getWireweight().split(",");
						double wireweight =Double.valueOf(str[0]);
						double wire = (double)Math.round(wireweight*parameter.getSpeed()*time*100)/100;//焊丝消耗量=焊丝|焊丝重量*送丝速度*焊接时间
						double air = (double)Math.round(parameter.getAirflow()*time*100)/100;//气体消耗量=气体流量*焊接时间
						data[ii][6]=wire;//焊丝消耗
						data[ii][8]=air;//气体消耗
					}
				}else{
					data[ii][3]="00:00:00";
					data[ii][5]=0;
					data[ii][6]=0;
					data[ii][8]=0;
				}
				}
				ii++;
			}
			filename = "设备生产数据" + sdf.format(new Date())+".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			
			new CommonExcelUtil(dtime, titles, data, path, "设备生产数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
		
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}	
	
	/**
	 * 设备焊接数据报表导出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportMachineWelddata")
	@ResponseBody
	public ResponseEntity<byte[]> exportMachineWelddata(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String item = request.getParameter("item");
		WeldDto dto = new WeldDto();
		BigInteger itemid = null;
		String dtime = "统计日期："+time1+"--"+time2;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			if(iutil.isNull(item)){
				itemid = new BigInteger(item);
			}
			List<DataStatistics> ilist = dss.getWeldMachineInCountData(dto,itemid);
			String[] titles = new String[]{"设备编码","所属班组","累计焊接时间","正常段时长","超规范时长","规范符合率(%)"};
			Object[][] data = new Object[ilist.size()][6];
			int ii = 0;
			for (DataStatistics i : ilist) {
				if (ii < ilist.size()) {
					data[ii][0] = i.getName();// 焊机编号
					data[ii][1] = i.getInsname();// 所属班组
					data[ii][2] = getTimeStrBySecond(i.getInsid());// 累计焊接时间
					data[ii][3] = getTimeStrBySecond(i.getInsid().subtract(i.getWorktime()));// 正常焊接时长
					data[ii][4] = getTimeStrBySecond(i.getWorktime());// 超规范焊接时长
					if (Integer.valueOf(i.getInsid().toString()) + Integer.valueOf(i.getWorktime().toString()) != 0) {
						data[ii][5] = new DecimalFormat("0.00")
								.format((float) Integer.valueOf(i.getInsid().subtract(i.getWorktime()).toString())
										/ (Integer.valueOf(i.getInsid().toString())) * 100);// 规范符合率
					} else {
						data[ii][5] = 0;
					}
				}
				ii++;
			}
			filename = "设备焊接数据" + sdf.format(new Date())+".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			
			new CommonExcelUtil(dtime, titles, data, path, "设备焊接数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
		
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}	
	
	/**
	 * 人员生产数据报表导出
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportPersonData")
	@ResponseBody
	public ResponseEntity<byte[]> exportPersonData(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		WeldDto dto = new WeldDto();
		String dtime = "统计日期："+time1+"--"+time2;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			List<DataStatistics> list = dss.getAllPersonData(im.getUserInsframework());
			String[] titles = new String []{"焊工编号","焊工名称","焊接焊缝数","焊接时间","工作时间","焊接效率(%)","焊丝消耗(KG)","电能消耗(KWH)","气体消耗(L)","规范符合率(%)"};
			Object[][] data = new Object[list.size()][10];
			int ii=0;
			for(DataStatistics i:list){
				if(ii<list.size()){
					dto.setWelderno(i.getSerialnumber());
					data[ii][0]=i.getSerialnumber();
					data[ii][1]=i.getName();
					DataStatistics weld = null;
					BigInteger worktime = null,standytime=null;
					DataStatistics junctionnum = dss.getWorkJunctionNumByWelder(null, dto);
					DataStatistics parameter = dss.getParameter();
					if(junctionnum.getJunctionnum()!=0){
						data[ii][2]=junctionnum.getJunctionnum();//焊接焊缝数
						worktime = dss.getStaringUpTimeByWelder(null, dto);
						data[ii][4]=getTimeStrBySecond(worktime);//工作时间
						standytime = dss.getStandytimeByWelder(null, dto);
						weld = dss.getWorkTimeAndEleVolByWelder(null, dto);
						double standytimes = 0,time=0,electric=0;
						if(standytime!=null){
							standytimes = standytime.doubleValue()/60/60;
						}
						if(weld!=null){
							time = weld.getWorktime().doubleValue()/60/60;
							electric = (double)Math.round((time*(weld.getElectricity()*weld.getVoltage())/1000+standytimes*parameter.getStandbypower()/1000)*100)/100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
						}else{
							electric = (double)Math.round((time+standytimes*parameter.getStandbypower()/1000)*100)/100;
						}
						data[ii][7]=electric;//电能消耗
					}else{
						data[ii][2]=0;
						data[ii][4]="00:00:00";//工作时间
						data[ii][7]=0;
					}
					if(weld!=null){
						data[ii][3]=getTimeStrBySecond(weld.getWorktime());//焊接时间
						double weldingproductivity = (double)Math.round(weld.getWorktime().doubleValue()/worktime.doubleValue()*100*100)/100;
						data[ii][5]=weldingproductivity;//焊接效率
						if(parameter!=null){
							double  time = weld.getWorktime().doubleValue()/60;
							String[] str = parameter.getWireweight().split(",");
							double wireweight =Double.valueOf(str[0]);
							double wire = (double)Math.round(wireweight*parameter.getSpeed()*time*100)/100;//焊丝消耗量=焊丝|焊丝重量*送丝速度*焊接时间
							double air = (double)Math.round(parameter.getAirflow()*time*100)/100;//气体消耗量=气体流量*焊接时间
//							String sperate = new DecimalFormat("0.00").format((float)Integer.valueOf(weld.getWorktime().subtract(new BigInteger(weld.getTime())).toString())/(Integer.valueOf(weld.getWorktime().toString()))*100);
							if(String.valueOf(weld.getWorktime()).equals("0")){
								data[ii][9]=0;//规范符合率
							}else{
								String sperate = new DecimalFormat("0.00").format((float)Integer.valueOf(weld.getWorktime().subtract(new BigInteger(weld.getTime())).toString())/(Integer.valueOf(weld.getWorktime().toString()))*100);
								data[ii][9]=sperate;//规范符合率
							}
							data[ii][6]=wire;//焊丝消耗
							data[ii][8]=air;//气体消耗
							
						}
					}else{
						data[ii][3]="00:00:00";
						data[ii][5]=0;
						data[ii][6]=0;
						data[ii][8]=0;
						data[ii][9]=0;//规范符合率
					}
				}
				ii++;
			}
			filename = "人员生产数据" + sdf.format(new Date())+".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			
			new CommonExcelUtil(dtime, titles, data, path, "人员生产数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
		
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}	
	
	/**
	 * 人员焊接数据报表导出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportPersonWelddata")
	@ResponseBody
	public ResponseEntity<byte[]> exportPersonWelddata(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		WeldDto dto = new WeldDto();
		String dtime = "统计日期："+time1+"--"+time2;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			dto.setParent(im.getUserInsframework());
			List<DataStatistics> ilist = dss.getWeldPersonInCountData(dto);
			String[] titles = new String[]{"焊工编号","焊工姓名","累计焊接时间","正常段时长","超规范时长","规范符合率(%)"};
			Object[][] data = new Object[ilist.size()][6];
			int ii = 0;
			for (DataStatistics i : ilist) {
				if (ii < ilist.size()) {
					data[ii][0] = i.getInsname();// 焊工编号
					data[ii][1] = i.getName();// 焊工 姓名
					data[ii][2] = getTimeStrBySecond(i.getInsid());// 累计焊接时间
					data[ii][3] = getTimeStrBySecond(i.getInsid().subtract(i.getWorktime()));// 正常焊接时长
					data[ii][4] = getTimeStrBySecond(i.getWorktime());// 超规范焊接时长
					if (Integer.valueOf(i.getInsid().toString()) + Integer.valueOf(i.getWorktime().toString()) != 0) {
						data[ii][5] = new DecimalFormat("0.00")
								.format((float) Integer.valueOf(i.getInsid().subtract(i.getWorktime()).toString())
										/ (Integer.valueOf(i.getInsid().toString())) * 100);// 规范符合率
					} else {
						data[ii][5] = 0;
					}
				}
				ii++;
			}
			filename = "人员焊接数据" + sdf.format(new Date())+".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			
			new CommonExcelUtil(dtime, titles, data, path, "人员焊接数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
		
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}	
	
	/**
	 * 工件生产数据报表导出
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportWorkpieceData")
	@ResponseBody
	public ResponseEntity<byte[]> exportWorkpieceData(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String junctionno = request.getParameter("junctionno");
		WeldDto dto = new WeldDto();
		String dtime = "统计日期："+time1+"--"+time2;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			List<DataStatistics> list = dss.getAllJunctionData("%"+ junctionno+"%");
			String[] titles = new String []{"焊缝编号","焊接时间","工作时间","焊接效率(%)","焊丝消耗(KG)","电能消耗(KWH)","气体消耗(L)","规范符合率(%)"};
			Object[][] data = new Object[list.size()][8];
			int ii=0;
			for(DataStatistics i:list){
				if(ii<list.size()){
					dto.setJunctionno(i.getSerialnumber());
					data[ii][0]=i.getSerialnumber();
					BigInteger worktime = dss.getStaringUpTimeByJunction(null, dto);
					DataStatistics parameter = dss.getParameter();
					BigInteger standytime = null;
					DataStatistics weld = null;
					if(worktime!=null){
						data[ii][2]=getTimeStrBySecond(worktime);//工作时间
						weld = dss.getWorkTimeAndEleVolByJunction(null, dto);
						standytime = dss.getStandytimeByJunction(null, dto);

						double standytimes = 0,time=0,electric=0;
						if(standytime!=null){
							standytimes = standytime.doubleValue()/60/60;
						}
						if(weld!=null){
							time = weld.getWorktime().doubleValue()/60/60;
							electric = (double)Math.round((time*(weld.getElectricity()*weld.getVoltage())/1000+standytimes*parameter.getStandbypower()/1000)*100)/100;//电能消耗量=焊接时间*焊接平均电流*焊接平均电压+待机时间*待机功率
						}else{
							electric = (double)Math.round((time+standytimes*parameter.getStandbypower()/1000)*100)/100;
						}
						data[ii][5]=electric;//电能消耗
					}else{
						data[ii][2]="00:00:00";
						data[ii][5]=0;
					}
					if(worktime!=null && weld!=null){
						data[ii][1]=getTimeStrBySecond(weld.getWorktime());//焊接时间
						double weldingproductivity = (double)Math.round(weld.getWorktime().doubleValue()/worktime.doubleValue()*100*100)/100;
						data[ii][3]=weldingproductivity;//焊接效率
						if(parameter!=null){
							double  time = weld.getWorktime().doubleValue()/60;
							String[] str = parameter.getWireweight().split(",");
							double wireweight =Double.valueOf(str[0]);
							double wire = (double)Math.round(wireweight*parameter.getSpeed()*time*100)/100;//焊丝消耗量=焊丝|焊丝重量*送丝速度*焊接时间
							double air = (double)Math.round(parameter.getAirflow()*time*100)/100;//气体消耗量=气体流量*焊接时间
							if(String.valueOf(weld.getWorktime()).equals("0")){
								data[ii][7]=0;
							}else{
								String sperate = new DecimalFormat("0.00").format((float)Integer.valueOf(weld.getWorktime().subtract(new BigInteger(weld.getTime())).toString())/(Integer.valueOf(weld.getWorktime().toString()))*100);
								data[ii][7]=sperate;
							}
//							String sperate = new DecimalFormat("0.00").format((float)Integer.valueOf(weld.getWorktime().subtract(new BigInteger(weld.getTime())).toString())/(Integer.valueOf(weld.getWorktime().toString()))*100);
							data[ii][4]=wire;//焊丝消耗
							data[ii][6]=air;//气体消耗
							
						}
					}else{
						data[ii][1]="00:00:00";
						data[ii][3]=0;
						data[ii][4]=0;
						data[ii][6]=0;
						data[ii][7]=0;
					}
				}
				ii++;
			}
			filename = "工件生产数据" + sdf.format(new Date())+".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			
			new CommonExcelUtil(dtime, titles, data, path, "工件生产数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
		
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}	
	
	/**
	 * 工件焊接数据报表导出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportWorkpieceWelddata")
	@ResponseBody
	public ResponseEntity<byte[]> exportWorkpieceWelddata(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String junctionno = request.getParameter("junctionno");
		WeldDto dto = new WeldDto();
		String dtime = "统计日期："+time1+"--"+time2;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			List<DataStatistics> ilist = dss.getWeldWorkpieceInCountData(dto,"%"+ junctionno+"%");
			String[] titles = new String[]{"焊缝编号","累计焊接时间","正常段时长","超规范时长","规范符合率(%)"};
			Object[][] data = new Object[ilist.size()][5];
			int ii = 0;
			for (DataStatistics i : ilist) {
				if (ii < ilist.size()) {
					data[ii][0] = i.getInsname();// 工件编号
					data[ii][1] = getTimeStrBySecond(i.getInsid());// 累计焊接时间
					data[ii][2] = getTimeStrBySecond(i.getInsid().subtract(i.getWorktime()));// 正常焊接时长
					data[ii][3] = getTimeStrBySecond(i.getWorktime());// 超规范焊接时长
					if (Integer.valueOf(i.getInsid().toString()) + Integer.valueOf(i.getWorktime().toString()) != 0) {
						data[ii][4] = new DecimalFormat("0.00")
								.format((float) Integer.valueOf(i.getInsid().subtract(i.getWorktime()).toString())
										/ (Integer.valueOf(i.getInsid().toString())));// 规范符合率
					} else {
						data[ii][4] = 0;
					}
				}
				ii++;
			}
			filename = "工件焊接数据" + sdf.format(new Date())+".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			
			new CommonExcelUtil(dtime, titles, data, path, "工件焊接数据");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
		
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			file.delete();
		}
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
	
	/**
	 * 设备生产数据报表导出
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportTaskDetail")
	@ResponseBody
	public ResponseEntity<byte[]> exportTaskDetail(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		BigInteger itemid = null;
		String welderno = "",taskno = "",dtoTime1 = "",dtoTime2 = "";
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
		WeldDto dto = new WeldDto();
		String dtime = "统计日期："+dtoTime1+"--"+dtoTime2;
		try{
			List<DataStatistics> list = dss.getTask(itemid, welderno, taskno, dtoTime1, dtoTime2);
		
			List<DataStatistics> task = dss.getTaskDetail(itemid, welderno, taskno, dtoTime1, dtoTime2);
			String[] titles = new String []{"焊工班组","焊工编号","焊工姓名","焊机编号","任务编号","开始时间","结束时间","使用通道","良好段","报警段","平均焊接电流","平均焊接电压","规范符合率(%)"};
			Object[][] data = new Object[list.size()][13];
			int ii=0;
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
				for(int j=0;j<task.size();j++){
					if(list.get(i).getType()!=1){
						if(list.get(i).getTaskid().equals(task.get(j).getTaskid()) && list.get(i).getWelderid().equals(task.get(j).getWelderid()) && list.get(i).getMachineid().equals(task.get(j).getMachineid())){
							data[ii][0] = task.get(j).getName();
							data[ii][1] = task.get(j).getWelderno();
							data[ii][2] = task.get(j).getWeldername();
							data[ii][3] = task.get(j).getMachineno();
							data[ii][4] = task.get(j).getTaskno();
							data[ii][5] = task.get(j).getStarttime();
							data[ii][6] = list.get(i).getEndtime();
							data[ii][7] = task.get(j).getChannel();
							if(task.get(j).getWorktime()!=null && !"".equals(task.get(j).getWorktime())){
								data[ii][8] = getTimeStrBySecond(task.get(j).getWorktime());
							}else{
								data[ii][8] = "00:00:00";
							}
							if(task.get(j).getWarntime()!=null && !"".equals(task.get(j).getWarntime())){
								data[ii][9] = getTimeStrBySecond(task.get(j).getWarntime());
							}else{
								data[ii][9] = "00:00:00";
							}
							data[ii][10] = (double)Math.round(task.get(j).getElectricity()*100)/100;
							data[ii][11] = (double)Math.round(task.get(j).getVoltage()*100)/100;
							double ratio = 0;
							if(task.get(j).getWarntime()!=null && !"".equals(task.get(j).getWarntime())){
								ratio = (double)Math.round(task.get(j).getWorktime().doubleValue()/(task.get(j).getWorktime().doubleValue()+task.get(j).getWarntime().doubleValue())*10000)/100;
							}
							data[ii][12] = ratio;//规范符合率
						}
					}
				}
				ii++;
			}
			filename = "生产任务详情" + sdf.format(new Date())+".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			
			new CommonExcelUtil(dtime, titles, data, path, "生产任务详情");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
		
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}	
	
	/**
	 * 故障报表导出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exportFauit")
	@ResponseBody
	public ResponseEntity<byte[]> exportFauit(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		WeldDto dto = new WeldDto();
		String dtime = "统计日期："+time1+"--"+time2;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			int fauit = 0 ;
			if(iutil.isNull(request.getParameter("fauit"))){
				fauit  = Integer.parseInt(request.getParameter("fauit"));
			}
			List<DataStatistics> list = dss.getFauit(dto, fauit);
			String[] titles = new String[]{"焊机编号","焊机归属","故障类型","故障次数"};
			Object[][] data = new Object[list.size()][4];
			int ii=0;
			for(DataStatistics i:list){
				if(ii<list.size()){
					data[ii][0]=i.getName();//焊机编号
					data[ii][1]=i.getInsname();//焊机归属
					data[ii][2]=i.getValuename();//故障类型
					data[ii][3]=i.getNum();//故障次数
				}
				ii++;
			}
			filename = "故障报表" + sdf.format(new Date())+".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			
			new CommonExcelUtil(dtime, titles, data, path, "故障报表");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
		
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}	
	
	/**
	 * 导出采集模块
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exporGather")
	@ResponseBody
	public ResponseEntity<byte[]> exporGather(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		try {
			String str=(String) request.getSession().getAttribute("searchStr");
			String parentid = request.getParameter("parent");
			BigInteger parent = null;
			if(iutil.isNull(parentid)){
				parent = new BigInteger(parentid);
			}
			List<Gather> list = gs.getGatherAll(str, parent);
			String dtime = null;
			String[] titles = new String[]{"采集模块编号","所属项目","采集模块状态","采集模块通讯协议","采集模块IP地址","采集模块MAC地址","采集模块出厂时间"};
			Object[][] data = new Object[list.size()][7];
			for(int i =0; i<list.size();i++){
				data[i][0] = list.get(i).getGatherNo();
				data[i][1] = list.get(i).getItemname();
				data[i][2] = list.get(i).getStatus();
				data[i][3] = list.get(i).getProtocol();
				data[i][4] = list.get(i).getIpurl();
				data[i][5] = list.get(i).getMacurl();
				data[i][6] = list.get(i).getLeavetime();
			}
			filename = "采集模块" + sdf.format(new Date()) + ".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			new CommonExcelUtil(dtime, titles, data, path, "采集模块数据");
			
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
			
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		}catch (Exception e) {
	    	return null;
		}  finally {
			file.delete();
		}
	}
	
	/**
	 * 导出焊工
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exporWelder")
	@ResponseBody
	public ResponseEntity<byte[]> exporWelder(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		try {
			String str=(String) request.getSession().getAttribute("searchStr");
			String parentid = request.getParameter("parent");
			BigInteger parent = null;
			if(iutil.isNull(parentid)){
				parent = new BigInteger(parentid);
			}
			List<Person> list = ps.findAll(parent,str);
			String dtime = null;
			String[] titles = new String[]{"姓名","编号","手机","级别","卡号","资质","部门","备注"};
			Object[][] data = new Object[list.size()][8];
			for(int i =0; i<list.size();i++){
				data[i][0] = list.get(i).getName();
				data[i][1] = list.get(i).getWelderno();
				data[i][2] = list.get(i).getCellphone();
				data[i][3] = list.get(i).getValuename();
				data[i][4] = list.get(i).getCardnum();
				data[i][5] = list.get(i).getValuenamex();
				data[i][6] = list.get(i).getInsname();
				data[i][7] = list.get(i).getBack();
			}
			filename = "焊工管理" + sdf.format(new Date()) + ".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			new CommonExcelUtil(dtime, titles, data, path, "焊工管理数据");
			
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
			
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		}catch (Exception e) {
	    	return null;
		}  finally {
			file.delete();
		}
	}
	
	/**
	 * 导出派工任务
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/exporWeldTask")
	@ResponseBody
	public ResponseEntity<byte[]> exporWeldTask(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		try {
			String serach="";
			MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			int instype = im.getUserInsfType(new BigInteger(String.valueOf(user.getId())));
			BigInteger userinsid = im.getUserInsfId(new BigInteger(String.valueOf(user.getId())));
			int bz=0;
			if(instype==20){
			}else if(instype==23){
				serach = "j.fitemId="+userinsid;
			}else{
				List<Insframework> ls = im.getInsIdByParent(userinsid,24);
				for(Insframework inns : ls ){
					if(bz==0){
						serach=serach+"(j.fitemId="+inns.getId();
					}else{
						serach=serach+" or j.fitemId="+inns.getId();
					}
					bz++;
				}
				serach=serach+" or j.fitemId="+userinsid+")";
			}
			if(request.getParameter("searchStr")!=null&&serach!=null&&serach!=""){
				serach=serach+" and "+request.getParameter("searchStr");
			}
			if(request.getParameter("searchStr")!=null&&(serach==null||serach=="")){
				serach=serach+request.getParameter("searchStr");
			}
			List<WeldedJunction> list = wjm.getWeldedJunctionAll(serach);
			String dtime = null;
			String[] titles = new String[]{"任务编号","任务等级","所属班组","计划开始时间","计划结束时间","任务评价","评价等级"};
			Object[][] data = new Object[list.size()][7];
			for(int i =0; i<list.size();i++){
				data[i][0] = list.get(i).getWeldedJunctionno();
				data[i][1] = list.get(i).getArea();
				data[i][2] = list.get(i).getIname();
				data[i][3] = list.get(i).getStartTime();
				data[i][4] = list.get(i).getEndTime();
				data[i][5] = list.get(i).getSystems();
				data[i][6] = list.get(i).getChildren();
			}
			filename = "派工任务管理" + sdf.format(new Date()) + ".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			new CommonExcelUtil(dtime, titles, data, path, "派工任务管理数据");
			
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
			
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		}catch (Exception e) {
	    	return null;
		}  finally {
			file.delete();
		}
	}
	
	/**
	 * 设备任务表导出
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportMachineTask")
	@ResponseBody
	public ResponseEntity<byte[]> exportMachineTask(HttpServletRequest request,HttpServletResponse response){
		File file = null;
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String item = request.getParameter("item");
		int status = Integer.parseInt(request.getParameter("status"));
		WeldDto dto = new WeldDto();
		BigInteger itemid = null;
		String dtime = "统计日期："+time1+"--"+time2;
		try{
			if(iutil.isNull(time1)){
				dto.setDtoTime1(time1);
			}
			if(iutil.isNull(time2)){
				dto.setDtoTime2(time2);
			}
			if(iutil.isNull(item)){
				itemid = new BigInteger(item);
			}
			List<DataStatistics> list = dss.getMachineTask(itemid, dss.getDay(time1, time2), status);
			String[] titles = new String []{"所属班组","设备编号","日期","任务号","状态"};
			Object[][] data = new Object[list.size()][9];
			int ii=0;
			for(DataStatistics i:list){
				if(ii<list.size()){
					data[ii][0]=i.getInsname();
					data[ii][1]=i.getMachineno();
					data[ii][2]=i.getTime();
					data[ii][3]=i.getTaskno();
					data[ii][4]=i.getType()==1?"未分配":"已分配";
				}
				ii++;
			}
			filename = "焊机任务表" + sdf.format(new Date())+".xls";

			ServletContext scontext=request.getSession().getServletContext();
			//获取绝对路径
			String abpath=scontext.getRealPath("");
			//String contextpath=scontext. getContextPath() ; 获取虚拟路径
			
			String path = abpath+"excelfiles/" + filename;
			
			new CommonExcelUtil(dtime, titles, data, path, "设备任务表");
			file = new File(path);
			HttpHeaders headers = new HttpHeaders();
			String fileName = "";
			fileName = new String(filename.getBytes("gb2312"),"iso-8859-1");
		
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			
			//处理ie无法下载的问题
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setHeader( "Content-Disposition", 
					"attachment;filename=\""+ fileName); 
			ServletOutputStream o = response.getOutputStream();
			o.flush();
			
			return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			return null;
		} finally {
			file.delete();
		}
	}	
	
}
