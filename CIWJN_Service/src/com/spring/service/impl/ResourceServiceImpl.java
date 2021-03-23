package com.spring.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.ResourceMapper;
import com.spring.model.Resources;
import com.spring.service.ResourceService;



@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class ResourceServiceImpl implements ResourceService {
	
	@Resource
	private ResourceMapper mapper;

	public boolean delete(int id) {
		try{
			return mapper.delete(id);
		}catch(Exception e){
			return false;
		}
	}

	public List<Resources> findAll(String str) {
		try{
			return mapper.findAll(str);
		}catch(Exception e){
			return null;
		}
	}
	
	public List<String> getResourcesByAuthName(String authName) {
		try{
			return mapper.getResourcesByAuthName(authName);
		}catch(Exception e){
			return null;
		}
	}
	
	public int getResourcenameCount(String resourceName) {
		try{
			return mapper.getResourcenameCount(resourceName);
		}catch(Exception e){
			return -1;
		}
	}

	public Resources findById(Integer id) {
		try{
			return  mapper.findById(id);
		}catch(Exception e){
			return null;
		}
	}

	public boolean save(Resources resource) {
		try{
			int count = mapper.save(resource);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}

	public boolean update(Resources resource) {
		try{
			return mapper.update(resource);
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public List<String> getAuthByRes(String url) {
		try{
			return mapper.getAuthByRes(url);
		}catch(Exception e){
			return null;
		}
	}

}