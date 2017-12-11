package org.xsnake.cloud.xflow.activity;

import java.util.List;

import org.xsnake.cloud.xflow.core.ParticipantActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.Waitable;
import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.core.context.OperateContext;

/**
 * 非定义的节点，因为他并发定义的节点所以不会影响到流程的走向，所以重写了他的nextPaths方法，让其总是返回null
 * @author Administrator
 *
 */

public class SupportActivity extends ParticipantActivity implements Waitable{

	private static final long serialVersionUID = 1L;

	@Override
	public void createTask(IXflowContext context) {
		//先从上下文中取出需要支持的参与者列表，然后循环创建任务
		
	}

	@Override
	public void definitionValidate() {
		//非定义的虚拟节点，不需要任何实现
	}
 
	public List<Transition> nextPaths(IXflowContext context){
		return null;
	}

	@Override
	public void doTask(OperateContext context) {
		
	}
}
