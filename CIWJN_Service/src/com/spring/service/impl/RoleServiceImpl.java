package com.spring.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dao.RoleMapper;
import com.spring.model.Role;
import com.spring.service.RoleService;

@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class RoleServiceImpl implements RoleService {
	
	@Resource
	private RoleMapper mapper;

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
	
	public boolean delete2(String roleName) {
		try{
			return mapper.delete2(roleName);
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean deleteAuthority(int id) {
		try{
			return mapper.deleteAuthority(id);
		}catch(Exception e){
			return false;
		}
	}
	
	public int getRolenameCount(String roleName) {
		try{
			return mapper.getRolenameCount(roleName);
		}catch(Exception e){
			return -1;
		}
	}
	
	public boolean deleteUser(int id) {
		try{
			return mapper.deleteUser(id);
		}catch(Exception e){
			return false;
		}
	}

	public List<Role> findAll(String str) {
		try{
			return  mapper.findAll(str);
		}catch(Exception e){
			return null;
		}
	}

	public Role findById(int id) {
		try{
			return mapper.findById(id);
		}catch(Exception e){
			return null;
		}
	}
	
	public String findByAuthorityId(Integer id) {
		try{
			return mapper.findByAuthorityId(id);
		}catch(Exception e){
			return null;
		}
	}
	
	public String findByUserId(Integer id) {
		try{
			return mapper.findByUserId(id);
		}catch(Exception e){
			return null;
		}
	}
	
	public String updateRoleAuthority(Integer findByAuthorityId) {
		try{
			return mapper.updateRoleAuthority(findByAuthorityId);
		}catch(Exception e){
			return null;
		}
	}
	
	public String updateRoleUser(Integer findByUserId) {
		try{
			return mapper.updateRoleUser(findByUserId);
		}catch(Exception e){
			return null;
		}
	}

	public boolean save(Role role) {
		try{
			int count = mapper.save(role);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean saveAuthority(Role Role) {
		try{
			int count = mapper.saveAuthority(Role);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean saveUser(Role Role) {
		try{
			int count = mapper.saveUser(Role);
			if(count>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}

	public boolean update(Role role) {
		try{
			return mapper.update(role);
		}catch(Exception e){
			return false;
		}
	}
	
	public List<Role> findAllAuthority() {
		try{
			return mapper.findAllAuthority();
		}catch(Exception e){
			return null;
		}
	}
	
	public List<Role> findAuthority(Integer id) {
		try{
			return mapper.findAuthority(id);
		}catch(Exception e){
			return null;
		}
	}
	
	public List<Role> findAllUser() {
		try{
			return mapper.findAllUser();
		}catch(Exception e){
			return null;
		}
	}
	
	public List<Role> findUser(Integer id) {
		try{
			return mapper.findUser(id);
		}catch(Exception e){
			return null;
		}
	}
	

}