package org.xsnake.cloud.xflow.activity.participant;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.ParticipantActivity;
import org.xsnake.cloud.xflow.core.context.OperateContext;

//可以有多个流转，人为选择走哪一个分支，把多个流转都存在任务信息中，前台读取
public class DecisionTaskActivity extends ParticipantActivity{

	private static final long serialVersionUID = 1L;

	public DecisionTaskActivity(Element activityElement) {
		super(activityElement);
	}

	@Override
	public void doTask(OperateContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void definitionValidate() {
		// TODO Auto-generated method stub
		
	}
	
}
