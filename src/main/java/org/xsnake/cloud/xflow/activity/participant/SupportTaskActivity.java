package org.xsnake.cloud.xflow.activity.participant;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.NoNextTarget;
import org.xsnake.cloud.xflow.core.ParticipantActivity;
import org.xsnake.cloud.xflow.core.context.OperateContext;

/**
 * 非定义的节点，因为他并发定义的节点所以不会影响到流程的走向，所以重写了他的nextPaths方法，让其总是返回null
 * @author Administrator
 *
 */

public class SupportTaskActivity extends ParticipantActivity implements NoNextTarget{

	public SupportTaskActivity(Element activityElement) {
		super(activityElement);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void definitionValidate() {
		//非定义的虚拟节点，不需要任何实现
	}

	@Override
	public void doTask(OperateContext context) {
		
	}
}
