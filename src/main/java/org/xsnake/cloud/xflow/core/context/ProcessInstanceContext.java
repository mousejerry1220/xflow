package org.xsnake.cloud.xflow.core.context;

import org.xsnake.cloud.xflow.core.ProcessDefinition;
import org.xsnake.cloud.xflow.service.api.vo.ProcessInstance;

public class ProcessInstanceContext extends Context {

	protected ApplicationContext applicationContext;
	
	protected ProcessInstance processInstance;
	
	protected String businessForm;
	
	protected ProcessDefinition processDefinition;
	
	protected ProcessInstanceContext(){}
	public ProcessInstanceContext(ApplicationContext applicationContext , ProcessInstance processInstance , String businessForm) {
		this.applicationContext = applicationContext;
		this.processInstance = processInstance;
		this.businessForm = businessForm;
	}

	public String getBusinessForm() {
		return businessForm;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}
	
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

}
