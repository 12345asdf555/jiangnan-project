package com.spring.dao;


import com.spring.model.Insframework;
import com.spring.model.Swipe;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigInteger;
import java.util.List;

public interface SwipeMapper extends Mapper<Insframework> {
    /*查询区接口*/
    public List<Swipe> areadata();

    /*查询班组接口*/
    public List<Swipe> groupdata(String groupid);

    /*机器显示信息*/
    public List<Swipe> machine(@Param("groupId") BigInteger groupId);

    /*查询次页焊机区组下的未绑定焊机*/
    public List<Swipe> machinec(@Param("groupId") BigInteger groupId);

    /*焊工登录入口*/
    public String signin(String sign);

    /*任务记录*/
    public List<Swipe> gettask();

    /*焊机使用信息*/
    public List<Swipe> getmachineinfo(String fequipmentno);

    //public Swipe getmachineinfo();

    /*焊工焊机任务绑定*/
    public int addtaskresult(Swipe swipe);

}
