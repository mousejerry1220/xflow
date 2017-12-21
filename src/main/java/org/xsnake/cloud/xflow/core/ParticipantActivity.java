package org.xsnake.cloud.xflow.core;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.core.context.OperateContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;

public abstract class ParticipantActivity extends Activity{

	private static final long serialVersionUID = 1L;
	
	//该属性是设置如果参与者为空则自动完成任务
	public final static String NONE_PARTICIPANT_AUTOCOMPLETE = "NONE_PARTICIPANT_AUTOCOMPLETE";
	
	ParticipantHandle participantHandle;
	
	public ParticipantActivity(Element activityElement) {
		super(activityElement);
		Element participantElement = activityElement.element(DefinitionConstant.ELEMENT_ACTIVITY_PARTICIPANTS);
		if(participantElement==null){
			throw new XflowDefinitionException("任务类型活动必须指定参与者");
		}
		
		
	}
	
	public final void createTask(IXflowContext context){
		
	}
	
	public abstract List<Transition> doTask(OperateContext context);

	//从人工参与环节进来的请求都为人为操作的，其上线文必为OperateContext
	@Override
	public final List<Transition> doWork(IXflowContext context) {
		return doTask((OperateContext)context);
	}
	
}
