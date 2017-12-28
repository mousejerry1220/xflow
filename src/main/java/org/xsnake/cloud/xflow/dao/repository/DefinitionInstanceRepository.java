package org.xsnake.cloud.xflow.dao.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.xflow.dao.DaoTemplate;
import org.xsnake.cloud.xflow.service.api.Page;
import org.xsnake.cloud.xflow.service.api.PageCondition;
import org.xsnake.cloud.xflow.service.api.vo.DefinitionInstance;

@Component
public class DefinitionInstanceRepository {
	
	public final static String STATUS_RELEASE = "release";
	
	public final static String STATUS_NEW = "new";
	
	@Autowired
	DaoTemplate daoTemplate;
	
	public BigDecimal currentMaxVersion(String code){
		try{
			return daoTemplate.queryBigDecimal(" SELECT MAX(VERSION) FROM XFLOW_DEFINITION_INSTANCE WHERE CODE = ? GROUP BY CODE ",new Object[]{code});
		}catch (Exception e) {
			return new BigDecimal(0);
		}
	}

	public void save(DefinitionInstance definitionInstance){
		daoTemplate.update(" INSERT INTO XFLOW_DEFINITION_INSTANCE (CODE,VERSION,STATUS,CREATE_DATE,LAST_UPDATE_DATE,REMARK) VALUES (?,?,?,?,?,?) ",
		new Object[]{
				definitionInstance.getCode(),
				definitionInstance.getVersion(),
				STATUS_NEW,
				new Date(),
				new Date(),
				definitionInstance.getRemark()
		});
	}
	
	public DefinitionInstance get(String code,Long version){
		return daoTemplate.queryForObject("SELECT CODE , VERSION , STATUS, CREATE_DATE , LAST_UPDATE_DATE, REMARK FROM XFLOW_DEFINITION_INSTANCE WHERE CODE = ? AND VERSION = ? ", 
				new Object[]{code,version},
				DefinitionInstance.class);
	}
	
	public void update(DefinitionInstance definitionInstance){
		daoTemplate.update(" UPDATE XFLOW_DEFINITION_INSTANCE SET REMARK = ? , LAST_UPDATE_DATE = ? WHERE CODE = ? AND VERSION = ? AND STATUS = ? ", 
		new Object[]{
				definitionInstance.getRemark(),
				new Date(),
				definitionInstance.getCode(),
				definitionInstance.getVersion(),
				STATUS_NEW
		});
	}
	
	public void updateStatus(String code,Long version,String status){
		daoTemplate.update(" UPDATE XFLOW_DEFINITION_INSTANCE SET STATUS = ? WHERE CODE = ? AND VERSION = ? ",new Object[]{status,code,version});
	}
	
	public BigDecimal getCurrentVersion(String code){
		return daoTemplate.queryBigDecimal(" SELECT IFNULL(CURRENT_VERSION , 0) FROM XFLOW_DEFINITION WHERE CODE = ? ", new Object[]{code});
	}
	
	public Page<DefinitionInstance> query(String code ,PageCondition pageCondition){
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
