package org.xsnake.cloud.xflow.core.context;

import org.xsnake.cloud.xflow.service.api.vo.ProcessInstanceVo;

public class ProcessInstanceContext extends Context implements IXflowContext{

	protected ApplicationContext applicationContext;
	
	protected ProcessInstanceVo processInstanceVo;
	
	protected String businessForm;
	
	public ProcessInstanceContext(ApplicationContext applicationContext , ProcessInstanceVo processInstanceVo) {
		this.applicationContext = applicationContext;
		this.processInstanceVo = processInstanceVo;
	}

	@Override
	public ProcessInstanceVo getProcessInstanceVo() {
		return processInstanceVo;
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
