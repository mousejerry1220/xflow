package org.xsnake.cloud.xflow.core.context;

import org.xsnake.cloud.xflow.service.api.vo.Task;

public class TaskContext extends ProcessInstanceContext {
	
	private Task task;
	
	public TaskContext(ApplicationContext applicationContext , String taskId){
		task = null;
		//判断任务状态是否运行中
//		DaoTemplate daoTemplate = applicationContext.getDaoTemplate();
		
	}

	public Task getTask() {
		return task;
	}
	
}
