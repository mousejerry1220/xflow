package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.NoNextTarget;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow.service.api.vo.ProcessInstance;

public final class EndActivity extends AutomaticActivity implements NoNextTarget {

	public EndActivity(Element activityElement) {
		super(activityElement);
	}

	private static final long serialVersionUID = 1L;

	public List<Transition> doWork(IXflowContext context){
		//移除缓存的流程实例
		ProcessInstance processInstance = context.getProcessInstance();
		if(processInstance.isSub()){
			//通知父流程
			String parentId = processInstance.getParentId();
			String parentActivityId = processInstance.getParentActivityId();
			String definitionCode = processInstance.getDefinitionCode();
			Long version = processInstance.getDefinitionVersion();
		}
		return null;
	}
	
	@Override
	public void definitionValidate(ApplicationContext context) {
		if(toTransitionList !=null && toTransitionList.size() > 0){
			throw new XflowDefinitionException("结束节点不能包含任何流转路径");
		}
	}
	
}
