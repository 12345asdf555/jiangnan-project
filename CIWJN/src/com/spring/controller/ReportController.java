package com.spring.controller;

import com.erdangjiade.spring.security.CustomerContextHolder;
import com.github.pagehelper.PageInfo;
import com.spring.dto.WeldDto;
import com.spring.model.Report;
import com.spring.page.Page;
import com.spring.service.InsframeworkService;
import com.spring.service.LiveDataService;
import com.spring.service.ReportService;
import com.spring.util.IsnullUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/rep", produces = {"text/json;charset=UTF-8"})
public class ReportController {

    private Page page;
    private int pageIndex = 1;
    private int pageSize = 10;
    private int total = 0;
    @Autowired
    private ReportService reportService;

    @Autowired
    private LiveDataService lm;

    @Autowired
    private InsframeworkService insm;

    IsnullUtil iutil = new IsnullUtil();

    @RequestMapping("/weldpara")
    public String WeldPara(HttpServletRequest request) {
        return "report/WeldParameter";
    }

    @RequestMapping("/warnreport")
    public String WarnRep(HttpServletRequest request) {
        return "report/WarnReport";
    }

    @RequestMapping("/wireuse")
    public String WireUse(HttpServletRequest request) {
        return "report/WireUse";
    }

    @RequestMapping("/welderreport")
    public String WelderRep(HttpServletRequest request) {
        return "report/WelderReport";
    }

    @RequestMapping("/alarm")
    public String Alarm(HttpServletRequest request) {
        return "report/AlarmManage";
    }

