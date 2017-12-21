package org.xsnake.cloud.xflow.dao.repository.pojo;

import java.io.Serializable;
import java.util.Date;

public class ProcessInstancePo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	String id;
	
	String definitionCode;
	
	Long definitionVersion;
	
	String businessKey;
	
	String businessForm;
	
	String name;
	
	String status;
	
	String parentId;
	
	String parentActivityId;
	
	Date startTime;
	
	String creatorId;
	
	String creatorName;
	
	String creatorType;
	
	Date endTime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDefinitionCode() {
		return definitionCode;
	}

	public void setDefinitionCode(String definitionCode) {
		this.definitionCode = definitionCode;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreatorType() {
		return creatorType;
	}

	public void setCreatorType(String creatorType) {
		this.creatorType = creatorType;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getDefinitionVersion() {
		return definitionVersion;
	}

	public void setDefinitionVersion(Long definitionVersion) {
		this.definitionVersion = definitionVersion;
	}

	public String getBusinessForm() {
		return businessForm;
	}

	public void setBusinessForm(String businessForm) {
		this.businessForm = businessForm;
	}

	public String getParentActivityId() {
		return parentActivityId;
	}

	public void setParentActivityId(String parentActivityId) {
		this.parentActivityId = parentActivityId;
	}

}
