package org.xsnake.cloud.xflow.core.context;

import org.xsnake.cloud.xflow.service.api.vo.ProcessInstance;

public class ProcessInstanceContext extends Context implements IXflowContext{

	protected ApplicationContext applicationContext;
	
	protected ProcessInstance processInstance;
	
	protected String businessForm;
	
	public ProcessInstanceContext(ApplicationContext applicationContext , ProcessInstance processInstance , String businessForm) {
		this.applicationContext = applicationContext;
		this.processInstance = processInstance;
		this.businessForm = businessForm;
	}

	@Override
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public String getBusinessForm() {
		return businessForm;
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
