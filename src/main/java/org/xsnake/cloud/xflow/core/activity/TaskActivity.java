package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.ParticipantActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.OperateContext;

public class TaskActivity extends ParticipantActivity{
	
	public TaskActivity(Element activityElement) {
		super(activityElement);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void definitionValidate(ApplicationContext context) {
		
	}

	@Override
	public List<Transition> doTask(OperateContext context) {
		return null;
	}


}
