package com.spring.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.AuthorityMapper;
import com.spring.model.Authority;
import com.spring.service.AuthorityService;

@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class AuthorityServiceImpl implements AuthorityService {
	
	@Resource
	private AuthorityMapper mapper;

	public boolean delete(int id) {
		try{
			return mapper.delete(id);
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean delete1(int id) {
		try{
			return mapper.delete1(id);
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean delete2(int id) {
		try{
			return mapper.delete2(id);
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean deleteResource(int id) {
		try{
			return mapper.deleteResource(id);
		}catch(Exception e){
			return false;
		}
	}

	public List<Authority> findAll(String str) {
		try{
			return mapper.findAll(str);
		}catch(Exception e){
			return null;
		}
	}
	
	public List<Authority> findAllResource() {
		try{
			return mapper.findAllResource();
		}catch(Exception e){
			return null;
		}
	}
	
	public List<Authority> getAllAuthoritys() {
		try{
			return mapper.getAllAuthoritys();
		}catch(Exception e){
			return null;
		}
	}
	
	public List<Authority> findResource(Integer id) {
		try{
			return mapper.findResource(id);
		}catch(Exception e){
			return null;
		}
	}

	public Authority findById(Integer id) {
		try{
			return mapper.findById(id);
		}catch(Exception e){
			return null;
		}
	}
	
	public int getAuthoritynameCount(String authorityName) {
		try{
			return mapper.getAuthoritynameCount(authorityName);
		}catch(Exception e){
			return -1;
		}
	}

	public String findByResourceId(Integer id) {
		try{
			return mapper.findByResourceId(id);
		}catch(Exception e){
			return null;
		}
		
	}
	
	public String updateAuthorityResource(Integer id) {
		try{
			return mapper.updateAuthorityResource(id);
		}catch(Exception e){
			return null;
		}
	}
	
	public boolean save(Authority authority) {
		try{
			int count = mapper.save(authority);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean saveResource(Authority authority) {
		try{
			int count = mapper.saveResource(authority);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}

	public boolean update(Authority authority) {
		try{
			return mapper.update(authority);
		}catch(Exception e){
			return false;
		}
	}
	
	public int findAuthId(String authName){
		try{
			return mapper.findAuthId(authName);
		}catch(Exception e){
			return -1;
		}
	}

}