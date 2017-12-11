package org.xsnake.cloud.xflow.core.context;

import org.xsnake.cloud.xflow.core.ProcessDefinition;
import org.xsnake.cloud.xflow.dao.model.ProcessInstance;

public interface IXflowContext extends IContext {

	ProcessInstance getProcessInstance();

	ProcessDefinition getProcessDefinition();

	String getBusinessInfo();
	
	ProcessInstanceContext getProcessInstanceContext();
	
	TaskContext getTaskContext();
	
	ApplicationContext getApplicationContext();
	
}
