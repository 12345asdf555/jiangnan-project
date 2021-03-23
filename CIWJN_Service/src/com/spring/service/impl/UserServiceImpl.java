package com.spring.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.UserMapper;
import com.spring.model.User;
import com.spring.service.UserService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class UserServiceImpl implements UserService {
	
	@Resource
	private UserMapper mapper;

	public boolean delete(int id) {
		try{
			return mapper.delete(id);
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean deleteRole(int id) {
		try{
			return mapper.deleteRole(id);
		}catch(Exception e){
			return false;
		}
	}
	
	public int getUsernameCount(String userName) {
		try{
			return mapper.getUsernameCount(userName);
		}catch(Exception e){
			return -1;
		}
	}

	public List<User> findAll(BigInteger parent,String str) {
		try{
			return mapper.findAll(parent,str);
		}catch(Exception e){
			return null;
		}
	}
	
	public List<User> findAllRole() {
		try{
			return mapper.findAllRole();
		}catch(Exception e){
			return null;
		}
	}
	
	public int findByName(String name){
		try{
			return mapper.findByName(name);
		}catch(Exception e){
			return -1;
		}
	}
	
	public List<User> getIns(BigInteger parent) {
		try{
			return mapper.getIns(parent);
		}catch(Exception e){
			return null;
		}
	}
	
	public List<String> getAuthoritiesByUsername(String userName) {
		try{
			return mapper.getAuthoritiesByUsername(userName);
		}catch(Exception e){
			return null;
		}
	}

	public User findById(Integer id) {
		try{
			return mapper.findById(id);
		}catch(Exception e){
			return null;
		}
	}
	
	public User LoadUser(String userName) {
		try{
			return mapper.LoadUser(userName);
		}catch(Exception e){
			return null;
		}
	}
	
	public String findByRoleId(Integer id) {
		try{
			return mapper.findByRoleId(id);
		}catch(Exception e){
			return null;
		}
	}

	public List<User> findRole(Integer id) {
		try{
			return mapper.findRole(id);
		}catch(Exception e){
			return null;
		}
	}
	
	public boolean save(User user) {
		try{
			int count = mapper.save(user);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean saveRole(User user) {
		try{
			int count = mapper.saveRole(user);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}

	public boolean update(User user) {
		try{
			return mapper.update(user);
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public String updateUserRole(Integer findByRoleId) {
		try{
			return mapper.updateUserRole(findByRoleId);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public User getUserInsframework(BigInteger id) {
		try{
			return mapper.getUserInsframework(id);
		}catch(Exception e){
			return null;
		}
	}
	
	public List<User> getInsUser(int ins) {
		try{
			return mapper.getInsUser(ins);
		}catch(Exception e){
			return null;
		}
	}

}