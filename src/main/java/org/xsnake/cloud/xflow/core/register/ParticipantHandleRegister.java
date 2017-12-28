package org.xsnake.cloud.xflow.core.register;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.xflow.core.Activity;
import org.xsnake.cloud.xflow.core.DefinitionConstant;
import org.xsnake.cloud.xflow.core.participant.AssignParticipant;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;

@Component  
@ConfigurationProperties(prefix="xflow")
public class ParticipantHandleRegister {
	
	/**
	 * 配置用户的参与者处理器。
	 * KEY为流程定义时指定的参与者类型type。
	 * VALUE为配置了该类型使用的处理器类。
	 * 该处理器类必须为ParticipantHandle的子类。
	 * 如果用户自定义的处理器与内置处理器的KEY值相同，则会覆盖系统内置处理器。
	 */
	Map<String,String> participants = new HashMap<>();
	
	/**
	 * 构造函数初始化了XFLOW内置的参与者处理器
	 */
	public ParticipantHandleRegister(){
		participants.put(DefinitionConstant.PARTICIPANT_ASSIGN, AssignParticipant.class.getName());
	}

	public Map<String, String> getParticipants() {
		return participants;
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends Activity> getParticipantHandle(String type){
		Class<Activity> cls = null;
		String className = participants.get(type);
		if(StringUtils.isEmpty(className)){
			throw new XflowDefinitionException("参与者类型："+ type + " , 没有找到对应的配置项");
		}
		try{
			cls = (Class<Activity>) Class.forName(className);
		}catch (ClassNotFoundException e) {
			throw new XflowDefinitionException("参与者类型："+ type +" , 没有找到对应的类 : " + className);
		}
		return cls;
	}
}
