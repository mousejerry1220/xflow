package org.xsnake.cloud.xflow.dao.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.xflow.dao.DaoTemplate;

@Component
@CacheConfig(cacheNames="task")
public class TaskRepository {
	
//	@Cacheable(key = "#id" )
//	public Task findTask(String id) {
//		Task t = new Task();
//		t.setId(id);
//		return t;
//	}
	
	@Autowired
	DaoTemplate daoTemplate;

	public void removeAllTask(String processInstanceId) {
		//TODO 按照流程ID删除所有相关的任务
	}
}
