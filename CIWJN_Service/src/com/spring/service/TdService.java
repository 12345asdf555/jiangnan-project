package com.spring.service;

import java.math.BigInteger;
import java.util.List;

import com.spring.model.Td;

public interface TdService {
	List<Td> findAll(String str);
	List<Td> findAllpro(long ins);
	List<Td> findAllcom();
	List<Td> findAlldiv(long ins);
	List<Td> getAllPosition(BigInteger parent);
	String findweld(String weldid);
	String findInsname(long uid);
	long findInsid(String insname);
	long findIns(long uid);
	String findPosition(String equip);
	List<Td> allWeldname();
	int findDic(long uid);
}