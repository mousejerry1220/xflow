package org.xsnake.cloud.xflow.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.xsnake.cloud.xflow.dao.DaoTemplate;
import org.xsnake.cloud.xflow.exception.XflowBusinessException;
import org.xsnake.cloud.xflow.service.api.IDefinitionService;
import org.xsnake.cloud.xflow.service.api.Page;
import org.xsnake.cloud.xflow.service.api.PageCondition;
import org.xsnake.cloud.xflow.service.api.model.Definition;

public class DefinitionServiceImpl implements IDefinitionService{

	@Autowired
	DaoTemplate daoTemplate;
	
	public static final String STATUS_DISABLE = "disable";
	
	public static final String STATUS_ENABLE = "enable";
	
	@Override
	public void create(String code, String name, String remark) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		if(StringUtils.isEmpty(name)){
			throw new IllegalArgumentException("流程定义标题不能为空");
		}
		//流程定义中code为主键，并且由用户定义，这里验证是否重复
		BigDecimal count = daoTemplate.queryBigDecimal(" SELECT COUNT(1) FROM XFLOW_DEFINITION WHERE CODE = ? ",new Object[]{code});
		if(count.intValue() > 0){
			throw new XflowBusinessException("流程定义代码已经存在");
		}
		//新增流程定义，初始状态为不可用，必须有了定义实例后才可以生效
		daoTemplate.execute(" INSERT INTO XFLOW_DEFINITION (CODE,REMARK,STATUS,NAME) VALUES (?,?,?,?) ",new Object[]{
			code,remark,STATUS_DISABLE,name
		});
	}

	@Override
	public void update(String code, String name, String remark) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		daoTemplate.execute(" UPDATE XFLOW_DEFINITION SET NAME = ? , REMARK = ? WHERE CODE = ? " , new Object[]{name,remark,code});
	}

	@Override
	public Page<Definition> query(PageCondition pageCondition) {
		StringBuffer sql = new StringBuffer(" SELECT CODE,REMARK,STATUS,NAME,CURRENT_VERSION FROM XFLOW_DEFINITION WHERE 1 = 1 ");
		List<Object> args = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(pageCondition.getSearchKey())){
			sql.append(" and ( CODE LIKE ? or NAME LIKE ? ) ");
			args.add("%"+pageCondition.getSearchKey()+"%");
			args.add("%"+pageCondition.getSearchKey()+"%");
		}
		sql.append(" ORDER BY CODE ");
		return daoTemplate.search(sql.toString(), args.toArray(), pageCondition.getPage(), pageCondition.getRows() , Definition.class);
	}

	@Override
	public void disable(String code) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		daoTemplate.execute(" UPDATE XFLOW_DEFINITION SET STATUS = ? WHERE CODE = ? " , new Object[]{STATUS_DISABLE,code});
	}

	@Override
	public void enable(String code) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		daoTemplate.execute(" UPDATE XFLOW_DEFINITION SET STATUS = ? WHERE CODE = ? " , new Object[]{STATUS_ENABLE,code});
	}

}
