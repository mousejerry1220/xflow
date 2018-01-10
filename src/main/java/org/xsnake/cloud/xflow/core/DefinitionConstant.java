package org.xsnake.cloud.xflow.core;

public class DefinitionConstant {

	public static final String ELEMENT_ACTIVITYS = "activitys";
	
	public static final String ELEMENT_ACTIVITY = "activity";

	public static final String ELEMENT_ACTIVITY_ATTRIBUTE_ID = "id";
	
	public static final String ELEMENT_ACTIVITY_ATTRIBUTE_TYPE = "type";
	
	public static final String ELEMENT_ACTIVITY_ATTRIBUTE_NAME = "name";

	public static final String ELEMENT_ATTRIBUTES = "attributes";
	
	public static final String ELEMENT_ATTRIBUTES_ATTRIBUTE = "attribute";
	
	public static final String ELEMENT_ATTRIBUTES_ATTRIBUTE_KEY = "name";
	
	public static final String ELEMENT_TRANSITIONS = "transitions";
	
	public static final String ELEMENT_TRANSITION = "transition";
	
	
//	public static final String ELEMENT_ACTIVITY_PARTICIPANTS = "participants";
	
	public static final String ELEMENT_ACTIVITY_PARTICIPANT = "participant";
	
	public static final String ELEMENT_ACTIVITY_PARTICIPANT_TYPE = "type";
	
	public static final String ELEMENT_ACTIVITY_PARTICIPANT_ASSIGN = "assign";
	
	//内置活动类型定义，对应ELEMENT_ACTIVITY_ATTRIBUTE_TYPE
	
	public static final String TYPE_ACTIVITY_DECISION = "decision";
	
	public static final String TYPE_ACTIVITY_END = "end";
	
	public static final String TYPE_ACTIVITY_FORK = "fork";
	
	public static final String TYPE_ACTIVITY_HTTP_GET = "httpGet";
	
	public static final String TYPE_ACTIVITY_HTTP_POST = "httpPost";
	
	public static final String TYPE_ACTIVITY_JOIN = "join";
	
	public static final String TYPE_ACTIVITY_PROCEDURE = "procedure";
	
	public static final String TYPE_ACTIVITY_SSH = "ssh";
	
	public static final String TYPE_ACTIVITY_MAIL = "mail";
	
	public static final String TYPE_ACTIVITY_START = "start";

	public static final String TYPE_ACTIVITY_SVN = "svn";
	
	public static final String TYPE_ACTIVITY_TASK_DECISION = "decisionTask";
	
	public static final String TYPE_ACTIVITY_TASK_MULTI = "multiTask";
	
	public static final String TYPE_ACTIVITY_TASK_SUPPORT = "supportTask";
	
	public static final String TYPE_ACTIVITY_TASK_NORMAL = "normalTask";
	
	//内置参与者类型定义
	
	public static final String PARTICIPANT_ASSIGN = "assign";

	public static final String TYPE_ACTIVITY_ASYN_SUB = "asynSub";
	
	public static final String TYPE_ACTIVITY_SYN_SUB = "synSub";
	
}
