package org.xsnake.cloud.xflow.dao.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.xflow.dao.DaoTemplate;
import org.xsnake.cloud.xflow.dao.repository.pojo.DefinitionPo;
import org.xsnake.cloud.xflow.service.api.Page;
import org.xsnake.cloud.xflow.service.api.PageCondition;
import org.xsnake.cloud.xflow.service.api.vo.DefinitionVo;

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
	public void save(DefinitionPo definitionPo){
		daoTemplate.update(" INSERT INTO XFLOW_DEFINITION (CODE,REMARK,STATUS,NAME) VALUES (?,?,?,?) ",new Object[]{
				definitionPo.getCode(),definitionPo.getRemark(),definitionPo.getStatus(),definitionPo.getName()
		});
	}
	
	/**
	 * 更新数据
	 * @param definitionPo
	 */
	public void update(DefinitionPo definitionPo){
		daoTemplate.update(" UPDATE XFLOW_DEFINITION SET NAME = ? , REMARK = ? WHERE CODE = ? " , new Object[]{definitionPo.getName(),definitionPo.getRemark(),definitionPo.getCode()});
	}
	
	/**
	 * 查询
	 * @param pageCondition
	 * @return
	 */
	public Page<DefinitionVo> query(PageCondition pageCondition){
		StringBuffer sql = new StringBuffer(" SELECT CODE,REMARK,STATUS,NAME,CURRENT_VERSION FROM XFLOW_DEFINITION WHERE 1 = 1 ");
		List<Object> args = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(pageCondition.getSearchKey())){
			sql.append(" and ( CODE LIKE ? or NAME LIKE ? ) ");
			args.add("%"+pageCondition.getSearchKey()+"%");
			args.add("%"+pageCondition.getSearchKey()+"%");
		}
		sql.append(" ORDER BY CODE ");
		return daoTemplate.search(sql.toString(), args.toArray(), pageCondition.getPage(), pageCondition.getRows() , DefinitionVo.class);
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
