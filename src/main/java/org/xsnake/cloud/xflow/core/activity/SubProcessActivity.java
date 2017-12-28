package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow.service.api.Participant;
import org.xsnake.cloud.xflow.service.api.vo.ProcessInstance;
import org.xsnake.cloud.xflow.service.impl.ProcessInstanceServiceImpl;

public abstract class SubProcessActivity extends AutomaticActivity {

	private static final long serialVersionUID = 1L;
	
	String definitionCode;
	
	public SubProcessActivity(Element activityElement) {
		super(activityElement);
		definitionCode = attributes.get("definitionCode");
	}

	@Override
	public void definitionValidate(ApplicationContext context) {
		if(StringUtils.isEmpty(definitionCode)){
			throw new XflowDefinitionException("子流程的定义代码不能空");
		}
	}

	@Override
	public final List<Transition> doWork(IXflowContext context) {
		ProcessInstanceServiceImpl pis = (ProcessInstanceServiceImpl)context.getApplicationContext().getProcessInstanceService();
		ProcessInstance processInstance = pis.start(
				definitionCode,
				context.getProcessInstanceContext().getProcessInstance().getBusinessKey(),
				context.getBusinessForm(),
				new Participant(
					context.getProcessInstanceContext().getProcessInstance().getCreatorId(),
					context.getProcessInstanceContext().getProcessInstance().getCreatorName(),
					context.getProcessInstanceContext().getProcessInstance().getCreatorType()
				),
				context.getProcessInstanceContext().getProcessInstance().getId(),
				id);
		return doWork(processInstance , context);
	}
	
	public abstract List<Transition> doWork(ProcessInstance subProcessInstance , IXflowContext context);
}
