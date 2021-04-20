package com.spring.service;

import com.spring.model.Swipe;

import java.math.BigInteger;
import java.util.List;

public interface SwipeService {
    public List<Swipe> areadata();

    public List<Swipe> groupdata(String groupid);

    public List<Swipe> machine(BigInteger groupId);

    public String signin(String sign);

    public List<Swipe> gettask();

    public List<Swipe> getmachineinfo(String fequipmentno);

    //public Swipe getmachineinfo();

    public int addtaskresult(Swipe swipe);
}
