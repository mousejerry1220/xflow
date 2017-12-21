package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.Waitable;
import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.service.api.vo.ProcessInstanceVo;

public class SynSubProcessActivity extends SubProcessActivity implements Waitable {

	private static final long serialVersionUID = 1L;
	
	public SynSubProcessActivity(Element activityElement) {
		super(activityElement);
	}

	@Override
	public List<Transition> doWork(ProcessInstanceVo subProcessInstanceVo , IXflowContext context) {
		if(subProcessInstanceVo.isEnd()){
			return toTransitionList;
		}
		return null;
	}

}