    //去焊工历史界面
    @RequestMapping("/historyWelder")
    public String History(HttpServletRequest request) {
        return "td/HistoryWelder";
    }
    //去任务历史界面
    @RequestMapping("/historyJunction")
    public String HistoryJunction(HttpServletRequest request) {
        return "td/HistoryJunction";
    }
    //去焊机历史界面
    @RequestMapping("/historyMachine")
    public String historyMachine(HttpServletRequest request) {
        return "td/HistoryMachine";
    }

/*	@RequestMapping("/getWeldPara")
	@ResponseBody
	public String getWeldPara(HttpServletRequest request){
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		System.out.println(myuser.getId());
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String parentId = request.getParameter("parent");
		String insid = request.getParameter("insid");
		String type = request.getParameter("otype");
		WeldDto dto = new WeldDto();
		if(!iutil.isNull(parentId)){
			//数据权限处理
			BigInteger uid = lm.getUserId(request);
			String afreshLogin = (String)request.getAttribute("afreshLogin");
			if(iutil.isNull(afreshLogin)){
				return "0";
			}
			int types = insm.getUserInsfType(uid);
			if(types==21){
				parentId = insm.getUserInsfId(uid).toString();
			}
		}
		BigInteger parent = null;
		if(iutil.isNull(time1)){
			dto.setDtoTime1(time1);
		}
		if(iutil.isNull(time2)){
			dto.setDtoTime2(time2);
		}
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		if(iutil.isNull(type)){
			if(type.equals("1")){
				dto.setYear("year");
			}else if(type.equals("2")){
				dto.setMonth("month");
			}else if(type.equals("3")){
				dto.setDay("day");
			}else if(type.equals("4")){
				dto.setWeek("week");
			}
		}
		Report rap = new Report();
		Report rap1 = null;
		BigInteger iid = null;
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String search = request.getParameter("searchStr");
		if(iutil.isNull(insid)){
			iid = new BigInteger(insid);
		}
		page = new Page(pageIndex,pageSize,total);
		List<WeldingMachine> list = wmm.getWeldingMachineAll(page,iid,search);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldingMachine> pageinfo = new PageInfo<WeldingMachine>(list);
			total = pageinfo.getTotal();
		}
		request.setAttribute("userList", list);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		String times;
		String hour1;
		String minute1;
		String second1;
		String times1;
		String hour2;
		String minute2;
		String second2;
		try{
			for(WeldingMachine wm:list){
				BigInteger wpsid=reportService.getWpsid(wm.getId());
				if(wpsid!=null){
					rap = reportService.getWps(wpsid);
				}else{
					rap.setFstandardele(0);
					rap.setFstandardvol(0);
				}
				long hour = reportService.getWeldingTime(dto, wm.getId())/3600;
				if(hour<10){
					hour1 = "0" + String.valueOf(hour) + ":";
				}else{
					hour1 = String.valueOf(hour) + ":";
				}
				long last = reportService.getWeldingTime(dto, wm.getId())%3600;
				long minute = last/60;
				if(minute<10){
					minute1 = "0" + String.valueOf(minute) + ":";
				}else{
					minute1 = String.valueOf(minute) + ":";
				}
				long second = last%60;
				if(second<10){
					second1 = "0" + String.valueOf(second);
				}else{
					second1 = String.valueOf(second);
				}
				times = hour1 + minute1 + second1;
				long ontime = reportService.getOnTime(dto, wm.getId())/3600;
				if(ontime<10){
					hour2 = "0" + String.valueOf(ontime) + ":";
				}else{
					hour2 = String.valueOf(ontime) + ":";
				}
				long last1 = reportService.getWeldingTime(dto, wm.getId())%3600;
				long minutes = last1/60;
				if(minutes<10){
					minute2 = "0" + String.valueOf(minutes) + ":";
				}else{
					minute2 = String.valueOf(minutes) + ":";
				}
				long seconds = last%60;
				if(seconds<10){
					second2 = "0" + String.valueOf(seconds);
				}else{
					second2 = String.valueOf(seconds);
				}
				times1 = hour2 + minute2 + second2;
				DecimalFormat df = new DecimalFormat("0.0");
				String ele = df.format((float)reportService.getRealEle(dto, wm.getId()));
				String vol = df.format((float)reportService.getRealVol(dto, wm.getId()));
				rap1 = reportService.getSyspara();
				json.put("standardvol",rap.getFstandardvol());
				json.put("standardele",rap.getFstandardele());
				json.put("machineid", wm.getEquipmentNo());
				json.put("machinemodel",wm.getModel());
				json.put("inspower",rap1.getFinspower());
				json.put("afv",rap1.getFafv());
				json.put("weldingtime", times);
				json.put("onlinetime", times1);
				json.put("realele", ele);
				json.put("realvol", vol);
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}*/
	
/*	@RequestMapping("/getWireUse")
	@ResponseBody
	public String getWireUse(HttpServletRequest request){
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		System.out.println(myuser.getId());
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		String parentId = request.getParameter("parent");
		String insid = request.getParameter("insid");
		String type = request.getParameter("otype");
		WeldDto dto = new WeldDto();
		if(!iutil.isNull(parentId)){
			//数据权限处理
			BigInteger uid = lm.getUserId(request);
			String afreshLogin = (String)request.getAttribute("afreshLogin");
			if(iutil.isNull(afreshLogin)){
				return "0";
			}
			int types = insm.getUserInsfType(uid);
			if(types==21){
				parentId = insm.getUserInsfId(uid).toString();
			}
		}
		BigInteger parent = null;
		if(iutil.isNull(time1)){
			dto.setDtoTime1(time1);
		}
		if(iutil.isNull(time2)){
			dto.setDtoTime2(time2);
		}
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		if(iutil.isNull(type)){
			if(type.equals("1")){
				dto.setYear("year");
			}else if(type.equals("2")){
				dto.setMonth("month");
			}else if(type.equals("3")){
				dto.setDay("day");
			}else if(type.equals("4")){
				dto.setWeek("week");
			}
		}
		Report rap = new Report();
		Report rap1 = null;
		BigInteger iid = null;
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String search = request.getParameter("searchStr");
		if(iutil.isNull(insid)){
			iid = new BigInteger(insid);
		}
		page = new Page(pageIndex,pageSize,total);
		List<WeldingMachine> list = wmm.getWeldingMachineAll(page,iid,search);
		long total = 0;
		
		if(list != null){
			PageInfo<WeldingMachine> pageinfo = new PageInfo<WeldingMachine>(list);
			total = pageinfo.getTotal();
		}

		request.setAttribute("userList", list);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		String times;
		String hour1;
		String minute1;
		String second1;
		String times1;
		String hour2;
		String minute2;
		String second2;
		try{
			for(WeldingMachine wm:list){
				BigInteger wpsid=reportService.getWpsid(wm.getId());
				if(wpsid!=null){
					rap = reportService.getWps(wpsid);
				}else{
					rap.setFdiameter(0);
				}
				long hour = reportService.getWeldingTime(dto, wm.getId())/3600;
				if(hour<10){
					hour1 = "0" + String.valueOf(hour) + ":";
				}else{
					hour1 = String.valueOf(hour) + ":";
				}
				long last = reportService.getWeldingTime(dto, wm.getId())%3600;
				long minute = last/60;
				if(minute<10){
					minute1 = "0" + String.valueOf(minute) + ":";
				}else{
					minute1 = String.valueOf(minute) + ":";
				}
				long second = last%60;
				if(second<10){
					second1 = "0" + String.valueOf(second);
				}else{
					second1 = String.valueOf(second);
				}
				times = hour1 + minute1 + second1;
				long ontime = reportService.getOnTime(dto, wm.getId())/3600;
				if(ontime<10){
					hour2 = "0" + String.valueOf(ontime) + ":";
				}else{
					hour2 = String.valueOf(ontime) + ":";
				}
				long last1 = reportService.getWeldingTime(dto, wm.getId())%3600;
				long minutes = last1/60;
				if(minutes<10){
					minute2 = "0" + String.valueOf(minutes) + ":";
				}else{
					minute2 = String.valueOf(minutes) + ":";
				}
				long seconds = last%60;
				if(seconds<10){
					second2 = "0" + String.valueOf(seconds);
				}else{
					second2 = String.valueOf(seconds);
				}
				times1 = hour2 + minute2 + second2;
				rap1 = reportService.getSyspara();
				json.put("machineid", wm.getEquipmentNo());
				json.put("machinemodel",wm.getModel());
				json.put("diameter", rap.getFdiameter());
				json.put("speed", rap1.getFspeed());
				json.put("weldingtime", times);
				json.put("onlinetime", times1);
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}*/

