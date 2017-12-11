package org.xsnake.cloud.xflow.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xsnake.cloud.xflow.core.context.IXflowContext;

public abstract class Activity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Map<String,String> attributes = new HashMap<String,String>();  
	
	public String getAttribute(final String id) {
		String obj = attributes.get(id);
		return obj;
	}
	
	//验证流程定义是否有错误
	public abstract void definitionValidate();
	
	public abstract void doWork(IXflowContext context);
	
	public List<Transition> nextPaths(IXflowContext context){
		return getOutTransition(context.getProcessDefinition());
	}

	//从其他节点流入的连接线
	public List<Transition> getInTransition(ProcessDefinition processDefinition) {
		//TODO 从定义中找到它的流入线
		return null;
	}

	//从本节点流出的连接线
	public List<Transition> getOutTransition(ProcessDefinition processDefinition) {
		//TODO 从定义中找到它的流出线
		return null;
	}
	
	
	//自动完成所有自动节点，直到结束或者遇到人工参与的节点等待人工完成
	public final boolean autoDoWork(IXflowContext context){
		//做节点需要做的事情
		List<Transition> toTransitionList = null;
		try{
			doWork(context);
		}catch (Exception e) {
//			TODO 这个错误是自动环节参数设置错误引起的原因
			//自动程序执行错误
		}
		
		//做完后如果是结束环节，这说明整个业务流程实例已经完全结束
		if(this instanceof Endedable){
			boolean result = ((Endedable)this).getResult();
			//把结果通过消息发送出去。通过该消息完成业务回调
			return true;
		}
		//获取环节的流出路径列表
		toTransitionList = nextPaths(context);
		//如果节点是可以返回空的流转，并且是Waitable的实现，则退出
		//在一些情况可能会出现没有流转的情况，比如JOIN，它会等待所有的任务都到达后再往下走
		if (this instanceof Waitable && (toTransitionList == null || toTransitionList.isEmpty())) {
			return false;
		}

		//这里记录最后流程实例是否结束
		boolean endFlag = false;

		//循环去做本节点产出的流转，找到流转所要去的节点，然后再做他们应该做的事情
		for(Transition transition : toTransitionList){
			//如果不是自动环节则是人工环节，自动环节会递归调用本方法。要么流程直到结束为止，要么遇到人工参与环节停止，等待人工处理
			Activity targetActivity = transition.getTargetActivity(context.getProcessDefinition());
			if(targetActivity instanceof AutomaticActivity){
				endFlag = ((AutomaticActivity)targetActivity).autoDoWork(context);
			}else{
				((ParticipantActivity)targetActivity).createTask(context);
			}
		}
		
		return endFlag;
	}

}
