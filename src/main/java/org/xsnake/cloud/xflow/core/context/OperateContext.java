package org.xsnake.cloud.xflow.core.context;

import java.util.List;

import org.xsnake.cloud.xflow.service.api.Participant;

public class OperateContext extends TaskContext{

	Participant operator;
	
	String suggestion;
	
	List<Participant> participantList;
	
	OperateType operateType;
	
	String toTransitionId; 
	
	public static enum OperateType {
		
		start("START"),complete("COMPLETE"),reject("REJECT"),assign("ASSIGN"),support("SUPPORT");
		
		private String type;
		
		OperateType(String type){
			this.type = type;
		}
		
		@Override
		public String toString() {
			return type;
		}
	}
	
	public OperateContext(ApplicationContext applicationContext,String taskId,OperateType operateType,Participant operator,String suggestion) {
		super(applicationContext,taskId);
		this.operateType = operateType;
		this.operator = operator;
		this.suggestion = suggestion;
	}
	
	public OperateContext(ApplicationContext applicationContext, String taskId,OperateType operateType,Participant operator,String remark,List<Participant> participantList) {
		this(applicationContext,taskId,operateType,operator,remark);
		this.participantList = participantList;
	}

	public String getToTransitionId() {
		return toTransitionId;
	}

	public void setToTransitionId(String toTransitionId) {
		this.toTransitionId = toTransitionId;
	}

	public Participant getOperator() {
		return operator;
	}

	public String getSuggestion() {
		return suggestion;
	}
	
}
