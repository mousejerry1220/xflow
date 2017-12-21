package org.xsnake.cloud.xflow.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.xsnake.cloud.xflow.core.ProcessDefinition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.dao.DaoTemplate;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow.service.api.IDefinitionInstanceService;
import org.xsnake.cloud.xflow.service.api.Page;
import org.xsnake.cloud.xflow.service.api.PageCondition;
import org.xsnake.cloud.xflow.service.api.model.DefinitionInstance;

public class DefinitionInstanceServiceImpl implements IDefinitionInstanceService{

	public final static String STATUS_RELEASE = "release";
	
	public final static String STATUS_NEW = "new";
	
	@Autowired
	DaoTemplate daoTemplate;

	@Autowired
	ApplicationContext applicationContext;
	
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
		int version = 0;
		try{
			BigDecimal maxVersion = daoTemplate.queryBigDecimal(" SELECT MAX(VERSION) FROM XFLOW_DEFINITION_INSTANCE WHERE CODE = ? GROUP BY CODE ",new Object[]{code});
			version = maxVersion.intValue();
		}catch (Exception e) {
			version = 1;
		}
		
		daoTemplate.execute(" INSERT INTO XFLOW_DEFINITION_INSTANCE (CODE,VERSION,STATUS,CREATE_DATE,LAST_UPDATE_DATE,CONTENT_XML,REMARK) VALUES (?,?,?,?,?,?,?) ",new Object[]{
			code,version,STATUS_NEW,new Date(),new Date(),xml,remark
		});
		
	}

	@Override
	public void update(String code, int version, String xml,String remark) {
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
		BigDecimal count = daoTemplate.queryBigDecimal(" SELECT COUNT(1) FROM XFLOW_DEFINITION_INSTANCE WHERE CODE = ? AND VERSION = ? AND STATUS = ? ", new Object[]{code,version,STATUS_RELEASE});
		if(count.intValue() != 0){
			throw new XflowDefinitionException("流程定义实例已经被发布，不能再进行修改操作");
		}
		daoTemplate.execute(" UPDATE XFLOW_DEFINITION_INSTANCE SET CONTENT_XML = ? ,REMARK = ? WHERE CODE = ? AND VERSION = ? AND STATUS = ? ", new Object[]{xml,remark,code,version,STATUS_NEW});
	}

	@Override
	public void release(String code, int version) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		//修改自己的状态，修改后就不能再修改
		daoTemplate.execute(" UPDATE XFLOW_DEFINITION_INSTANCE SET STATUS = ? WHERE CODE = ? AND VERSION = ? ",new Object[]{STATUS_RELEASE,code,version});
		daoTemplate.execute(" UPDATE XFLOW_DEFINITION SET CURRENT_VERSION = ? WHERE CODE = ? ",new Object[]{code,version});
	}

	@Override
	public String getCurrentDefinitionXML(String code) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		return daoTemplate.queryString("select di.CONTENT_XML from XFLOW_DEFINITION_INSTANCE di, XFLOW_DEFINITION d where di.code = d.code and d.CURRENT_VERSION = di.version and d.code = ? ", new Object[]{code});
	}

	@Override
	public String getDefinitionXML(String code, int version) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		return daoTemplate.queryString("select di.CONTENT_XML from XFLOW_DEFINITION_INSTANCE di where di.code = ? and di.version = ? ", new Object[]{code,version});
	}

	@Override
	public Page<DefinitionInstance> queryInstance(String code, PageCondition pageCondition) {
		if(StringUtils.isEmpty(code)){
			throw new IllegalArgumentException("流程定义代码不能为空");
		}
		StringBuffer sql = new StringBuffer(" SELECT CODE,REMARK,STATUS,VERSION,CREATE_DATE,LAST_UPDATE_DATE FROM XFLOW_DEFINITION_INSTANCE WHERE CODE = ? ");
		List<Object> args = new ArrayList<Object>();
		args.add(code);
		if(StringUtils.isNotEmpty(pageCondition.getSearchKey())){
			sql.append(" and ( TITLE LIKE ? ) ");
			args.add("%"+pageCondition.getSearchKey()+"%");
		}
		
		sql.append(" ORDER BY VERSION DESC ");
		return daoTemplate.search(sql.toString(), args.toArray(), pageCondition.getPage(), pageCondition.getRows() , DefinitionInstance.class);
	}

}
