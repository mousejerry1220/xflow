package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.ProcessInstanceContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;

public class ForkActivity extends AutomaticActivity {

	public ForkActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public List<Transition> doWork(ProcessInstanceContext context){
		return toTransitionList;
	}

	@Override
	public void definitionValidate(ApplicationContext context) {
		if(toTransitionList.size() < 2){
			throw new XflowDefinitionException("分支节点需要包含多个出口路径");
		}
	}
}
