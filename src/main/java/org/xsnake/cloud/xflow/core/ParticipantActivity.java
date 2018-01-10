package org.xsnake.cloud.xflow.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.IContext;
import org.xsnake.cloud.xflow.core.context.OperateContext;
import org.xsnake.cloud.xflow.core.context.ProcessInstanceContext;
import org.xsnake.cloud.xflow.dao.DaoTemplate;
import org.xsnake.cloud.xflow.exception.XflowBusinessException;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow.service.api.Participant;

public abstract class ParticipantActivity extends Activity{

	private static final long serialVersionUID = 1L;
	
	//该属性是设置如果参与者为空则自动完成任务
	public final static String NONE_PARTICIPANT_AUTOCOMPLETE = "NONE_PARTICIPANT_AUTOCOMPLETE";
	
	ParticipantHandle participantHandle;
	
	public ParticipantActivity(){}
	
	public ParticipantActivity(ApplicationContext context,Element activityElement) {
		super(context,activityElement);
		Element participantElement = activityElement.element(DefinitionConstant.ELEMENT_ACTIVITY_PARTICIPANT);
		if(participantElement==null){
			throw new XflowDefinitionException("任务类型活动必须指定参与者");
		}
		//初始化参与者处理器
		participantHandle = parseParticipantHandle(context,participantElement);
	}
	
	
	private ParticipantHandle parseParticipantHandle(ApplicationContext context, Element participantElement) {
			String type = participantElement.attributeValue(DefinitionConstant.ELEMENT_ACTIVITY_PARTICIPANT_TYPE);
			Constructor<? extends ParticipantHandle> constructor = null;
			ParticipantHandle participantHandle = null;
			Class<? extends ParticipantHandle> participantHandleCls = context.getParticipantHandleRegister().getParticipantHandle(type);
			try {
				constructor = participantHandleCls.getDeclaredConstructor(Element.class);
			} catch (NoSuchMethodException e) {
				throw new XflowDefinitionException("自定义的活动类没有找到构造函数 ："+e.getMessage());
			} catch(SecurityException e){
				throw new XflowDefinitionException("自定义的活动类的构造函数不能访问 ："+e.getMessage());
			}
			
			try {
				participantHandle = constructor.newInstance(participantElement);
			} catch (InstantiationException 
					| IllegalAccessException 
					| IllegalArgumentException e) {
				throw new XflowDefinitionException("自定义的活动类实例化出错："+e.getMessage());
			} catch (InvocationTargetException e) {
				throw new XflowDefinitionException("自定义的活动类实例化出错："+e.getCause().getMessage());
			}
			
		return participantHandle;
	}
	
	public final void createTask(ProcessInstanceContext context){
		DaoTemplate daoTemplate = context.getApplicationContext().getDaoTemplate();
		List<Participant> participantList = participantHandle._findParticipantList(context);
		String batchNo = UUID.randomUUID().toString();
		for(Participant participant : participantList){
			List<Object> args = new ArrayList<Object>();
			String recordId = (String)context.getAttribute(IContext.RECORD_ID);
			String taskId = UUID.randomUUID().toString();
			args.add(taskId);
			args.add(recordId);
			args.add(participant.getId());
			args.add(participant.getName());
			args.add(participant.getType());
			args.add(new Date());
			args.add(null);
			args.add("RUNNING");
			args.add(batchNo);
			daoTemplate.update(" INSERT INTO XFLOW_PROCESS_INSTANCE_TASK(ID,RECORD_ID,PARTICIPANT_ID,PARTICIPANT_NAME,PARTICIPANT_TYPE,CREATE_TIME,PARENT_ID,STATUS,BATCH_NO) VALUES ( ?,?,?,?,?,?,?,?,?) ",args.toArray());
		}
	}
	
	protected abstract List<Transition> doTask(OperateContext context);
	
	@Override
	public void definitionValidate(ApplicationContext context) {
		if(toTransitionList.size() != 1){
			throw new XflowDefinitionException("任务有且只能有一个出口路径");
		}
	}
	
	//从人工参与环节进来的请求都为人为操作的，其上线文必为OperateContext
	@Override
	public List<Transition> doAutomaticWork(ProcessInstanceContext context){
		throw new XflowBusinessException("程序内部错误");
	}
	
	public List<Transition> doParticipantTask(OperateContext context){
		List<Transition> resultList = doTask(context);
		//这里删除相关的任务。
		context.getApplicationContext().getDaoTemplate().update(" DELETE FROM XFLOW_PROCESS_INSTANCE_TASK WHERE BATCH_NO = ? ",new Object[]{context.getTask().getBatchNo()});
		//记录任务操作历史。
		String sql = "INSERT INTO XFLOW_PROCESS_INSTANCE_HISTORY(RECORD_ID,OPERATOR_ID,OPERATOR_NAME,OPERATOR_TYPE,SUGGESTION,OPERATE_TIME,OPERATE_TYPE,CREATE_TIME) VALUES ( ?,?,?,?,?,?,?,?)";
		List<Object> args = new ArrayList<Object>();
		args.add(context.getAttribute(IContext.RECORD_ID));
		args.add(context.getOperator().getId());
		args.add(context.getOperator().getName());
		args.add(context.getOperator().getType());
		args.add(context.getSuggestion());
		args.add(new Date());
		args.add(OperateContext.OperateType.start);
		args.add(context.getTask().getCreateTime());
		context.getApplicationContext().getDaoTemplate().update(sql,args.toArray());
		return resultList;
	}
}
