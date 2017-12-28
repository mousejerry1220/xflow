package org.xsnake.cloud.xflow.dao.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.xflow.dao.DaoTemplate;
import org.xsnake.cloud.xflow.service.api.DefinitionCondition;
import org.xsnake.cloud.xflow.service.api.IDefinitionService;
import org.xsnake.cloud.xflow.service.api.Page;
import org.xsnake.cloud.xflow.service.api.vo.Definition;

@Component
public class DefinitionRepository {
	
	@Autowired
	DaoTemplate daoTemplate;
	
	/**
	 * 判断代码是否已经存在
	 * @param code
	 * @return
	 */
	public BigDecimal existCount(String code){
		return daoTemplate.queryBigDecimal(" SELECT COUNT(1) FROM XFLOW_DEFINITION WHERE CODE = ? ",new Object[]{code});
	}
	
	/**
	 * 新增数据
	 * @param definitionPo
	 */
	public void save(String code,String name,String remark){
		daoTemplate.update(" INSERT INTO XFLOW_DEFINITION (CODE,REMARK,STATUS,NAME,CREATE_TIME) VALUES (?,?,?,?,?) ",new Object[]{
			code,remark,IDefinitionService.STATUS_DISABLE,name,new Date()
		});
	}
	
	public void update(String code,String name,String remark){
		daoTemplate.update(" UPDATE XFLOW_DEFINITION SET NAME = ? , REMARK = ? WHERE CODE = ? " , 
				new Object[]{name,remark,code});
	}
	
	/**
	 * 查询
	 * @param pageCondition
	 * @return
	 */
	public Page<Definition> query(DefinitionCondition definitionCondition){
		StringBuffer sql = new StringBuffer(" SELECT * FROM VIEW_DEFINITION_LIST WHERE 1 = 1 ");
		List<Object> args = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(definitionCondition.getSearchKey())){
			sql.append(" and ( CODE LIKE ? or NAME LIKE ? ) ");
			args.add("%"+definitionCondition.getSearchKey()+"%");
			args.add("%"+definitionCondition.getSearchKey()+"%");
		}
		
		if(StringUtils.isNotEmpty(definitionCondition.getStatus()) && !"ALL".equals(definitionCondition.getStatus())){
			sql.append(" and STATUS = ? ");
			args.add(definitionCondition.getStatus());
		}
		
		sql.append(" ORDER BY CODE ");
		return daoTemplate.search(sql.toString(), args.toArray(), definitionCondition.getPage(), definitionCondition.getRows() , Definition.class);
	}
	
	/**
	 * 更新状态字段
	 * @param status
	 * @param code
	 */
	public void updateStatus(String status,String code){
		daoTemplate.update(" UPDATE XFLOW_DEFINITION SET STATUS = ? WHERE CODE = ? " , new Object[]{status,code});
	}
	
	/**
	 * 更新当前版本字段
	 * @param version
	 * @param code
	 */
	public void updateCurrentVersion(String code ,Long version){
		daoTemplate.update(" UPDATE XFLOW_DEFINITION SET CURRENT_VERSION = ? WHERE CODE = ? " , new Object[]{version,code});
	}
	
	
}
