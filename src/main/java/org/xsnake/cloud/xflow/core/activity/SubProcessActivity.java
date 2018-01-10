package org.xsnake.cloud.xflow.core.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.ProcessInstanceContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow.service.api.Participant;
import org.xsnake.cloud.xflow.service.api.vo.ProcessInstance;
import org.xsnake.cloud.xflow.service.impl.ProcessInstanceServiceImpl;

public abstract class SubProcessActivity extends AutomaticActivity {

	private static final long serialVersionUID = 1L;
	
	final List<String> definitionCodeList = new ArrayList<String>();
	
	public SubProcessActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
		try{
			List<Element> definitionCodes = activityElement.element("subProcess").elements("code");
			for(Element e : definitionCodes){
				if(StringUtils.isNotEmpty(e.getText())){
					definitionCodeList.add(e.getText());
				}
			}
		}catch (Exception e) {
			throw new XflowDefinitionException("子流程必须包含节点subProcess -> code");
		}
		
		if(definitionCodeList.size() == 0){
			throw new XflowDefinitionException("子流程必须描述需要启动的流程代码");
		}
	}

	@Override
	public void definitionValidate(ApplicationContext context) {
		
	}

	@Override
	public final List<Transition> doWork(ProcessInstanceContext context) {

		List<ProcessInstance> subProcessInstanceList = new ArrayList<ProcessInstance>();
		//逐个开启子流程，并存入到子流程实例列表
		for(String definitionCode : definitionCodeList){
			ProcessInstanceServiceImpl pis = (ProcessInstanceServiceImpl)context.getApplicationContext().getProcessInstanceService();
			ProcessInstance processInstance = pis.start(
					definitionCode,
					context.getProcessInstance().getBusinessKey(),
					context.getBusinessForm(),
					new Participant(
							context.getProcessInstance().getCreatorId(),
							context.getProcessInstance().getCreatorName(),
							context.getProcessInstance().getCreatorType()
					),
					context.getProcessInstance().getId(),
					id);
			subProcessInstanceList.add(processInstance);
		}
		return doWork(subProcessInstanceList , context);
	}
	
	public abstract List<Transition> doWork(List<ProcessInstance> subProcessInstanceList , ProcessInstanceContext context);
}
