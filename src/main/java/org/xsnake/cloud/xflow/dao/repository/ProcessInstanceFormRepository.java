package org.xsnake.cloud.xflow.dao.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.xflow.dao.DaoTemplate;

@Component
public class ProcessInstanceFormRepository {
	
	@Autowired
	DaoTemplate daoTemplate;
	
	public void save(String id,String businessForm){
		daoTemplate.update("INSERT INTO XFLOW_PROCESS_INSTANCE_FORM (ID, BUSINESS_FORM) VALUES (?,?)" , new Object[]{id,businessForm});
	}
	
	public String getBusinessForm(String id){
		return daoTemplate.queryForString(" SELECT BUSINESS_FORM FROM XFLOW_PROCESS_INSTANCE_FORM WHERE ID = ? ",new Object[]{id});
	}
	
	public void updateFinalBusinessForm(String id,String finalBusinessForm){
		daoTemplate.update(" UPDATE XFLOW_PROCESS_INSTANCE_FORM SET FINAL_BUSINESS_FORM = ? WHERE ID = ? ",new Object[]{id,finalBusinessForm});
	}
	
	public String getFinalBusinessForm(String id){
		return daoTemplate.queryForString(" SELECT FINAL_BUSINESS_FORM FROM XFLOW_PROCESS_INSTANCE_FORM WHERE ID = ? ",new Object[]{id});
	}

}
