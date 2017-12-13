package org.xsnake.cloud.xflow.activity.participant;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.ParticipantActivity;
import org.xsnake.cloud.xflow.core.context.OperateContext;

public class TaskActivity extends ParticipantActivity{
	
	public TaskActivity(Element activityElement) {
		super(activityElement);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void definitionValidate() {
		
	}

	@Override
	public void doTask(OperateContext context) {
		
	}


}
