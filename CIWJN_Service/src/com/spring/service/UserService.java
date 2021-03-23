package com.spring.service;

import java.math.BigInteger;
import java.util.List;

import com.spring.model.User;

public interface UserService {
	boolean save(User user);
	boolean saveRole(User user);
	boolean update(User user);
	boolean delete(int id);
	boolean deleteRole(int id);
	User findById(Integer id);
	String findByRoleId(Integer id);
	int findByName(String name);
	List<User> findAll(BigInteger parent,String str);
	List<User> findRole(Integer id);
	List<User> findAllRole();
	String updateUserRole(Integer findByRoleId);
	int getUsernameCount(String userName);
	User LoadUser(String userName);
	List<String> getAuthoritiesByUsername(String userName);
	List<User> getIns(BigInteger parent);
	User getUserInsframework(BigInteger id);
	List<User> getInsUser(int ins);
}