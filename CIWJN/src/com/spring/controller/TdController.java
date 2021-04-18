package com.spring.controller;

import com.erdangjiade.spring.security.CustomerContextHolder;
import com.spring.model.*;
import com.spring.service.*;
import com.spring.util.IsnullUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/td", produces = {"text/json;charset=UTF-8"})
public class TdController {

    @Autowired
    private TdService tdService;
    @Autowired
    private LiveDataService lm;
    @Autowired
    private InsframeworkService insm;
    @Autowired
    private PersonService ps;

    @Autowired
    private WeldingMachineService wm;

    IsnullUtil iutil = new IsnullUtil();

    /**
     * 获取所有用户列表
     *
     * @param request
     * @return
     */
    @RequestMapping("/AllTdbf")
    @ResponseBody
    public String Alltdbf(HttpServletRequest request) {
        String websocket = request.getSession().getServletContext().getInitParameter("websocket");
//		request.setAttribute("web_socket", websocket);
        MyUser myuser = (MyUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        JSONObject obj = new JSONObject();
        obj.put("userName", myuser.getUsername());
        obj.put("web_socket", websocket);
        return obj.toString();
    }

    @RequestMapping("/AllTd")
    public String Alltd(HttpServletRequest request) {
/*		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		long uid = myuser.getId();
		String insname = tdService.findInsname(tdService.findIns(uid));
		request.setAttribute("insname", insname);*/
        lm.getUserId(request);
        return "td/newCurve";
    }

    @RequestMapping("/goNextcurve")
    public String goNextcurve(HttpServletRequest request) {
        lm.getUserId(request);
        String machineId = request.getParameter("machineId");//焊机id
        //String machineId = "1581";//焊机id
        String valuename = request.getParameter("valuename");//焊机编号
        String type = request.getParameter("type");
        String model = request.getParameter("model");
        String manufacture = request.getParameter("manufacture");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String starttime = "";
        String endtime = "";
        String junctionNo = "";
        //获取本周的数据表名
        //String nowTableName = JNDateUtil.getNowTableName();
        //当天临时表
        String nowTableName = "tb_temporary";
        //切换数据源
        CustomerContextHolder.setCustomerType(CustomerContextHolder.DATASOURCE_REALTIME);
        String datetime = df.format(new Date()) + " 00:00:00";//当天时间
        //String starttime = tdService.getBootTime(datetime, new BigInteger(machineId), nowTableName, "asc");
        //String endtime = tdService.getBootTime(datetime, new BigInteger(machineId), nowTableName, "desc");
        Td byRtdata = tdService.getJunctionIdByRtdata(new BigInteger(machineId), datetime, nowTableName);
        if (null != byRtdata){
            starttime = byRtdata.getStartTime();
            endtime = byRtdata.getEndTime();
            junctionNo = byRtdata.getFjunction_no();
        }
        //切换回去
        CustomerContextHolder.setCustomerType(CustomerContextHolder.DATASOURCE_CIWJN);
        request.setAttribute("machineId", machineId);
        request.setAttribute("valuename", valuename);
        request.setAttribute("type", type);
        request.setAttribute("model", model);
        request.setAttribute("starttime", starttime);//开机时间
        request.setAttribute("endtime", endtime);//关机时间
        request.setAttribute("manufacture", manufacture);
        request.setAttribute("junctionNo", junctionNo); //任务编号/名称
        return "td/nextCurve";
    }

    @RequestMapping("/AllTdd")
    public String AllTdd(HttpServletRequest request) {
        request.setAttribute("divi", request.getParameter("value"));
        return "/division";
    }

    @RequestMapping("/AllTddp")
    public String AllTddp(HttpServletRequest request) {
        request.setAttribute("proj", request.getParameter("value"));
        return "/project";
    }

    @RequestMapping("/AllTdp")
    public String AllTdp(HttpServletRequest request) {
        return "/project";
    }

    @RequestMapping("/AllTdad")
    @ResponseBody
    public String AllTdad(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        String eq = request.getParameter("e");
        String an = request.getParameter("a");
        String vo = request.getParameter("v");
        String value = request.getParameter("value");
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            String[] equ = eq.split(",");
            String[] anp = an.split(",");
            String[] vol = vo.split(",");
            System.out.println(equ);
            for (int i = 0; i < equ.length; i++) {
                if (value.equals(equ[i])) {
                    json.put("voltage", vol[i]);
                    json.put("electricity", anp[i]);
                    ary.add(json);
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();

    }

    @RequestMapping("/AllTda")
    public String AllTda(HttpServletRequest request) {
        request.setAttribute("av", request.getParameter("value"));
        return "/AV";
    }

    @RequestMapping("/getAllTd")
    @ResponseBody
    public String getAllTd(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        String da = request.getParameter("data");
        /*		System.out.println(da);*/
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (int i = 0; i < da.length(); i += 53) {
                json.put("fstatus_id", da.substring(0 + i, 2 + i));
                json.put("finsframework_id", da.substring(2 + i, 4 + i));
                json.put("fequipment_no", da.substring(4 + i, 8 + i));
                json.put("fwelder_no", da.substring(8 + i, 12 + i));
                json.put("voltage", da.substring(12 + i, 16 + i));
                json.put("electricity", da.substring(16 + i, 20 + i));
                json.put("time", da.substring(20 + i, 41 + i));
                json.put("maxele", da.substring(41 + i, 44 + i));
                json.put("minele", da.substring(44 + i, 47 + i));
                json.put("maxvol", da.substring(47 + i, 50 + i));
                json.put("minvol", da.substring(50 + i, 53 + i));
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTddiv")
    @ResponseBody
    public String getAllTddiv(HttpServletRequest request) {

        MyUser myuser = (MyUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        JSONObject obj = new JSONObject();
        long uid = myuser.getId();
        String insname = tdService.findInsname(uid);
        List<Td> findAlld = tdService.findAlldiv(tdService.findIns(uid));
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : findAlld) {
                json.put("fid", td.getFdi());
                json.put("fname", td.getFdn());
                json.put("fparent", td.getFdp());
                json.put("ftype", td.getFdt());
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTdp")
    @ResponseBody
    public String getAllTdp(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        String da = request.getParameter("data");
        System.out.println(da);
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (int i = 0; i < da.length(); i += 53) {
                json.put("fstatus_id", da.substring(0 + i, 2 + i));
                json.put("finsframework_id", da.substring(2 + i, 4 + i));
                json.put("fequipment_no", da.substring(4 + i, 8 + i));
                json.put("fwelder_no", da.substring(8 + i, 12 + i));
                String weldname = tdService.findweld(da.substring(8 + i, 12 + i));
                json.put("fname", weldname);
                json.put("voltage", da.substring(12 + i, 16 + i));
                json.put("electricity", da.substring(16 + i, 20 + i));
                json.put("time", da.substring(20 + i, 41 + i));
                json.put("maxele", da.substring(41 + i, 44 + i));
                json.put("minele", da.substring(44 + i, 47 + i));
                json.put("maxvol", da.substring(47 + i, 50 + i));
                json.put("minvol", da.substring(50 + i, 53 + i));
                String position = tdService.findPosition(da.substring(4 + i, 8 + i));
                json.put("fposition", position);
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTdp1")
    @ResponseBody
    public String getAllTdp1(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        long uid = Long.parseLong(request.getParameter("ins"));
        List<Td> findAllpro = tdService.findAlldiv(uid);
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : findAllpro) {
                json.put("fid", td.getFdi());
                json.put("fname", td.getFdn());
                json.put("fparent", td.getFdp());
                json.put("ftype", td.getFdt());
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTdp2")
    @ResponseBody
    public String getAllTdp2(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        String insname = request.getParameter("div");
        long insid = tdService.findInsid(insname);
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {

            json.put("fid", insid);
            ary.add(json);

        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTdd")
    @ResponseBody
    public String getAllTdd(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        String insname = request.getParameter("div");
        long insid = tdService.findInsid(insname);
        List<Td> findAlld = tdService.findAlldiv(insid);
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : findAlld) {
                json.put("fid", td.getFdi());
                json.put("fname", td.getFdn());
                json.put("fparent", td.getFdp());
                json.put("ftype", td.getFdt());
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTdd1")
    @ResponseBody
    public String getAllTdd1(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        List<Td> findAllcom = tdService.findAllcom();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : findAllcom) {
/*				json.put("fpname",td.getFinsp());
				json.put("fdname",td.getFinsd());
				json.put("fcname",td.getFinsc());*/
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllTdd2")
    @ResponseBody
    public String getAllTdd2(HttpServletRequest request) {

        JSONObject obj = new JSONObject();
        List<Td> findAllcom = tdService.findAllcom();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : findAllcom) {
/*				json.put("fpname",td.getFinsp());
				json.put("fdname",td.getFinsd());
				json.put("fcname",td.getFinsc());*/
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getWeld")
    @ResponseBody
    public String getWeld(HttpServletRequest request) {

        String weldid = request.getParameter("weldid");
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            json.put("fweldname", tdService.findweld(weldid));
            ary.add(json);
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getPosition")
    @ResponseBody
    public String getPosition(HttpServletRequest request) {

        String equip = request.getParameter("equip");
        String eee = tdService.findPosition(equip);
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            json.put("fpositin", eee);
            ary.add(json);
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getAllPosition")
    @ResponseBody
    public String getAllPosition(HttpServletRequest request) {
        String parentId = request.getParameter("parent");
        parentId = String.valueOf(insm.getUserInsframework());
        BigInteger parent = null;
        if (iutil.isNotEmpty(parentId)) {
            parent = new BigInteger(parentId);
        }
        List<Td> getAP = tdService.getAllPosition(parent, null);
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : getAP) {
                json.put("fid", td.getId());
                json.put("fequipment_no", td.getFequipment_no());
                json.put("fposition", td.getFposition());
                json.put("finsid", td.getFci());
                json.put("finsname", td.getFcn());
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getLiveMachine")
    @ResponseBody
    public String getLiveMachine(HttpServletRequest request) {
        String parentId = request.getParameter("parent");
        BigInteger parent = null;
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        if (iutil.isNull(parentId)) {
            parent = new BigInteger(parentId);
            List<Td> getAP = tdService.getAllPosition(parent, null);
            try {
                for (Td td : getAP) {
                    json.put("fid", td.getId());
                    json.put("fequipment_no", td.getFequipment_no());
                    json.put("fposition", td.getFposition());
                    json.put("finsid", td.getFci());
                    json.put("finsname", td.getFcn());
                    json.put("type", td.getTypeid());
                    json.put("model", td.getFpp());
                    json.put("manufacture", td.getFpt());
                    ary.add(json);
                }
            } catch (Exception e) {
                e.getMessage();
            }
        } else {
            MyUser myuser = (MyUser) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            long uid = myuser.getId();
            List<Insframework> insframework = insm.getInsByUserid(BigInteger.valueOf(uid));
            parent = insframework.get(0).getId();
            if (insframework.get(0).getType() == 20) {
                List<Td> getAP = tdService.getAllPosition(parent, null);
                try {
                    for (Td td : getAP) {
                        json.put("fid", td.getId());
                        json.put("fequipment_no", td.getFequipment_no());
                        json.put("fposition", td.getFposition());
                        json.put("finsid", td.getFci());
                        json.put("finsname", td.getFcn());
                        json.put("type", td.getTypeid());
                        ary.add(json);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            } else {
                List<Insframework> in = insm.getInsIdByParent(insm.getInsByUserid(BigInteger.valueOf(uid)).get(0).getId(), 24);
                List<Td> getAP = tdService.getAllPosition(parent, null);
                try {
                    for (Td td : getAP) {
                        for (Insframework ins : in) {
                            if (td.getFci() == Integer.valueOf(ins.getId().toString())) {
                                json.put("fid", td.getId());
                                json.put("fequipment_no", td.getFequipment_no());
                                json.put("fposition", td.getFposition());
                                json.put("finsid", td.getFci());
                                json.put("finsname", td.getFcn());
                                json.put("type", td.getTypeid());
                                ary.add(json);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
        obj.put("rows", ary);
        return obj.toString();
    }


    @RequestMapping("/getMachine")
    @ResponseBody
    public String getMachine(HttpServletRequest request) {
        String mach = request.getParameter("mach");
        String parentId = request.getParameter("parent");
        BigInteger parent = null;
        if (iutil.isNull(parentId)) {
            parent = new BigInteger(parentId);
        }
        List<Td> getAP = tdService.getMachine(new BigInteger(mach), parent);
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : getAP) {
                json.put("fid", td.getId());
                json.put("fequipment_no", td.getFequipment_no());
                json.put("fposition", td.getFposition());
                json.put("finsid", td.getFci());
                json.put("finsname", td.getFcn());
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }
	
/*	@RequestMapping("/isnull")
	@ResponseBody
	public String isnull(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		List<Td> getAP = tdService.getAllPosition();
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		try{
			for(Td td:getAP){
				json.put("fequipment_no",td.getFequipment_no());
				json.put("fposition", td.getFposition());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}*/

    @RequestMapping("/geInsname")
    @ResponseBody
    public String geInsname(HttpServletRequest request) {

        int iid = Integer.parseInt(request.getParameter("iid"));
        String insname = tdService.findInsname(iid);
        ;
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            json.put("fid", insname);
            ary.add(json);
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/allWeldname")
    @ResponseBody
    public String allWeldname(HttpServletRequest request) {

        String parentId = String.valueOf(insm.getUserInsframework());
        List<Td> fwn = tdService.allWeldname(parentId);
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            for (Td td : fwn) {
//				json.put("id", td.getId());
//				json.put("fname",td.getFname());
//				json.put("fwelder_no", td.getFwelder_no());
                ary.add(td.getId());
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }


    @RequestMapping("/getLiveWelder")
    @ResponseBody
    public String getLiveWelder(HttpServletRequest request) {
        BigInteger uid = lm.getUserId(request);
        BigInteger parent = null;
        List<Person> list = ps.findAll(parent);
        String parentId = request.getParameter("parent");
        if (iutil.isNull(parentId)) {
            parent = new BigInteger(parentId);
        } else {
            parent = insm.getUserInsfId(uid);
        }
        JSONObject obj = new JSONObject();
        JSONObject json = new JSONObject();
        JSONArray ary = new JSONArray();
        try {
            Insframework insname = insm.getInsById(parent);
            for (int i = 0; i < list.size(); i++) {
                json.put("fid", list.get(i).getId());
                json.put("fname", list.get(i).getName());
                json.put("fwelder_no", list.get(i).getWelderno());
                json.put("fitemid", list.get(i).getInsid());
                json.put("fitemname", insname.getName());
                ary.add(json);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        obj.put("rows", ary);
        return obj.toString();
    }

    @RequestMapping("/getLiveTime")
    @ResponseBody
    public String getLiveTime(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        try {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String time = sdf.format(new Date());
/*			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, 1); //得到后一天
			String totime = sdf.format(calendar.getTime());*/
            //Td list = tdService.getLiveTime(time.substring(0,11)+"00:00:00", time.substring(0,14)+"00:00", new BigInteger(request.getParameter("machineid")));
            Td list = tdService.getLiveTime(time, new BigInteger(request.getParameter("machineid")));
            WeldingMachine machinelist = wm.getWeldingMachineById(new BigInteger(request.getParameter("machineid")));
            json.put("machineno", machinelist.getTypename());
            if (list != null) {
                json.put("worktime", list.getWorktime());
                json.put("time", list.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

}