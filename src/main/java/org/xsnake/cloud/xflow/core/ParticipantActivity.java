package org.xsnake.cloud.xflow.core;

import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.core.context.OperateContext;

public abstract class ParticipantActivity extends Activity{

	private static final long serialVersionUID = 1L;
	
	//该属性是设置如果参与者为空则自动完成任务
	public final static String NONE_PARTICIPANT_AUTOCOMPLETE = "NONE_PARTICIPANT_AUTOCOMPLETE";
	
	public abstract void createTask(IXflowContext context);
	
	public abstract void doTask(OperateContext context);

	//从人工参与环节进来的请求都为人为操作的，其上线文必为OperateContext
	@Override
	public final void doWork(IXflowContext context) {
		doTask((OperateContext)context);
	}
		
}
