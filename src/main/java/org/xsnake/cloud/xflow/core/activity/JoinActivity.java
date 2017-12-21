package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.Waitable;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.IXflowContext;

public class JoinActivity extends AutomaticActivity implements Waitable{

	public JoinActivity(Element activityElement) {
		super(activityElement);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public List<Transition> doWork(IXflowContext context){
		return null;
	}

	@Override
	public void definitionValidate(ApplicationContext context) {
		
	}
	
}
