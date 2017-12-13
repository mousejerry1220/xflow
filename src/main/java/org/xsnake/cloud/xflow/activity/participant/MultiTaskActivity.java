package org.xsnake.cloud.xflow.activity.participant;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.ParticipantActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.core.context.OperateContext;

public class MultiTaskActivity extends ParticipantActivity {

	public MultiTaskActivity(Element activityElement) {
		super(activityElement);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void definitionValidate() {
		
	}

	@Override
	public void doTask(OperateContext context) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 验证是否所有任务都已经结束
	 */
	public List<Transition> nextPaths(IXflowContext context){
		return null;
	}
}
