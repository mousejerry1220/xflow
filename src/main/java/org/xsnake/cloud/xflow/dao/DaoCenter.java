package org.xsnake.cloud.xflow.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DaoCenter {
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	ProcessInstanceDao processInstanceDao;

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public ProcessInstanceDao getProcessInstanceDao() {
		return processInstanceDao;
	}
	
}
