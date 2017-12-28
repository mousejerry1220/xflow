package org.xsnake.cloud.xflow.core.context;

import org.xsnake.cloud.xflow.service.api.vo.ProcessInstance;

public interface IXflowContext extends IContext {

	ProcessInstance getProcessInstance();

	String getBusinessForm();
	
	ProcessInstanceContext getProcessInstanceContext();
	
	TaskContext getTaskContext();
	
	ApplicationContext getApplicationContext();
	
}
