package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.service.api.vo.ProcessInstance;

/**
 * 异步子流程
 * @author Jerry.Zhao
 *
 */
public class AsynSubProcessActivity extends SubProcessActivity {

	private static final long serialVersionUID = 1L;
	
	public AsynSubProcessActivity(Element activityElement) {
		super(activityElement);
	}

	@Override
	public List<Transition> doWork(ProcessInstance subProcessInstance, IXflowContext context) {
		return toTransitionList;
	}
	
}
