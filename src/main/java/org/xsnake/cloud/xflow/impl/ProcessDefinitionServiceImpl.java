package org.xsnake.cloud.xflow.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.xsnake.cloud.dao.DaoTemplate;
import org.xsnake.cloud.xflow.api.IProcessDefinitionService;

public class ProcessDefinitionServiceImpl implements IProcessDefinitionService{
	
	@Autowired
	DaoTemplate daoTemplate;
	
	
}
