package org.xsnake.cloud.xflow.service.api;

import java.util.Date;
/**
 * 
 * @author Jerry.Zhao
 *
 */
public class ProcessInstanceCondition extends PageCondition{

	private static final long serialVersionUID = 1L;

	String id;
	
	String definitionCode;
	
	int definitionVersion;
	
	String definitionName;
	
	String businessKey;
	
	String businessType;
	
	String name;
	
	String status;
	
	String parentId;
	
	Date startTime;
	
	String creatorId;
	
	String creatorName;
	
	String creatorType;
	
	Date endTime;
	
}
