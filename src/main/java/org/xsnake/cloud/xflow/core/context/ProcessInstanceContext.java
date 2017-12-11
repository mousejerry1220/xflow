package org.xsnake.cloud.xflow.core.context;

import org.xsnake.cloud.xflow.core.ProcessDefinition;
import org.xsnake.cloud.xflow.dao.model.ProcessInstance;

public class ProcessInstanceContext extends Context implements IXflowContext{

	protected ApplicationContext applicationContext;
	
	protected ProcessInstance processInstance;
	
	protected ProcessDefinition processDefinition;
	
	protected String businessInfo;
	
	protected ProcessInstanceContext(ApplicationContext applicationContext ,String processInstanceId) {
		
	}

	@Override
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	@Override
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	@Override
	public String getBusinessInfo() {
		return businessInfo;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public ProcessInstanceContext getProcessInstanceContext() {
		return this;
	}

	//在这里总是返回null
	@Override
	public TaskContext getTaskContext() {
		return null;
	}
	
}
