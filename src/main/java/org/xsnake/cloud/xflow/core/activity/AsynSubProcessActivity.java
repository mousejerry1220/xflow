package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.ProcessInstanceContext;
import org.xsnake.cloud.xflow.service.api.vo.ProcessInstance;

/**
 * 2018/1/15
 * 异步子流程
 * @author Jerry.Zhao
 *
 */
public class AsynSubProcessActivity extends SubProcessActivity {

	private static final long serialVersionUID = 1L;
	
	public AsynSubProcessActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	@Override
	public List<Transition> doWork(List<ProcessInstance> subProcessInstanceList, ProcessInstanceContext context) {
		return toTransitionList;
	}
	
}
