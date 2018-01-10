package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.xsnake.cloud.xflow.core.ParticipantActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.VirtualNode;
import org.xsnake.cloud.xflow.core.context.OperateContext;

/**
 * 2018/1/15
 * 非定义的节点，因为他并发定义的节点所以不会影响到流程的走向，所以重写了他的nextPaths方法，让其总是返回null
 * @author Jerry.Zhao
 *
 */
public class SupportTaskActivity extends ParticipantActivity implements VirtualNode{

	private static final long serialVersionUID = 1L;
	
	public SupportTaskActivity() {
		this.id = "SUPPORT";
		this.name = "SUPPORT";
		this.type = "SUPPORT";
	}

	@Override
	public List<Transition> doTask(OperateContext context) {
		return null;
	}
}
