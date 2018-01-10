package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.Activity;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.ProcessDefinition;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.ProcessInstanceContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow.service.api.vo.ProcessInstance;

public final class EndActivity extends AutomaticActivity {

	public EndActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	private static final long serialVersionUID = 1L;

	public List<Transition> doWork(ProcessInstanceContext context){
		//移除缓存的流程实例
		ProcessInstance processInstance = context.getProcessInstance();
		if(processInstance.isSub()){
			wakeUp(context);
		}
		
		return null;
	}
	
	@Override
	public void definitionValidate(ApplicationContext context) {
		if(toTransitionList !=null && toTransitionList.size() > 0){
			throw new XflowDefinitionException("结束节点不能包含任何流转路径");
		}
	}
	
	private void wakeUp(ProcessInstanceContext context){
		//通知父流程
		ProcessInstance processInstance = context.getProcessInstance();
		String parentId = processInstance.getParentId();
		String parentActivityId = processInstance.getParentActivityId();
		String definitionCode = processInstance.getDefinitionCode();
		Long version = processInstance.getDefinitionVersion();
		ProcessInstance parentProcessInstance = context.getApplicationContext().getDaoTemplate().queryForObject(" SELECT * FROM XFLOW_PROCESS_INSTANCE WHERE ROW_ID = ? ", new Object[]{parentId}, ProcessInstance.class);
		ProcessInstanceContext parentProcessInstanceContext = new ProcessInstanceContext(context.getApplicationContext(),parentProcessInstance,context.getBusinessForm());
		String xml = context.getApplicationContext().getDefinitionInstanceXMLRepository().getXML(definitionCode, version);
		try {
			ProcessDefinition processDefinition = ProcessDefinition.parse(context.getApplicationContext(), definitionCode, xml);
			Activity activity = processDefinition.getActivity(parentActivityId);
			activity.process(parentProcessInstanceContext);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
	
}
