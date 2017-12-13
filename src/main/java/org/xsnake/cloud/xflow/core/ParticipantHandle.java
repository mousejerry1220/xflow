package org.xsnake.cloud.xflow.core;

import java.util.List;

import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.service.api.Participant;

public abstract class ParticipantHandle {

	public static enum Type {
		
		assign("assign"), 
		
		creator("creator"),
		
		runtimeAssign("runtimeAssign"),
		
		expand("expand"),
		
		sql("sql"),
		
		formVariable("formVariable");
		
		String type;

		private Type(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
		
	}

	protected abstract List<Participant> _findParticipantList(IXflowContext context);
	
	public final List<Participant> findParticipantList(IXflowContext context){
		List<Participant> list = null;
		try{
			list = _findParticipantList(context);
		}catch (Exception e) {
			//TODO 记录出错的参与者处理器，可以让管理员尽快解决错误。同时把这个任务转交到默认管理员处理
		}
		
		if(list == null || list.isEmpty() ){
			//如果没有任何参与者，这里可以读取配置使用一个默认管理员处理没有找到参与者的环节
		}
		return list;
	}

}
