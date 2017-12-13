package org.xsnake.cloud.xflow.participant;

import java.io.Serializable;
import java.util.List;

import org.xsnake.cloud.xflow.core.ParticipantHandle;
import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.service.api.Participant;

public class AssignParticipant extends ParticipantHandle implements Serializable{

	private static final long serialVersionUID = 1L;

	List<Participant> list;
	
	@Override
	protected List<Participant> _findParticipantList(IXflowContext context) {
		return null;
	}

	public List<Participant> getList() {
		return list;
	}

	public void setList(List<Participant> list) {
		this.list = list;
	}
	
}