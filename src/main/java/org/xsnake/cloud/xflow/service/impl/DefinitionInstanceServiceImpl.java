package org.xsnake.cloud.xflow.service.impl;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xsnake.cloud.xflow.core.ProcessDefinition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.dao.repository.DefinitionInstanceRepository;
import org.xsnake.cloud.xflow.dao.repository.DefinitionInstanceXMLRepository;
import org.xsnake.cloud.xflow.dao.repository.DefinitionRepository;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow.service.api.IDefinitionInstanceService;
import org.xsnake.cloud.xflow.service.api.Page;
import org.xsnake.cloud.xflow.service.api.PageCondition;
import org.xsnake.cloud.xflow.service.api.vo.DefinitionInstance;
@Service
public class DefinitionInstanceServiceImpl implements IDefinitionInstanceService{

	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	DefinitionInstanceRepository definitionInstanceRepository;
	
	@Autowired
	DefinitionRepository definitionRepository;
	
	@Autowired
	DefinitionInstanceXMLRepository definitionInstanceXMLRepository;
	
	@Override
	public void create(String code, final String xml,String remark) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		
		if(StringUtils.isEmpty(xml)){
			throw new IllegalArgumentException("流程定义XML内容不能为空");
		}
		
		try {
			ProcessDefinition.parse(applicationContext,code,xml);
		} catch (DocumentException e) {
			throw new XflowDefinitionException("流程定义XML有错误：" + e.getMessage());
		}
		
		//生成最新版本的版本号码
		BigDecimal maxVersion = definitionInstanceRepository.currentMaxVersion(code);
		if(maxVersion == null){
			maxVersion = new BigDecimal(0);
		}
		Long version = maxVersion.longValue() + 1;
		
		DefinitionInstance definitionInstance = new DefinitionInstance();
		definitionInstance.setCode(code);
		definitionInstance.setVersion(version);
		definitionInstance.setRemark(remark);
		
		definitionInstanceRepository.save(definitionInstance);
		definitionInstanceXMLRepository.save(code, version, xml);
	}

	public void update(String code, Long version, String xml,String remark) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		if(StringUtils.isEmpty(xml)){
			throw new IllegalArgumentException("流程定义XML内容不能为空");
		}
		try {
			ProcessDefinition.parse(applicationContext,code,xml);
		} catch (DocumentException e) {
			throw new XflowDefinitionException("流程定义XML有错误：" + e.getMessage());
		}
		//判断状态是否已经发布
		DefinitionInstance definitionInstance = definitionInstanceRepository.get(code, version);
		
		if(definitionInstance == null){
			throw new XflowDefinitionException("没有找到要更新的数据");
		}
		
		if(STATUS_RELEASE.equals(definitionInstance.getStatus())){
			throw new XflowDefinitionException("流程定义实例已经被发布，不能再进行修改操作");
		}
		
		definitionInstance = new DefinitionInstance();
		definitionInstance.setCode(code);
		definitionInstance.setVersion(version);
		definitionInstance.setRemark(remark);
		definitionInstanceRepository.update(definitionInstance);
		definitionInstanceXMLRepository.update(code, version, xml);
	}

	@Override
	public void release(String code, Long version) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		//修改自己的状态，修改后就不能再修改
		definitionInstanceRepository.updateStatus(code, version, STATUS_RELEASE);
		definitionRepository.updateCurrentVersion(code,version);
	}

	@Override
	public String getXML(String code, Long version) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		return definitionInstanceXMLRepository.getXML(code, version);
	}

	@Override
	public Page<DefinitionInstance> query(String code, PageCondition pageCondition) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		return definitionInstanceRepository.query(code, pageCondition);

	}

}
