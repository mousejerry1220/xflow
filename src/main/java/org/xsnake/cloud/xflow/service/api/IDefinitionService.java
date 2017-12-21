package org.xsnake.cloud.xflow.service.api;

import org.xsnake.cloud.xflow.service.api.vo.DefinitionVo;
/**
 * 
 * @author Jerry.Zhao
 *
 */
public interface IDefinitionService {
	
	void create(String code,String name,String remark);
	
	void update(String code,String name,String remark);
	/**
	 * 查看所有的定义数据
	 * @param keyword
	 * @return
	 */
	Page<DefinitionVo> query(PageCondition pageCondition);
	
	/**
	 * 删除流程定义
	 * @param code
	 */
	void disable(String code);
	
	void enable(String code);
}
