package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.ParticipantActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.OperateContext;

//可以有多个流转，人为选择走哪一个分支，把多个流转都存在任务信息中，前台读取
public class DecisionTaskActivity extends ParticipantActivity{

	private static final long serialVersionUID = 1L;

	public DecisionTaskActivity(Element activityElement) {
		super(activityElement);
	}

	@Override
	public List<Transition> doTask(OperateContext context) {
		return null;
	}

	@Override
	public void definitionValidate(ApplicationContext context) {
		
	}
	
}
