package org.xsnake.cloud.xflow.service.api;

import java.util.List;

import org.xsnake.cloud.xflow.service.api.model.ActivityInstance;
import org.xsnake.cloud.xflow.service.api.model.ProcessInstance;

/**
 * 
 * @author Jerry.Zhao
 *
 */
public interface IProcessInstanceService {
	
	/**
	 * 开启流程实例。一个流程定义只能同时运行一个业务流程，
	 * 可以先通过缓存设置占位，然后再做业务流程开启，开启流程前都先去验证是否在缓存中是否有占位。
	 * @param definitionCode
	 * @param businessKey
	 * @param bussinessForm
	 * @param creator
	 * @return
	 */
	ProcessInstance start(String definitionCode,String businessType,String businessKey,String bussinessForm,Participant creator);

	/**
	 * 获取该业务的全部流程实例
	 * @param businessKey
	 * @return
	 */
	List<ProcessInstance> listProcessInstanceByBusinessKey(String businessType,String businessKey);
	
	/**
	 * 获取该业务对应的运行中的流程实例，如果没有则为null
	 * @param definitionCode 指定的流程定义
	 * @param businessKey 指定的业务关键值
	 * @return
	 */
	ProcessInstance getProcessInstanceByBusinessKey(String definitionCode,String businessType,String businessKey);
	
	/**
	 * 获取流程实例
	 * @param processInstanceId 指定的流程实例ID
	 * @return
	 */
	ProcessInstance getProcessInstance(String processInstanceId);

	/**
	 * 关闭流程实例
	 * @param processInstanceId
	 * @param participant
	 * @param comment
	 */
	void close(String processInstanceId,Participant participant,String comment);
	
	/**
	 * 关闭流程实例
	 * @param definitionCode
	 * @param businessKey
	 * @param participant
	 * @param comment
	 */
	void closeByBusinessKey(String definitionCode,String businessType,String businessKey,Participant participant,String comment);

	List<ProcessInstance> query(ProcessInstanceCondition processInstanceCondition);

	/**
	 * 列出流程实例的历史
	 * @param processInstanceId
	 * @return
	 */
	List<ActivityInstance> listHistory(String processInstanceId);
	
}
