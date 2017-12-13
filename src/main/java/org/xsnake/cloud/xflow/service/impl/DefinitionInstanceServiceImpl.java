package org.xsnake.cloud.xflow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.xsnake.cloud.xflow.dao.DaoTemplate;
import org.xsnake.cloud.xflow.service.api.IDefinitionInstanceService;

public class DefinitionInstanceServiceImpl implements IDefinitionInstanceService{

	@Autowired
	DaoTemplate daoTemplate;

	@Override
	public void create(String code, String xml) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String code, int version, String xml) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void release(String code, int version) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String code, int version) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCurrentDefinitionXML(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefinitionXML(String code, int version) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
