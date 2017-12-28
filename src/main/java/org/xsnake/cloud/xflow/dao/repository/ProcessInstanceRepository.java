package org.xsnake.cloud.xflow.dao.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.xflow.dao.DaoTemplate;
import org.xsnake.cloud.xflow.service.api.IProcessInstanceService;
import org.xsnake.cloud.xflow.service.api.Page;
import org.xsnake.cloud.xflow.service.api.ProcessInstanceCondition;
import org.xsnake.cloud.xflow.service.api.vo.ProcessInstance;

@Component
public class ProcessInstanceRepository {
	
	@Autowired
	DaoTemplate daoTemplate;
	
	public void save(ProcessInstance processInstance){
		String sql = "INSERT INTO XFLOW_PROCESS_INSTANCE (ID,DEFINITION_CODE,DEFINITION_VERSION,BUSINESS_KEY,NAME,STATUS,PARENT_ID,START_TIME,CREATOR_ID,CREATOR_NAME,CREATOR_TYPE,Parent_Activity_Id ) values (?,?,?,?,?,?,?,?,?,?,?,?) ";
		daoTemplate.update(sql,new Object[]{
				processInstance.getId(),
				processInstance.getDefinitionCode(),
				processInstance.getDefinitionVersion(),
				processInstance.getBusinessKey(),
				processInstance.getName(),
				processInstance.getStatus(),
				processInstance.getParentId(),
				processInstance.getStartTime(),
				processInstance.getCreatorId(),
				processInstance.getCreatorName(),
				processInstance.getCreatorType(),
				processInstance.getParentActivityId()
		});
	}

	public List<ProcessInstance> listProcessInstanceByBusinessKey(String businessKey) {
		return daoTemplate.query(" SELECT * FROM XFLOW_PROCESS_INSTANCE WHERE BUSINESS_KEY = ? ", new Object[]{businessKey}, ProcessInstance.class);
	}

	public ProcessInstance getRunningByBusinessKey(String definitionCode,String businessKey) {
		return daoTemplate.queryForObject(" SELECT * FROM XFLOW_PROCESS_INSTANCE WHERE BUSINESS_KEY = ? AND STATUS = ? ", new Object[]{businessKey,IProcessInstanceService.STATUS_RUNNING}, ProcessInstance.class);
	}
	
	public ProcessInstance get(String id) {
		return daoTemplate.queryForObject(" SELECT * FROM XFLOW_PROCESS_INSTANCE WHERE ROW_ID = ? ", new Object[]{id}, ProcessInstance.class);
	}

	public void updateStatus(String processInstanceId, String status) {
		daoTemplate.update(" UPDATE XFLOW_PROCESS_INSTANCE SET STATUS = ? WHERE ID = ? " , new Object[]{status,processInstanceId});
	}

	public Page<ProcessInstance> query(ProcessInstanceCondition processInstanceCondition) {
		StringBuffer sql = new StringBuffer(" SELECT * FROM XFLOW_PROCESS_INSTANCE WHERE 1 = 1 ");
		List<Object> args = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(processInstanceCondition.getSearchKey())){
			sql.append(" and ( NAME LIKE ? ) ");
			args.add("%"+processInstanceCondition.getSearchKey()+"%");
		}
		sql.append(" ORDER BY START_TIME DESC ");
		return daoTemplate.search(sql.toString(), args.toArray(), processInstanceCondition.getPage(), processInstanceCondition.getRows() , ProcessInstance.class);
	}

	public Page<ProcessInstance> queryJoin(ProcessInstanceCondition processInstanceCondition) {
		return null;
	}
	
}
