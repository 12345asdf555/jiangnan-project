package com.spring.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.TdMapper;
import com.spring.model.Td;
import com.spring.service.TdService;

	@Service
	@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
	public class TdServiceImpl implements TdService {
		
		@Resource
		private TdMapper mapper;

		public List<Td> findAll(String str) {
			try{
			return mapper.findAll(str);
			}catch(Exception e){
				return null;
			}
		}
		
		public List<Td> findAllpro(long ins){
			try{
				return mapper.findAllpro(ins);
			}catch(Exception e){
				return null;
			}
		}
		
		public List<Td> findAllcom(){
			try{
				return mapper.findAllcom();
			}catch(Exception e){
				return null;
			}
		}
		
		public List<Td> findAlldiv(long ins){
			try{
				return mapper.findAlldiv(ins);
			}catch(Exception e){
				return null;
			}
		}
		
		public List<Td> getAllPosition(BigInteger parent){
			try{
				return mapper.getAllPosition(parent);
			}catch(Exception e){
				return null;
			}
		}
		
		public long findIns(long uid){
			try{
				return mapper.findAllIns(uid);
			}catch(Exception e){
				return -1;
			}
		}
		
		public long findInsid(String insname){
			try{
				return mapper.findInsid(insname);
			}catch(Exception e){
				return -1;
			}
		}
		
		public String findweld(String weldid){
			try{
				return mapper.findweld(weldid);	
			}catch(Exception e){
				return null;
			}
		}
		
		public String findInsname(long uid){
			try{
				return mapper.findInsname(uid);		
			}catch(Exception e){
				return null;
			}
		}
		
		public String findPosition(String equip){
			try{
				return mapper.findPosition(equip);
			}catch(Exception e){
				return null;
			}
		}
		
		public List<Td> allWeldname(){
			try{
				return mapper.allWeldname();
			}catch(Exception e){
				return null;
			}
		}
		
		public int findDic(long uid){
			try{
				return mapper.findDic(uid);
			}catch(Exception e){
				return -1;
			}
		}
	}