package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.NoNextTarget;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow.service.api.vo.ProcessInstanceVo;

public final class EndActivity extends AutomaticActivity implements NoNextTarget {

	public EndActivity(Element activityElement) {
		super(activityElement);
	}

	private static final long serialVersionUID = 1L;

	public List<Transition> doWork(IXflowContext context){
		//移除缓存的流程实例
		ProcessInstanceVo processInstanceVo = context.getProcessInstanceVo();
		if(processInstanceVo.isSub()){
			//通知父流程
			String parentId = processInstanceVo.getParentId();
			String parentActivityId = processInstanceVo.getParentActivityId();
			String definitionCode = processInstanceVo.getDefinitionCode();
			Long version = processInstanceVo.getDefinitionVersion();
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
