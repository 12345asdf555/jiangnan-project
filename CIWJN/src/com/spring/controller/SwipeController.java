package com.spring.controller;

import com.spring.model.Swipe;
import com.spring.service.SwipeService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.List;

@CrossOriginResourceSharing
@Controller
@RequestMapping(value = "/swipe", produces = {"text/json;charset=UTF-8"})
public class SwipeController {

    @Autowired
    SwipeService swipeService;

    @RequestMapping("/areadata")
    @ResponseBody
    public String areadata(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");

        List<Swipe> area = swipeService.areadata();

        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();

        for (Swipe insfr : area) {
            json.put("id", insfr.getFid());
            json.put("name", insfr.getFname());
            array.add(json);
        }
        obj.put("ary", array);
        return obj.toString();
    }


    @RequestMapping("/groupdata")
    @ResponseBody
    public String group(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");

        String groupid = request.getParameter("groupid");
        List<Swipe> group = swipeService.groupdata(groupid);
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        for (Swipe insfr : group) {
            json.put("id", insfr.getFid());
            json.put("name", insfr.getFname());
            array.add(json);
        }
        obj.put("ary", array);
        return obj.toString();
    }

    @RequestMapping("/machine")
    @ResponseBody
    public String machine(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");

        String groupId = request.getParameter("groupid");
        BigInteger finsframeworkid = BigInteger.ZERO;
        if (groupId != null) {
            finsframeworkid = new BigInteger(groupId);
        }
        List<Swipe> machine = swipeService.machine(finsframeworkid);
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        for (Swipe insfr : machine) {
            json.put("id", insfr.getFid());
            json.put("fmanufacturerid", insfr.getFmanufacturerId());
            json.put("ftypeid", insfr.getFtypeId());
            json.put("fequipmentNo",insfr.getFequipmentNo());
            array.add(json);
        }
        obj.put("ary", array);
        return obj.toString();
    }



    @RequestMapping("/signin")
    @ResponseBody
    public String signin(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");

        String sign = request.getParameter("login");
        String signindata = swipeService.signin(sign);
        System.out.println(signindata);

        /*JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();

        json.put("signindata",signindata);
        array.add(json);
        obj.put("signindata",array);*/

        return signindata;
    }

    @RequestMapping("/task")
    @ResponseBody
    public String gettask(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");

        List<Swipe> taskdata = swipeService.gettask();
        System.out.println("222222222222222222");
        System.out.println(taskdata);

        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        for (Swipe insfr : taskdata) {
            json.put("fno", insfr.getFweldedJunctionNo());
            json.put("weldedjunctionid",insfr.getFid());

            if (insfr.getFoperatetype() != null) {
                json.put("foperatetype", insfr.getFoperatetype());
            } else {
                json.put("foperatetype", 2);
            }
            array.add(json);
        }
        obj.put("ary", array);
        return obj.toString();
    }

    @RequestMapping("/machinedata")
    @ResponseBody
    public String machineinfo(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        String fequipmentno=request.getParameter("fequipmentno");

        List<Swipe> machinedata = swipeService.getmachineinfo(fequipmentno);
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        for (Swipe data : machinedata) {
            json.put("fequipmentNo", data.getFequipmentNo());
            json.put("finsframeworkId", data.getFinsframeworkId());
            json.put("fmodel", data.getFmodel());
            json.put("fwelderNo", data.getFwelderNo());
            json.put("fname", data.getFname());
            json.put("fweldedJunctionNo", data.getFweldedJunctionNo());
            json.put("foperator", data.getFoperator());
            array.add(json);
        }
        obj.put("ary", array);
        return obj.toString();

        /*Swipe swipe=new Swipe();

        swipe=swipeService.getmachineinfo();

        return swipe;*/

    }

    @RequestMapping("/addtask")
    @ResponseBody
    public String addtaskresult(HttpServletRequest request,HttpServletResponse response,Swipe swipe){
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");

        int addtaskresult = swipeService.addtaskresult(swipe);
        if (addtaskresult != 0){
            return "true";
        }
        return "false";
    }

}
