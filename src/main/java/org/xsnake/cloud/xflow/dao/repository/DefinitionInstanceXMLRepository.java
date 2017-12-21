package org.xsnake.cloud.xflow.dao.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.xflow.dao.DaoTemplate;

@Component
public class DefinitionInstanceXMLRepository {

	@Autowired
	DaoTemplate daoTemplate;
	
	public void save(String code,Long version,String xml){
		daoTemplate.update(" INSERT INTO XFLOW_DEFINITION_INSTANCE_XML (CODE,VERSION,XML) VALUES(?,?,?) " , new Object[]{code,version,xml});
	}
	
	public void update(String code,Long version,String xml){
		daoTemplate.update(" UPDATE XFLOW_DEFINITION_INSTANCE SET XML = ? WHERE CODE = ? AND VERSION = ? ",new Object[]{xml,code,version});
	}
	
	public String getXML(String code, Long version) {
		return daoTemplate.queryForString("SELECT XML FROM XFLOW_DEFINITION_INSTANCE_XML WHERE CODE = ? AND VERSION = ? ", new Object[]{code,version});
	}
	
}
