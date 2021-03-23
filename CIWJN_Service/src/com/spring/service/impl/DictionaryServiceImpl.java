package com.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.DictionaryMapper;
import com.spring.model.Dictionarys;
import com.spring.service.DictionaryService;


@Service
@Transactional
public class DictionaryServiceImpl implements DictionaryService {

	@Autowired
	private DictionaryMapper dictionaryMapper;
	
	@Override
	public List<Dictionarys> getAllDictionary(String str) {
		try{
			return dictionaryMapper.getDictionaryAll(str);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public boolean addDictionary(Dictionarys d) {
		try{
			int value= dictionaryMapper.getDictionaryMaxValue(d.getTypeid());
			d.setValue(value+1);
			int count = dictionaryMapper.addDictionary(d);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public boolean editDictionary(Dictionarys d) {
		try{
//			int value= dictionaryMapper.getDictionaryMaxValue(d.getTypeid());
//			d.setValue(value+1);
			int count = dictionaryMapper.editDictionary(d);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	@Override
	public int getDictionaryMaxValue(int typeid) {
		try{
		    return dictionaryMapper.getDictionaryMaxValue(typeid);
		}catch(Exception e){
			return -1;
		}
	}
	@Override
	public Dictionarys getDictionaryByFid(int id) {
		try{
			return dictionaryMapper.getDictionaryByFid(id);
		}catch(Exception e){
			return null;
		}
	}
	@Override
	public boolean deleteDictionary(int id) {
		try{
			int count = dictionaryMapper.deleteDictionary(id);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public List<Dictionarys> getDictionaryValue(int typeid) {
		try{
			return dictionaryMapper.getDictionaryValue(typeid);
		}catch(Exception e){
			return null;
		}
	}
	@Override
	public List<Dictionarys> getDicValueByValue(int typeid, int value) {
		try{
			return dictionaryMapper.getDicValueByValue(typeid, value);
		}catch(Exception e){
			return null;
		}
	}
	@Override
	public int getvaluebyname(int typeid,String valuename) {
		try{
			return dictionaryMapper.getvaluebyname(typeid,valuename);
		}catch(Exception e){
			return -1;
		}
	}

	@Override
	public String getDicValueByType(int value) {
		try{
			return dictionaryMapper.getDicValueByType(value);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<Dictionarys> getBack() {
		try{
			return dictionaryMapper.getBack();
		}catch(Exception e){
			return null;
		}
	}
}
