package org.xsnake.cloud.xflow.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.activity.EndActivity;
import org.xsnake.cloud.xflow.core.activity.StartActivity;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.IContext;
import org.xsnake.cloud.xflow.core.context.OperateContext;
import org.xsnake.cloud.xflow.core.context.ProcessInstanceContext;
import org.xsnake.cloud.xflow.dao.DaoTemplate;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;

public abstract class Activity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected String id;
	
	protected String name;
	
	protected String type;
	
	protected List<Transition> fromTransitionList = new ArrayList<Transition>();
	
	protected List<Transition> toTransitionList = new ArrayList<Transition>();
	
	protected Map<String,String> attributes = new HashMap<String,String>();
	
	public Activity(){}
	
	public Activity(ApplicationContext context, final Element activityElement){
		id = activityElement.attributeValue(DefinitionConstant.ELEMENT_ACTIVITY_ATTRIBUTE_ID);
		name = activityElement.attributeValue(DefinitionConstant.ELEMENT_ACTIVITY_ATTRIBUTE_NAME);
		type = activityElement.attributeValue(DefinitionConstant.ELEMENT_ACTIVITY_ATTRIBUTE_TYPE);
		if(StringUtils.isEmpty(id)){
			throw new XflowDefinitionException("活动定义错误： id不能为空");
		}
		if(StringUtils.isEmpty(name)){
			throw new XflowDefinitionException("活动定义错误： name不能为空 ");
		}
		if(StringUtils.isEmpty(type)){
			throw new XflowDefinitionException("活动定义错误： type不能为空 ");
		}
		parseAttributes(activityElement);
	}

	private void parseAttributes(final Element activityElement) {
		Element propsElement = activityElement.element(DefinitionConstant.ELEMENT_ATTRIBUTES);
		if(propsElement == null){
			return;
		}
		List<Element> propertyList = propsElement.elements(DefinitionConstant.ELEMENT_ATTRIBUTES_ATTRIBUTE);
		if(propertyList == null){
			return;
		}
		for(Element propertyElement : propertyList){
			attributes.put(propertyElement.attributeValue(DefinitionConstant.ELEMENT_ATTRIBUTES_ATTRIBUTE_KEY), propertyElement.getText());
		}
	}
	
	public String getAttribute(final String id) {
		return attributes.get(id);
	}
	
	public final void validate(ApplicationContext context){
		//只有开始环节是没有来源流转的，其他都必须包含来源流转，否则系统无法到达改环节
		if(!(this instanceof StartActivity)){ //如果不是开始环节
			if(fromTransitionList.size() == 0){ //并且该环节的来源为空
				throw new XflowDefinitionException("活动定义错误：[" + name + "] 没有任何一个来源路径");
			}
		}
		
		if(!(this instanceof VirtualNode || this instanceof EndActivity)){
			if(toTransitionList.size() == 0 ){
				throw new XflowDefinitionException("活动定义错误：[" + name + "] 没有任何一个出口路径");
			}
		}
		definitionValidate(context);
	}
	
	//验证流程定义是否有错误
	public abstract void definitionValidate(ApplicationContext context);
	
	protected abstract List<Transition> doAutomaticWork(ProcessInstanceContext context);
	
	protected abstract List<Transition> doParticipantTask(OperateContext context);
	
	private List<Transition> _doWork(ProcessInstanceContext context){
		DaoTemplate daoTemplate = context.getApplicationContext().getDaoTemplate();
		List<Transition> toPathList = null;// doWork(context);
		String recordId = null;
		if(this instanceof ParticipantActivity){
			recordId = ((OperateContext)context).getTask().getRecordId();
			toPathList = doParticipantTask((OperateContext)context);
		}else{
			recordId = activityRecord(context,this);
			toPathList = doAutomaticWork(context);
		}
		
		//记录转出路径
		if(toPathList != null){
			List<Object> args = new ArrayList<Object>();
			for(Transition toTransition : toPathList){
				args.add(recordId);
				args.add(toTransition.getId());
				args.add(toTransition.getName());
				args.add(toTransition.targetActivity.getType());
				args.add(toTransition.targetActivity.getName());
				args.add(toTransition.targetActivity.getId());
				daoTemplate.update(" INSERT INTO XFLOW_PROCESS_INSTANCE_PATH(RECORD_ID,TO_PATH_ID,TO_PATH_NAME,TO_ACTIVITY_TYPE,TO_ACTIVITY_NAME,TO_ACTIVITY_ID) VALUES ( ?,?,?,?,?,?)",args.toArray());
			}
		}
		return toPathList;
	}

	private String activityRecord(ProcessInstanceContext context,Activity activity) {
		DaoTemplate daoTemplate = context.getApplicationContext().getDaoTemplate();
		Date startTime = new Date();
		String recordId = UUID.randomUUID().toString();
		context.setAttribute(IContext.RECORD_ID,recordId);
		//自动环节保存记录
		Date endTime = new Date();
		List<Object> args = new ArrayList<Object>();
		Transition formTransition = (Transition)context.getAttribute(IContext.FROM_TRANSITION);
		args.add(recordId);
		args.add(context.getProcessInstance().getId());
		args.add(activity.type);
		args.add(activity.name);
		args.add(activity.id);
		args.add(startTime);
		args.add(endTime);
		args.add(formTransition != null ? formTransition.getId():null);
		Integer sn = getSN(context);
		args.add(sn);
		daoTemplate.update(" INSERT INTO XFLOW_PROCESS_INSTANCE_RECORD (ID,PROCESS_INSTANCE_ID,ACTIVITY_TYPE,ACTIVITY_NAME,ACTIVITY_ID,START_TIME,END_TIME,FROM_PATH,SN) VALUES ( ?,?,?,?,?,?,?,?,?) ",args.toArray());
		return recordId;
	}

	//自动完成所有自动节点，直到结束或者遇到人工参与的节点等待人工完成
	public final boolean process(ProcessInstanceContext context){
		//做节点需要做的事情
		List<Transition> toPathList = _doWork(context);
		
		//如果是任务类型的实现，则广播完成的消息
		if(this instanceof ParticipantActivity){
			//TODO 发送任务完成的消息广播
		}
		
		//做完后如果是结束环节，这说明整个业务流程实例已经完全结束
		if(this instanceof EndActivity){
			return true;
		}
		
		//如果是waitAble接口实现，则有可能返回一个空的流转，这个时候不做任何处理
		//在一些情况可能会出现没有流转的情况，比如JOIN，它会等待所有的任务都到达后再往下走
		if (((this instanceof Waitable) || (this instanceof VirtualNode)) && 
			(toPathList == null || toPathList.isEmpty())){
			return false;
		}
		
		//这里记录最后流程实例是否结束
		boolean endFlag = false;
		
		//循环去做本节点产出的流转，找到流转所要去的节点，然后再做他们应该做的事情
		for(Transition transition : toPathList){
			context.setAttribute(IContext.FROM_TRANSITION, transition);
			//如果不是自动环节则是人工环节，自动环节会递归调用本方法。要么流程直到结束为止，要么遇到人工参与环节停止，等待人工处理
			Activity targetActivity = transition.targetActivity;
			if(targetActivity instanceof AutomaticActivity){
				endFlag = ((AutomaticActivity)targetActivity).process(context);
			}else{
				activityRecord(context,targetActivity);
				((ParticipantActivity)targetActivity).createTask(context);
			}
		}
		
		return endFlag;
	}

	private Integer getSN(ProcessInstanceContext context) {
		Integer sn = (Integer)context.getAttribute(IContext.RECORD_SN);
		if(sn == null){
			sn = 0;
		}
		sn = sn + 1;
		context.setAttribute(IContext.RECORD_SN, sn);
		return sn;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	
}
