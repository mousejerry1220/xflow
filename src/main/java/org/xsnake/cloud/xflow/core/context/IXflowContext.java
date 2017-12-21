package org.xsnake.cloud.xflow.core.context;

import org.xsnake.cloud.xflow.service.api.vo.ProcessInstanceVo;

public interface IXflowContext extends IContext {

	ProcessInstanceVo getProcessInstanceVo();

	String getBusinessForm();
	
	ProcessInstanceContext getProcessInstanceContext();
	
	TaskContext getTaskContext();
	
	ApplicationContext getApplicationContext();
	
}
