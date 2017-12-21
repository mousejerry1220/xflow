package org.xsnake.cloud.xflow.dao.repository.pojo;

public class DefinitionInstancePo {
	
	public DefinitionInstancePo(){}
	
	public DefinitionInstancePo(String code, Long version, String remark) {
		
		this.code = code;
		
		this.version = version;
		
		this.remark = remark;
		
	}
	
	private String code;
	
	private Long version;
	
	private String remark;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
