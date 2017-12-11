package org.xsnake.cloud.xflow.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.xsnake.cloud.dao.DaoTemplate;
import org.xsnake.cloud.xflow.dao.model.Task;

public class TaskDao {
	
	@Autowired
	DaoTemplate daoTemplate;
	
	public void save(Task task){
		
	}
	
	public void delete(String taskId){
		
	}
	
	public void update(Task task){
		
	}
	
	public Task get(String taskId){
		return null;
	}
	
}
