package org.xsnake.cloud.xflow.core;

import java.io.Serializable;
import java.util.List;

import org.xsnake.cloud.common.utils.StringUtils;

import com.alibaba.fastjson.JSON;

public class ProcessDefinition implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<Activity> activityList;
	
	private List<Transition> transitionList;

	private String id;
	
	//相同code的流程代表一种流程的不同版本
	private String code;
	
	private String processDefinitionJson;
	
	public static ProcessDefinition parse(String id,final String code,final String processDefinitionJson){
		//如果id不为空则试图从缓存中获取数据如果获取成功
		ProcessDefinition processDefinition = (ProcessDefinition)JSON.parseObject(processDefinitionJson,ProcessDefinition.class);
		if(id == null){
			id = StringUtils.getUUID();
		}
		//初始化完毕后将其缓存
		processDefinition.processDefinitionJson = processDefinitionJson;
		processDefinition.id = id;
		processDefinition.code = code;
		return processDefinition;
	}
	
	public static ProcessDefinition parse(String code,String processDefinitionJson){
		return parse(null,processDefinitionJson);
	}

	public List<Activity> getActivityList() {
		return activityList;
	}

	public List<Transition> getTransitionList() {
		return transitionList;
	}

	public String getId() {
		return id;
	}
	
	public String getCode() {
		return code;
	}

	public String getProcessDefinitionJson() {
		return processDefinitionJson;
	}

}
