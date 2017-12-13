package org.xsnake.cloud.xflow.core.context;

import org.xsnake.cloud.xflow.core.ProcessDefinition;
import org.xsnake.cloud.xflow.dao.model.ProcessInstance;
import org.xsnake.cloud.xflow.dao.model.Task;

public class TaskContext extends Context implements IXflowContext{
	
	private ProcessInstanceContext processInstanceContext;
	
	private Task task;
	
	public TaskContext(ApplicationContext applicationContext , String taskId){
//		DaoCenter daoCenter = applicationContext.getDaoCenter();
//		task = daoCenter.getTaskDao().get(taskId);
		task = null;
		processInstanceContext = new ProcessInstanceContext(applicationContext,task.getProcessInstanceId());
	}

	@Override
	public ProcessInstance getProcessInstance() {
		return processInstanceContext.getProcessInstance();
	}

	@Override
	public ProcessDefinition getProcessDefinition() {
		return processInstanceContext.getProcessDefinition();
	}

	@Override
	public String getBusinessInfo() {
		return processInstanceContext.getBusinessInfo();
	}

	public ProcessInstanceContext getXflowContext() {
		return processInstanceContext;
	}

	public Task getTask() {
		return task;
	}

	@Override
	public ProcessInstanceContext getProcessInstanceContext() {
		return processInstanceContext;
	}

	@Override
	public TaskContext getTaskContext() {
		return this;
	}

	@Override
	public ApplicationContext getApplicationContext() {
		return processInstanceContext.getApplicationContext();
	}
	
}
