package org.xsnake.cloud.xflow.core.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsnake.cloud.xflow.core.register.ActivityRegister;
import org.xsnake.cloud.xflow.core.register.ParticipantHandleRegister;
import org.xsnake.cloud.xflow.dao.DaoTemplate;
import org.xsnake.cloud.xflow.dao.repository.DefinitionInstanceXMLRepository;
import org.xsnake.cloud.xflow.service.api.IProcessInstanceService;

@Component
public class ApplicationContext extends Context{
	
	@Autowired
	private ActivityRegister activityRegister;
	
	@Autowired
	private ParticipantHandleRegister participantHandleRegister;
	
	@Autowired
	private DaoTemplate daoTemplate;
	
	@Autowired
	private IProcessInstanceService processInstanceService;

	@Autowired
	private DefinitionInstanceXMLRepository definitionInstanceXMLRepository;
	
	public ActivityRegister getActivityRegister() {
		return activityRegister;
	}

	public ParticipantHandleRegister getParticipantHandleRegister() {
		return participantHandleRegister;
	}

	public DaoTemplate getDaoTemplate() {
		return daoTemplate;
	}

	public IProcessInstanceService getProcessInstanceService() {
		return processInstanceService;
	}

	public DefinitionInstanceXMLRepository getDefinitionInstanceXMLRepository() {
		return definitionInstanceXMLRepository;
	}
	
}
