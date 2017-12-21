package org.xsnake.cloud.xflow.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.dom4j.DocumentException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xsnake.cloud.xflow.core.ProcessDefinition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.core.context.ProcessInstanceContext;
import org.xsnake.cloud.xflow.dao.repository.DefinitionInstanceRepository;
import org.xsnake.cloud.xflow.dao.repository.DefinitionInstanceXMLRepository;
import org.xsnake.cloud.xflow.dao.repository.ProcessInstanceRepository;
import org.xsnake.cloud.xflow.dao.repository.TaskRepository;
import org.xsnake.cloud.xflow.dao.repository.pojo.ProcessInstancePo;
import org.xsnake.cloud.xflow.exception.XflowBusinessException;
import org.xsnake.cloud.xflow.service.api.IProcessInstanceService;
import org.xsnake.cloud.xflow.service.api.Page;
import org.xsnake.cloud.xflow.service.api.Participant;
import org.xsnake.cloud.xflow.service.api.ProcessInstanceCondition;
import org.xsnake.cloud.xflow.service.api.vo.ActivityInstance;
import org.xsnake.cloud.xflow.service.api.vo.ProcessInstanceVo;

@Service
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
	
	public ProcessInstanceVo start(String definitionCode, String businessKey, String businessForm,Participant creator,String parentId,String parentActivityId) {
		//TODO 这里要检查是否已经存在同一业务，同一流程正在创建，可以通过缓存来实现
		BigDecimal version = definitionInstanceRepository.getCurrentVersion(definitionCode);
		if(version == null || version.longValue() == 0){
			throw new XflowBusinessException("流程未设置完成");
		}
		String xml = definitionInstanceXMLRepository.getXML(definitionCode, version.longValue());
		ProcessDefinition processDefinition = null;
		try {
			processDefinition = ProcessDefinition.parse(applicationContext, definitionCode, xml);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		ProcessInstancePo processInstancePo = new ProcessInstancePo();
		processInstancePo.setBusinessForm(businessForm);
		processInstancePo.setBusinessKey(businessKey);
		processInstancePo.setCreatorId(creator.getId());
		processInstancePo.setCreatorName(creator.getName());
		processInstancePo.setCreatorType(creator.getType());
		processInstancePo.setDefinitionCode(definitionCode);
		processInstancePo.setDefinitionVersion(version.longValue());
		processInstancePo.setParentId(parentId);
		processInstancePo.setParentActivityId(parentActivityId);
		//TODO 需要重构优化，可以让用户自定义主键生成策略，和标题生成策略，默认使用UUID，生成ID后要检查是否已经存在
		String processInstanceId = UUID.randomUUID().toString();
		processInstancePo.setId(processInstanceId);
		
		processInstancePo.setName("临时名称,这里通过定义的规则替换");
		processInstancePo.setStatus("RUN");
		processInstancePo.setStartTime(new Date());
		processInstancePo.setParentId(null);
		processInstanceRepository.save(processInstancePo);
		
		ProcessInstanceVo processInstanceVo = new ProcessInstanceVo();
		BeanUtils.copyProperties(processInstancePo, processInstanceVo);
		
		IXflowContext xflowContext = new ProcessInstanceContext(applicationContext,processInstanceVo);
		boolean end = processDefinition.startProcess(xflowContext);
		if(end){
			processInstanceVo.setEndTime(new Date());
			processInstanceVo.setStatus(ProcessInstanceVo.STATUS_END);
		}
		
		return processInstanceVo;
	}
	
	@Override
	public ProcessInstanceVo start(String definitionCode, String businessKey, String businessForm,Participant creator) {
		return start(definitionCode, businessKey, businessForm, creator, null,null);
	}
	
	@Override
	public List<ProcessInstanceVo> listProcessInstanceByBusinessKey(String businessKey) {
		return processInstanceRepository.listProcessInstanceByBusinessKey(businessKey);
	}

	@Override
	public ProcessInstanceVo getRunningByBusinessKey(String definitionCode,String businessKey) {
		return processInstanceRepository.getRunningByBusinessKey(definitionCode,businessKey);
	}

	@Override
	public ProcessInstanceVo getProcessInstance(String processInstanceId) {
		return processInstanceRepository.get(processInstanceId);
	}

	@Override
	public void close(String processInstanceId, Participant participant, String comment) {
		taskRepository.removeAllTask(processInstanceId);
		processInstanceRepository.updateStatus(processInstanceId,ProcessInstanceVo.STATUS_CLOSE);
	}

	@Override
	public void closeByBusinessKey(String definitionCode, String businessKey,Participant participant, String comment) {
		ProcessInstanceVo processInstanceVo = processInstanceRepository.getRunningByBusinessKey(definitionCode, businessKey);
		taskRepository.removeAllTask(processInstanceVo.getId());
		processInstanceRepository.updateStatus(processInstanceVo.getId(),ProcessInstanceVo.STATUS_CLOSE);
	}

	@Override
	public Page<ProcessInstanceVo> query(ProcessInstanceCondition processInstanceCondition) {
		return processInstanceRepository.query(processInstanceCondition);
	}

	@Override
	public Page<ProcessInstanceVo> queryJoin(ProcessInstanceCondition processInstanceCondition) {
		return processInstanceRepository.queryJoin(processInstanceCondition);
	}

	@Override
	public List<ActivityInstance> listHistory(String processInstanceId) {
		return null;
	}

}
