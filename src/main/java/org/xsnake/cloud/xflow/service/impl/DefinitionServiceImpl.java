package org.xsnake.cloud.xflow.service.impl;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xsnake.cloud.xflow.dao.repository.DefinitionRepository;
import org.xsnake.cloud.xflow.exception.XflowBusinessException;
import org.xsnake.cloud.xflow.service.api.DefinitionCondition;
import org.xsnake.cloud.xflow.service.api.IDefinitionService;
import org.xsnake.cloud.xflow.service.api.Page;
import org.xsnake.cloud.xflow.service.api.vo.Definition;


@Service
@RestController
@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
public class DefinitionServiceImpl implements IDefinitionService{
	
	@Autowired
	DefinitionRepository definitionRepository;
	
	@Override
	public void create(String code, String name, String remark) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		if(StringUtils.isEmpty(name)){
			throw new IllegalArgumentException("流程定义标题不能为空");
		}
		//流程定义中code为主键，并且由用户定义，这里验证是否重复
		BigDecimal count = definitionRepository.existCount(code);
		if(count !=null && count.longValue() > 0){
			throw new XflowBusinessException("流程定义代码已经存在");
		}
		//新增流程定义，初始状态为不可用，必须有了定义实例后才可以生效
		definitionRepository.save(code,name,remark);
	}

	@Override
	public void update(String code, String name, String remark) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		definitionRepository.update(code,name,remark);
	}

	@Override
	public Page<Definition> query(@RequestBody DefinitionCondition definitionCondition) {
		return definitionRepository.query(definitionCondition);
	}

	@Override
	public void disable(String code) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		definitionRepository.updateStatus(STATUS_DISABLE,code);
	}

	@Override
	public void enable(String code) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		definitionRepository.updateStatus(STATUS_ENABLE,code);
	}

}
