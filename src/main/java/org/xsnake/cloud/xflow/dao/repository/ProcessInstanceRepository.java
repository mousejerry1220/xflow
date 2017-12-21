package org.xsnake.cloud.xflow.dao.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.xflow.dao.DaoTemplate;
import org.xsnake.cloud.xflow.dao.repository.pojo.ProcessInstancePo;
import org.xsnake.cloud.xflow.service.api.Page;
import org.xsnake.cloud.xflow.service.api.ProcessInstanceCondition;
import org.xsnake.cloud.xflow.service.api.vo.ProcessInstanceVo;

@Component
public class ProcessInstanceRepository {
	
	@Autowired
	DaoTemplate daoTemplate;
	
	public void save(ProcessInstancePo processInstancePo){
		String sql = "INSERT INTO XFLOW_PROCESS_INSTANCE (ID,DEFINITION_CODE,DEFINITION_VERSION,BUSINESS_KEY,BUSINESS_FORM,NAME,STATUS,PARENT_ID,START_TIME,CREATOR_ID,CREATOR_NAME,CREATOR_TYPE,Parent_Activity_Id ) values (?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		daoTemplate.update(sql,new Object[]{
				processInstancePo.getId(),
				processInstancePo.getDefinitionCode(),
				processInstancePo.getDefinitionVersion(),
				processInstancePo.getBusinessKey(),
				processInstancePo.getBusinessForm(),
				processInstancePo.getName(),
				processInstancePo.getStatus(),
				processInstancePo.getParentId(),
				processInstancePo.getStartTime(),
				processInstancePo.getCreatorId(),
				processInstancePo.getCreatorName(),
				processInstancePo.getCreatorType(),
				processInstancePo.getParentActivityId()
		});
	}

	public List<ProcessInstanceVo> listProcessInstanceByBusinessKey(String businessKey) {
		return daoTemplate.query(" SELECT * FROM XFLOW_PROCESS_INSTANCE WHERE BUSINESS_KEY = ? ", new Object[]{businessKey}, ProcessInstanceVo.class);
	}

	public ProcessInstanceVo getRunningByBusinessKey(String definitionCode,String businessKey) {
		return daoTemplate.queryForObject(" SELECT * FROM XFLOW_PROCESS_INSTANCE WHERE BUSINESS_KEY = ? AND STATUS = ? ", new Object[]{businessKey,ProcessInstanceVo.STATUS_RUNNING}, ProcessInstanceVo.class);
	}
	
	public ProcessInstanceVo get(String id) {
		return daoTemplate.queryForObject(" SELECT * FROM XFLOW_PROCESS_INSTANCE WHERE ROW_ID = ? ", new Object[]{id}, ProcessInstanceVo.class);
	}

	public void updateStatus(String processInstanceId, String status) {
		daoTemplate.update(" UPDATE XFLOW_PROCESS_INSTANCE SET STATUS = ? WHERE ID = ? " , new Object[]{status,processInstanceId});
	}

	public Page<ProcessInstanceVo> query(ProcessInstanceCondition processInstanceCondition) {
		StringBuffer sql = new StringBuffer(" SELECT * FROM XFLOW_PROCESS_INSTANCE WHERE 1 = 1 ");
		List<Object> args = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(processInstanceCondition.getSearchKey())){
			sql.append(" and ( NAME LIKE ? ) ");
			args.add("%"+processInstanceCondition.getSearchKey()+"%");
		}
		sql.append(" ORDER BY START_TIME DESC ");
		return daoTemplate.search(sql.toString(), args.toArray(), processInstanceCondition.getPage(), processInstanceCondition.getRows() , ProcessInstanceVo.class);
	}

	public Page<ProcessInstanceVo> queryJoin(ProcessInstanceCondition processInstanceCondition) {
		return null;
	}
	
}
