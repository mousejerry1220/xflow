package org.xsnake.cloud.xflow.core.context;

import java.util.List;

import org.xsnake.cloud.xflow.api.Participant;

public class OperateContext extends TaskContext{

	Participant operator;
	
	String remark;
	
	List<Participant> participantList;
	
	OperateType operateType;
	
	public static enum OperateType {
		complete,reject,assign,support
	}
	
	public OperateContext(ApplicationContext applicationContext,String taskId,OperateType operateType,Participant operator,String remark) {
		super(applicationContext,taskId);
		this.operateType = operateType;
		this.operator = operator;
		this.remark = remark;
	}
	
	public OperateContext(ApplicationContext applicationContext, String taskId,OperateType operateType,Participant operator,String remark,List<Participant> participantList) {
		this(applicationContext,taskId,operateType,operator,remark);
		this.participantList = participantList;
	}

	public OperateContext getOperateContext() {
		return this;
	}
}
