package org.xsnake.cloud.xflow.core.register;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.xflow.activity.automatic.DecisionActivity;
import org.xsnake.cloud.xflow.activity.automatic.EndActivity;
import org.xsnake.cloud.xflow.activity.automatic.ForkActivity;
import org.xsnake.cloud.xflow.activity.automatic.GetHttpActivity;
import org.xsnake.cloud.xflow.activity.automatic.JoinActivity;
import org.xsnake.cloud.xflow.activity.automatic.PostHttpActivity;
import org.xsnake.cloud.xflow.activity.automatic.ProcedureActivity;
import org.xsnake.cloud.xflow.activity.automatic.RemoteSSHActivity;
import org.xsnake.cloud.xflow.activity.automatic.SVNActivity;
import org.xsnake.cloud.xflow.activity.automatic.SendMailActivity;
import org.xsnake.cloud.xflow.activity.automatic.StartActivity;
import org.xsnake.cloud.xflow.activity.participant.DecisionTaskActivity;
import org.xsnake.cloud.xflow.activity.participant.MultiTaskActivity;
import org.xsnake.cloud.xflow.activity.participant.SupportTaskActivity;
import org.xsnake.cloud.xflow.activity.participant.TaskActivity;
import org.xsnake.cloud.xflow.core.Activity;
import org.xsnake.cloud.xflow.core.DefinitionConstant;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;

@Component  
@ConfigurationProperties(prefix="xflow")  
public class ActivityRegister {
	
	
	/**
	 * 配置用户的自定义活动类
	 * KEY值为流程定义中Activity的type属性
	 * VALUE值为对应的活动处理类
	 * 如果此处配置的KEY与内置名称一致，则覆盖内置活动
	 */
	Map<String,String> activitys = new HashMap<>();
	
	
	/**
	 * 构造函数初始化了XFLOW内置的参与者处理器
	 */
	public ActivityRegister(){
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_DECISION, DecisionActivity.class.getName());
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_END, EndActivity.class.getName());
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_FORK, ForkActivity.class.getName());
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_HTTP_GET, GetHttpActivity.class.getName());
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_JOIN, JoinActivity.class.getName());
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_HTTP_POST, PostHttpActivity.class.getName());
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_PROCEDURE, ProcedureActivity.class.getName());
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_SSH, RemoteSSHActivity.class.getName());
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_MAIL, SendMailActivity.class.getName());
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_START, StartActivity.class.getName());
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_SVN, SVNActivity.class.getName());
		
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_TASK_DECISION, DecisionTaskActivity.class.getName());
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_TASK_MULTI, MultiTaskActivity.class.getName());
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_TASK_SUPPORT, SupportTaskActivity.class.getName());
		activitys.put(DefinitionConstant.TYPE_ACTIVITY_TASK_NORMAL, TaskActivity.class.getName());
	}
	
	public Map<String, String> getActivitys() {
		return activitys;
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends Activity> getActivity(String type){
		Class<Activity> cls = null;
		
		String className = activitys.get(type);
		
		if(StringUtils.isEmpty(className)){
			throw new XflowDefinitionException("活动类型："+ type + " , 没有找到对应的配置项");
		}
		
		try{
			cls = (Class<Activity>) Class.forName(className);
		}catch (ClassNotFoundException e) {
			throw new XflowDefinitionException("活动类型："+ type +" , 没有找到对应的类 : " + className);
		}
		return cls;
	}
	
}

