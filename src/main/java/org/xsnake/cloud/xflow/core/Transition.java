package org.xsnake.cloud.xflow.core;

import java.io.Serializable;

public class Transition implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;

	private String name;
	
	private String sourceId;
	
	private String targetId;

	public Activity getSourceActivity(ProcessDefinition processDefinition) {
		return null;
	}

	public Activity getTargetActivity(ProcessDefinition processDefinition) {
		return null;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

}
