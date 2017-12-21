package org.xsnake.cloud.xflow.core;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xsnake.cloud.xflow.activity.automatic.StartActivity;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;

public class ProcessDefinition implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private List<Activity> activityList;
	
	private List<Transition> transitionList;
	
	//相同code的流程代表一种流程的不同版本
	private String code;
	
	private String processDefinitionXML;
	
	private ProcessDefinition(ApplicationContext context,String processDefinitionXML) {
		Document document = null;
		try {
			document = DocumentHelper.parseText(processDefinitionXML);
		} catch (DocumentException e) {
			throw new XflowDefinitionException("定义的XML不符合规范，无法解析 " + e.getMessage());
		}
		Element root = document.getRootElement();
		Element activitysElement = root.element(DefinitionConstant.ELEMENT_ACTIVITYS);
		Element transitionsElement = root.element(DefinitionConstant.ELEMENT_TRANSITIONS);
		
		List<Element> activitys = activitysElement.elements(DefinitionConstant.ELEMENT_ACTIVITY);
		List<Element> transitions = transitionsElement.elements(DefinitionConstant.ELEMENT_TRANSITIONS);
		
		activityList = parseActivitys(context,activitys);
		transitionList = parseTransitions(transitions);
		setRelationship();
		validate();
	}
	
	private void validate() {
		int startCount = 0;
		int endCount = 0;
		for(Activity activity : activityList){
			if(activity instanceof StartActivity){
				startCount++;
			}
			if(activity instanceof Endedable){
				endCount++;
			}
			activity.validate();
		}
		if(startCount != 1){
			throw new XflowDefinitionException("流程定义必须有且只有一个开始活动");
		}
		
		if(endCount == 0){
			throw new XflowDefinitionException("流程定义必须至少包含一个结束活动");
		}
	}

	private void setRelationship() {
		for(Activity activity : activityList){
			for(Transition transition : transitionList){
				if(transition.getSourceId().equals(activity.getId())){
					activity.toTransitionList.add(transition);
					transition.sourceActivity = activity;
				}
				if(transition.getTargetId().equals(activity.getId())){
					activity.fromTransitionList.add(transition);
					 transition.targetActivity = activity;
				}
			}
		}
	}

	private List<Transition> parseTransitions(List<Element> transitions) {
		List<Transition> list = new ArrayList<Transition>();
		for(Element transitionElement : transitions){
			Transition transition = new Transition(transitionElement);
			list.add(transition);
		}
		return list;
	}

	private List<Activity> parseActivitys(ApplicationContext context,List<Element> activitys) {
		List<Activity> list = new ArrayList<Activity>();
		for(final Element activityElement : activitys){
			String type = activityElement.attributeValue(DefinitionConstant.ELEMENT_ACTIVITY_ATTRIBUTE_TYPE);
			Constructor<? extends Activity> constructor = null;
			Activity activity = null;
			Class<? extends Activity> activityCls = context.getActivityRegister().getActivity(type);
			try {
				constructor = activityCls.getDeclaredConstructor(Element.class);
			} catch (NoSuchMethodException e) {
				throw new XflowDefinitionException("自定义的活动类没有找到构造函数 ："+e.getMessage());
			} catch(SecurityException e){
				throw new XflowDefinitionException("自定义的活动类的构造函数不能访问 ："+e.getMessage());
			}
			
			try {
				activity = constructor.newInstance(activityElement);
			} catch (InstantiationException 
					| IllegalAccessException 
					| IllegalArgumentException
					| InvocationTargetException e) {
				throw new XflowDefinitionException("自定义的活动类实例化出错："+e.getMessage());
			}
			list.add(activity);
		}
		return list;
	}

	public static ProcessDefinition parse(ApplicationContext context,final String code,final String processDefinitionXML) throws DocumentException{
		//如果id不为空则试图从缓存中获取数据如果获取成功
		ProcessDefinition processDefinition = new ProcessDefinition(context,processDefinitionXML);
		//初始化完毕后将其缓存
		processDefinition.processDefinitionXML = processDefinitionXML;
		processDefinition.code = code;
		return processDefinition;
	}

	public List<Activity> getActivityList() {
		return activityList;
	}

	public List<Transition> getTransitionList() {
		return transitionList;
	}
	
	public String getCode() {
		return code;
	}

	public String getProcessDefinitionXML() {
		return processDefinitionXML;
	}

}
