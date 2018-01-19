package org.xsnake.cloud.xflow.core.participant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.DefinitionConstant;
import org.xsnake.cloud.xflow.core.ParticipantHandle;
import org.xsnake.cloud.xflow.core.context.ProcessInstanceContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;
import org.xsnake.cloud.xflow.service.api.Participant;

public class AssignParticipant extends ParticipantHandle implements Serializable{

	private static final long serialVersionUID = 1L;

	public AssignParticipant(Element participantElement) {
		super(participantElement);
		List<Element> assignList = participantElement.elements(DefinitionConstant.ELEMENT_ACTIVITY_PARTICIPANT_ASSIGN);
		for(Element assignElement : assignList){
			String id = assignElement.attributeValue("id");
			String name = assignElement.attributeValue("name");
			String type = assignElement.attributeValue("type");
			if(StringUtils.isEmpty(id)){
				throw new XflowDefinitionException("指派参与者ID不能为空");
			}
			if(StringUtils.isEmpty(name)){
				throw new XflowDefinitionException("指派参与者NAME不能为空");
			}
			if(StringUtils.isEmpty(type)){
				throw new XflowDefinitionException("指派参与者TYPE不能为空");
			}
			list.add(new Participant(id,name,type));
		}
	}

	final private List<Participant> list = new ArrayList<Participant>();
	
	@Override
	protected List<Participant> findParticipantList(ProcessInstanceContext context) {
		return new ArrayList<Participant>(list);
	}
	
}