package org.xsnake.cloud.xflow.core.context;

import org.xsnake.cloud.xflow.service.api.vo.ProcessInstance;
import org.xsnake.cloud.xflow.service.api.vo.Task;

public class TaskContext extends Context implements IXflowContext{
	
	private ProcessInstanceContext processInstanceContext;
	
	private Task task;
	
	public TaskContext(ApplicationContext applicationContext , String taskId){
		task = null;
//		processInstanceContext = new ProcessInstanceContext(applicationContext,task.getProcessInstanceId());
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

	@Override
	public String getBusinessForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstance getProcessInstance() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
