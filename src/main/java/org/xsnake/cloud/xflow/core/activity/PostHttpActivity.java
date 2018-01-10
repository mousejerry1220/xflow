package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.ProcessInstanceContext;

public class PostHttpActivity extends AutomaticActivity {

	public PostHttpActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public List<Transition> doWork(ProcessInstanceContext context){
		return null;
	}

	@Override
	public void definitionValidate(ApplicationContext context) {
		
	}

}
