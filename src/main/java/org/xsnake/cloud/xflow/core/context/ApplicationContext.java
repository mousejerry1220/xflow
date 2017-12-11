package org.xsnake.cloud.xflow.core.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.xflow.dao.DaoCenter;

@Component
public class ApplicationContext extends Context{
	
	@Autowired
	private DaoCenter daoCenter;
	
	public DaoCenter getDaoCenter() {
		return daoCenter;
	}
    
}
