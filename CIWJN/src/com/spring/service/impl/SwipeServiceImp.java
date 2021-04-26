package com.spring.service.impl;

import com.spring.dao.SwipeMapper;
import com.spring.model.Swipe;
import com.spring.service.SwipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SwipeServiceImp implements SwipeService {

    @Autowired
    SwipeMapper swipeMapper;

    @Override
    public List<Swipe> areadata() {
        return swipeMapper.areadata();
    }

    @Override
    public List<Swipe> groupdata(String groupid){
        return swipeMapper.groupdata(groupid);
    }

    @Override
    public List<Swipe> machine(BigInteger groupId){
        return swipeMapper.machine(groupId);
    }

    @Override
    public List<Swipe> machinec(BigInteger groupId){
        return swipeMapper.machinec(groupId);
    }

    @Override
    public String signin(String sign){
        return swipeMapper.signin(sign);
    }

    @Override
    public List<Swipe> gettask(){
        return swipeMapper.gettask();
    }

    @Override
    public List<Swipe> getmachineinfo(String fequipmentno){
        return swipeMapper.getmachineinfo(fequipmentno);
    }

    /*public Swipe getmachineinfo(){
        return swipeMapper.getmachineinfo();
    }*/

    @Override
    public int addtaskresult(Swipe swipe){
        return swipeMapper.addtaskresult(swipe);
    }

}