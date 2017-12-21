package org.xsnake.cloud.xflow.dao.repository.pojo;

public class DefinitionPo {
	
	public DefinitionPo(String code,String name,String remark){
		this.code = code; 
		this.name = name;
		this.remark = remark;
	}
	
	private String code;
	
	private Long currentVersion;
	
	private String name;
	
	private String remark;
	
	private String status;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(Long currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
