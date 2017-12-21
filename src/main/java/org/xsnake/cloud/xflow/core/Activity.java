package org.xsnake.cloud.xflow.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.activity.EndActivity;
import org.xsnake.cloud.xflow.core.activity.StartActivity;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;

public abstract class Activity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected String id;
	
	protected String name;
	
	protected String type;
	
	protected List<Transition> fromTransitionList = new ArrayList<Transition>();
	
	protected List<Transition> toTransitionList = new ArrayList<Transition>();
	
	public Activity(final Element activityElement){
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
		Element propsElement = activityElement.element(DefinitionConstant.ELEMENT_ACTIVITY_ATTRIBUTES);
		List<Element> propertyList = propsElement.elements(DefinitionConstant.ELEMENT_ACTIVITY_ATTRIBUTES_ATTRIBUTE);
		for(Element propertyElement : propertyList){
			attributes.put(propertyElement.attributeValue(DefinitionConstant.ELEMENT_ACTIVITY_ATTRIBUTES_ATTRIBUTE_KEY), propertyElement.getText());
		}
	}
	
	protected Map<String,String> attributes = new HashMap<String,String>();
	
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

		if(!(this instanceof NoNextTarget)){
			if(toTransitionList.size() == 0 ){
				throw new XflowDefinitionException("活动定义错误：[" + name + "] 没有任何一个出口路径");
			}
		}
		definitionValidate(context);
	}
	
	//验证流程定义是否有错误
	public abstract void definitionValidate(ApplicationContext context);
	
	public abstract List<Transition> doWork(IXflowContext context);
	
	/**
	 * 该方法可以让子类覆盖。可以用作某些节点等待条件达成后再做后续动作。
	 * @return
	 */
//	public List<Transition> nextPaths(IXflowContext context){
//		return toTransitionList;
//	}

	//自动完成所有自动节点，直到结束或者遇到人工参与的节点等待人工完成
	public final boolean process(IXflowContext context){
		//做节点需要做的事情
		List<Transition> toTransitionList = null;
		try{
			toTransitionList = doWork(context);
		}catch (Exception e) {
//			TODO 这个错误是自动环节参数设置错误引起的原因
			//自动程序执行错误
		}
		
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
		if (((this instanceof Waitable) || (this instanceof NoNextTarget)) && 
			(toTransitionList == null || toTransitionList.isEmpty())){
			return false;
		}

		//这里记录最后流程实例是否结束
		boolean endFlag = false;

		//循环去做本节点产出的流转，找到流转所要去的节点，然后再做他们应该做的事情
		for(Transition transition : toTransitionList){
			//如果不是自动环节则是人工环节，自动环节会递归调用本方法。要么流程直到结束为止，要么遇到人工参与环节停止，等待人工处理
			Activity targetActivity = transition.targetActivity;
			if(targetActivity instanceof AutomaticActivity){
				endFlag = ((AutomaticActivity)targetActivity).process(context);
			}else{
				((ParticipantActivity)targetActivity).createTask(context);
			}
		}
		
		return endFlag;
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