    @RequestMapping("/getTime")
    @ResponseBody
    public String getTime(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject obj = new JSONObject();
//		pageIndex = Integer.parseInt(request.getParameter("page"));
//		pageSize = Integer.parseInt(request.getParameter("rows"));
        String search = request.getParameter("searchStr");
        String parentId = request.getParameter("parent");
        BigInteger parent = null;
        if (iutil.isNull(parentId)) {
            parent = new BigInteger(parentId);
        }
//		page = new Page(pageIndex,pageSize,total);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datetime = "%" + sdf.format(date) + "%";
//		List<WeldingMachine> list = wmm.getWeldingMachineAll(page,parent,search);
        List<Report> list = reportService.getAllPara(parent, search, datetime);
        long total = 0;
        if (list != null) {
            PageInfo<Report> pageinfo = new PageInfo<Report>(list);
            total = pageinfo.getTotal();
        }
        Report r1 = reportService.getSyspara();
        try {
            for (Report wm1 : list) {
/*		 long zx = reportService.getZxTime(wm1.getId(), datetime);
		 long hj = reportService.getHjTime(wm1.getId(), datetime);*/
                json.put("id", wm1.getId());
                json.put("afv", r1.getFafv());
                json.put("standardele", wm1.getFstandardele());
                json.put("standardvol", wm1.getFstandardvol());
                json.put("boottime", "");
                json.put("machineid", wm1.getEno());
                json.put("machinemodel", wm1.getModel());
                json.put("macstatus", "关机");
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        obj.put("total", total);
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getTWeld")
    @ResponseBody
    public String getTWeld(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject obj = new JSONObject();
//		pageIndex = Integer.parseInt(request.getParameter("page"));
//		pageSize = Integer.parseInt(request.getParameter("rows"));
        String search = request.getParameter("searchStr");
        String parentId = request.getParameter("parent");
        BigInteger parent = null;
        if (iutil.isNull(parentId)) {
            parent = new BigInteger(parentId);
        }
//		page = new Page(pageIndex,pageSize,total);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datetime = "%" + sdf.format(date) + "%";
        List<Report> list = reportService.getAllPara(parent, search, datetime);
        long total = 0;
        if (list != null) {
            PageInfo<Report> pageinfo = new PageInfo<Report>(list);
            total = pageinfo.getTotal();
        }
        Report r1 = reportService.getSyspara();
        try {
            for (Report wm1 : list) {
/*		 long zx = reportService.getZxTime(wm1.getId(), datetime);
		 long hj = reportService.getHjTime(wm1.getId(), datetime);*/
                json.put("boottime", wm1.getTime());
                json.put("machineid", wm1.getEno());
                json.put("machinemodel", wm1.getModel());
                json.put("diameter", wm1.getDia());
                json.put("speed", r1.getFspeed());
                json.put("macstatus", "关机");
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        obj.put("total", total);
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getWelderReport")
    @ResponseBody
    public String getWelderReport(HttpServletRequest request) {
/*		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		System.out.println(myuser.getId());*/
        String time1 = request.getParameter("dtoTime1");
        String time2 = request.getParameter("dtoTime2");
        String parentId = request.getParameter("parent");
        String insid = request.getParameter("insid");
        String type = request.getParameter("otype");
        WeldDto dto = new WeldDto();
        if (!iutil.isNull(parentId)) {
            //数据权限处理
            BigInteger uid = lm.getUserId(request);
            String afreshLogin = (String) request.getAttribute("afreshLogin");
            if (iutil.isNull(afreshLogin)) {
                return "0";
            }
            int types = insm.getUserInsfType(uid);
            if (types == 21) {
                parentId = insm.getUserInsfId(uid).toString();
            }
        }
        BigInteger parent = null;
        if (iutil.isNull(time1)) {
            dto.setDtoTime1(time1);
        }
        if (iutil.isNull(time2)) {
            dto.setDtoTime2(time2);
        }
        if (iutil.isNull(parentId)) {
            parent = new BigInteger(parentId);
        }
        if (iutil.isNull(type)) {
            if (type.equals("1")) {
                dto.setYear("year");
            } else if (type.equals("2")) {
                dto.setMonth("month");
            } else if (type.equals("3")) {
                dto.setDay("day");
            } else if (type.equals("4")) {
                dto.setWeek("week");
            }
        }
        pageIndex = Integer.parseInt(request.getParameter("page"));
        pageSize = Integer.parseInt(request.getParameter("rows"));
        String str = request.getParameter("searchStr");
        BigInteger iid = null;
        if (iutil.isNull(insid)) {
            iid = new BigInteger(insid);
        }
        page = new Page(pageIndex, pageSize, total);
        List<Report> list = reportService.findAllWelder(page, dto);
        long total = 0;

        if (list != null) {
            PageInfo<Report> pageinfo = new PageInfo<Report>(list);
            total = pageinfo.getTotal();
        }
        request.setAttribute("userList", list);
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject obj = new JSONObject();
        String hour1;
        String minute1;
        String second1;
        String times;
        try {
            for (Report repo : list) {
                json.put("machinemodel", repo.getFmachinemodel());
                json.put("machineid", repo.getFmachineid());
                json.put("fname", repo.getFname());
                json.put("phone", repo.getNum3());
                json.put("weldernum", repo.getFweldernum());
                json.put("back", repo.getFback());
                if (repo.getResult1().toString() == "0") {
                    json.put("valuetime", "00:00:00");
                } else {
                    long hour = Integer.valueOf(repo.getResult1().toString()) / 3600;
                    if (hour < 10) {
                        hour1 = "0" + String.valueOf(hour) + ":";
                    } else {
                        hour1 = String.valueOf(hour) + ":";
                    }
                    long last = Integer.valueOf(repo.getResult1().toString()) % 3600;
                    long minute = last / 60;
                    if (minute < 10) {
                        minute1 = "0" + String.valueOf(minute) + ":";
                    } else {
                        minute1 = String.valueOf(minute) + ":";
                    }
                    long second = last % 60;
                    if (second < 10) {
                        second1 = "0" + String.valueOf(second);
                    } else {
                        second1 = String.valueOf(second);
                    }
                    times = hour1 + minute1 + second1;
                    json.put("valuetime", times);
                }
                ary.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        obj.put("total", total);
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/historyCurve")
    @ResponseBody
    public String historyCurve(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject obj = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Calendar cb = Calendar.getInstance();
        String time1 = request.getParameter("dtoTime1");
        String time2 = request.getParameter("dtoTime2");
        String parentId = request.getParameter("parent");
        WeldDto dto = new WeldDto();
        if (!iutil.isNull(parentId)) {
            //数据权限处理
            BigInteger uid = lm.getUserId(request);
            String afreshLogin = (String) request.getAttribute("afreshLogin");
            if (iutil.isNull(afreshLogin)) {
                return "0";
            }
            int types = insm.getUserInsfType(uid);
            if (types == 21) {
                parentId = insm.getUserInsfId(uid).toString();
            }
        }
        if (iutil.isNull(time1)) {
            dto.setDtoTime1(time1);
        }
        if (iutil.isNull(time2)) {
            dto.setDtoTime2(time2);
        }
        //dto.setRtDataTableName("rtdata2021_04_05_2021_04_11");
        String fid = request.getParameter("fid");//任务编号
        BigInteger mach = new BigInteger(request.getParameter("mach"));//焊机id
        String welderid = request.getParameter("welderid");//焊工编号
        //切换实时数据库数据源
        CustomerContextHolder.setCustomerType(CustomerContextHolder.DATASOURCE_REALTIME);
        pageIndex = 1;
        pageSize = 28800;
        page = new Page(pageIndex, pageSize, total);
        List<Report> list = reportService.historyData(page, dto, fid, mach, welderid);
        //切换实时数据库数据源
        CustomerContextHolder.setCustomerType(CustomerContextHolder.DATASOURCE_CIWJN);
        try {
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        json.put("ele", list.get(i).getFstandardele());
                        json.put("vol", list.get(i).getFstandardvol());
                        json.put("time", list.get(i).getFweldingtime());
                        ary.add(json);
                    } else {
                        c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(list.get(i).getFweldingtime()));
                        cb.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(list.get(i + 1).getFweldingtime()));
                        if (cb.getTimeInMillis() - c.getTimeInMillis() == 1000) {
                            json.put("ele", list.get(i).getFstandardele());
                            json.put("vol", list.get(i).getFstandardvol());
                            json.put("time", list.get(i).getFweldingtime());
                            ary.add(json);
                        } else if (cb.getTimeInMillis() - c.getTimeInMillis() == 2000) {
                            json.put("ele", list.get(i).getFstandardele());
                            json.put("vol", list.get(i).getFstandardvol());
                            json.put("time", list.get(i).getFweldingtime());
                            ary.add(json);
                            c.add(Calendar.SECOND, 1);
                            json.put("ele", 0);
                            json.put("vol", 0);
                            json.put("time", sdf.format(c.getTime()));
                            ary.add(json);
                        } else {
                            json.put("ele", list.get(i).getFstandardele());
                            json.put("vol", list.get(i).getFstandardvol());
                            json.put("time", list.get(i).getFweldingtime());
                            ary.add(json);
                            c.add(Calendar.SECOND, 1);
                            json.put("ele", 0);
                            json.put("vol", 0);
                            json.put("time", sdf.format(c.getTime()));
                            ary.add(json);
                            cb.add(Calendar.SECOND, -1);
                            json.put("ele", 0);
                            json.put("vol", 0);
                            json.put("time", sdf.format(cb.getTime()));
                            ary.add(json);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getWpsByMid")
    @ResponseBody
    public String getWpsByMid(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        JSONObject obj = new JSONObject();
        BigInteger fid = new BigInteger(request.getParameter("fid"));
        try {
            Report repo = reportService.getWps(reportService.getWpsid(fid));
            json.put("maxele", repo.getInsid());
            json.put("minele", repo.getMachid());
            json.put("maxvol", repo.getResult1());
            json.put("minvol", repo.getResult2());
            ary.add(json);
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }
}