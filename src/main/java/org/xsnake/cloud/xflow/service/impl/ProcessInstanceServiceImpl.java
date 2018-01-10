package org.xsnake.cloud.xflow.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.cloud.xflow.core.ProcessDefinition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.ProcessInstanceContext;
import org.xsnake.cloud.xflow.dao.repository.DefinitionInstanceRepository;
import org.xsnake.cloud.xflow.dao.repository.DefinitionInstanceXMLRepository;
import org.xsnake.cloud.xflow.dao.repository.ProcessInstanceFormRepository;
import org.xsnake.cloud.xflow.dao.repository.ProcessInstanceRepository;
import org.xsnake.cloud.xflow.dao.repository.TaskRepository;
import org.xsnake.cloud.xflow.exception.XflowBusinessException;
import org.xsnake.cloud.xflow.service.api.IProcessInstanceService;
import org.xsnake.cloud.xflow.service.api.Page;
import org.xsnake.cloud.xflow.service.api.Participant;
import org.xsnake.cloud.xflow.service.api.ProcessInstanceCondition;
import org.xsnake.cloud.xflow.service.api.vo.HistoryRecord;
import org.xsnake.cloud.xflow.service.api.vo.ProcessInstance;
import org.xsnake.cloud.xflow.tools.LockService;
import org.xsnake.cloud.xflow.tools.LockedException;

@Service
@Transactional
public class ProcessInstanceServiceImpl implements IProcessInstanceService{
	
	@Autowired
	ProcessInstanceRepository processInstanceRepository;

	@Autowired
	DefinitionInstanceRepository definitionInstanceRepository;
	
	@Autowired
	DefinitionInstanceXMLRepository definitionInstanceXMLRepository;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	ProcessInstanceFormRepository processInstanceFormRepository;
	
	@Autowired
	LockService lockService;
	
	public ProcessInstance start(String definitionCode, String businessKey, String businessForm,Participant creator,String parentId,String parentActivityId) {
		//这里要检查是否已经存在同一业务，同一流程正在创建，可以通过缓存来实现
		try {
			lockService.lock(definitionCode, businessKey);

			ProcessInstance runningProcessInstance = processInstanceRepository.getRunningByBusinessKey(definitionCode, businessKey);
			if(runningProcessInstance !=null){
				throw new XflowBusinessException("该业务已经正在运行中");
			}
			
			BigDecimal version = definitionInstanceRepository.getCurrentVersion(definitionCode);
			if(version == null || version.longValue() == 0){
				throw new XflowBusinessException("流程未设置完成");
			}
			String xml = definitionInstanceXMLRepository.getXML(definitionCode, version.longValue());
			//TODO 交给配置做转码
			xml = new String(xml.getBytes("iso8859-1"));
			ProcessDefinition processDefinition = null;
			try {
				processDefinition = ProcessDefinition.parse(applicationContext, definitionCode, xml);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			ProcessInstance processInstance = new ProcessInstance();
			processInstance.setBusinessKey(businessKey);
			processInstance.setCreatorId(creator.getId());
			processInstance.setCreatorName(creator.getName());
			processInstance.setCreatorType(creator.getType());
			processInstance.setDefinitionCode(definitionCode);
			processInstance.setDefinitionVersion(version.longValue());
			processInstance.setParentId(parentId);
			processInstance.setParentActivityId(parentActivityId);
			//TODO 需要重构优化，可以让用户自定义主键生成策略，和标题生成策略，默认使用UUID，生成ID后要检查是否已经存在
			String processInstanceId = UUID.randomUUID().toString();
			processInstance.setId(processInstanceId);
			processInstance.setName("临时名称,这里通过定义的规则替换");
			processInstance.setStatus(IProcessInstanceService.STATUS_RUNNING);
			processInstance.setStartTime(new Date());
			processInstance.setParentId(null);
			processInstanceRepository.save(processInstance);
			processInstanceFormRepository.save(processInstanceId, businessForm);
			ProcessInstanceContext context = new ProcessInstanceContext(applicationContext,processInstance,businessForm);
			boolean end = processDefinition.startProcess(context);
			if(end){
				processInstance.setEndTime(new Date());
				processInstance.setStatus(IProcessInstanceService.STATUS_END);
			}
			return processInstance;
		} catch (LockedException e) {
			throw new XflowBusinessException("该业务已经开启了流程");
		} catch (IOException e) {
			throw new XflowBusinessException("zookeeper服务器连接异常");
		} catch (Exception e){
			e.printStackTrace();
			throw new XflowBusinessException("异常: "+e.getMessage());
		} finally{
			//释放锁
			try {
				lockService.unLock(definitionCode, businessKey);
			} catch (IOException e) {
				//TODO 这里可能会导致业务开启流程，但是锁依然没有被释放，记录到异常日志
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public ProcessInstance start(String definitionCode, String businessKey, String businessForm,Participant creator) {
		return start(definitionCode, businessKey, businessForm, creator, null,null);
	}
	
	@Override
	public List<ProcessInstance> listProcessInstanceByBusinessKey(String businessKey) {
		return processInstanceRepository.listProcessInstanceByBusinessKey(businessKey);
	}

	@Override
	public ProcessInstance getRunningByBusinessKey(String definitionCode,String businessKey) {
		return processInstanceRepository.getRunningByBusinessKey(definitionCode,businessKey);
	}

	@Override
	public ProcessInstance getProcessInstance(String processInstanceId) {
		return processInstanceRepository.get(processInstanceId);
	}

	@Override
	public void close(String processInstanceId, Participant participant, String comment) {
		taskRepository.removeAllTask(processInstanceId);
		processInstanceRepository.updateStatus(processInstanceId,IProcessInstanceService.STATUS_CLOSE);
	}

	@Override
	public void closeByBusinessKey(String definitionCode, String businessKey,Participant participant, String comment) {
		ProcessInstance processInstance = processInstanceRepository.getRunningByBusinessKey(definitionCode, businessKey);
		taskRepository.removeAllTask(processInstance.getId());
		processInstanceRepository.updateStatus(processInstance.getId(),IProcessInstanceService.STATUS_CLOSE);
	}

	@Override
	public Page<ProcessInstance> query(ProcessInstanceCondition processInstanceCondition) {
		return processInstanceRepository.query(processInstanceCondition);
	}

	@Override
	public Page<ProcessInstance> queryJoin(ProcessInstanceCondition processInstanceCondition) {
		return processInstanceRepository.queryJoin(processInstanceCondition);
	}

	@Override
	public List<HistoryRecord> listHistory(String processInstanceId) {
		return null;
	}

}
