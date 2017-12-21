package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;

public final class StartActivity extends AutomaticActivity {

	public StartActivity(Element activityElement) {
		super(activityElement);
	}

	private static final long serialVersionUID = 1L;

	public List<Transition> doWork(IXflowContext context){
		return null;
	}

	@Override
	public void definitionValidate(ApplicationContext context) {
		if(toTransitionList.size() != 1){
			throw new XflowDefinitionException("开始活动有且只能有一个出口路径");
		}
	}
}
