package com.spring.service.impl;

import com.github.pagehelper.PageHelper;
import com.spring.dao.TdMapper;
import com.spring.model.Td;
import com.spring.page.Page;
import com.spring.service.TdService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional // 此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class TdServiceImpl implements TdService {

    @Resource
    private TdMapper mapper;

    public List<Td> findAll(Page page, String str) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        List<Td> findAllList = mapper.findAll(str);
        return findAllList;
    }

    public List<Td> findAllpro(long ins) {
        return mapper.findAllpro(ins);
    }

    public List<Td> findAllcom() {
        return mapper.findAllcom();
    }

    public List<Td> findAlldiv(long ins) {
        return mapper.findAlldiv(ins);
    }

    public List<Td> getAllPosition(BigInteger parent, String str) {
        return mapper.getAllPosition(parent, str);
    }

    public long findIns(long uid) {
        return mapper.findAllIns(uid);
    }

    public long findInsid(String insname) {
        return mapper.findInsid(insname);
    }

    public String findweld(String weldid) {
        return mapper.findweld(weldid);
    }

    public String findInsname(long uid) {
        return mapper.findInsname(uid);
    }

    public String findPosition(String equip) {
        return mapper.findPosition(equip);
    }

    public List<Td> allWeldname(BigInteger str) {
        return mapper.allWeldname(str);
    }

    public List<Td> getAllMachine(String position) {
        return mapper.findMachine(position);
    }

    @Override
    public List<Td> getMachine(BigInteger mach, BigInteger parent) {
        return mapper.getMachine(mach, parent);
    }

    @Override
    public Td getLiveTime(String time, BigInteger machineid) {
        return mapper.getLiveTime(time, machineid);
    }

    @Override
    public String getBootTime(String time, BigInteger machineId, String nowTableName, String orderType) {
        return mapper.getBootTime(time, machineId, nowTableName, orderType);
    }

    @Override
    public Td getJunctionIdByRtdata(BigInteger machineId, String startTime, String tableName) {
        return mapper.getJunctionIdByRtdata(machineId, startTime, tableName);
    }
}