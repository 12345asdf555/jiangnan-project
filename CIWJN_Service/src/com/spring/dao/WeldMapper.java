package com.spring.dao;

import java.math.BigInteger;

import com.spring.model.Weld;

public interface WeldMapper {
	BigInteger FindIns_Id(Weld user);
	boolean AddWeld(Weld user);
	boolean UpdateWeld(Weld user);
	boolean AddJunction(Weld user);
	boolean UpdateJunction(Weld user);
	boolean DeleteJunction(Weld user);
}