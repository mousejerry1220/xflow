package org.xsnake.cloud.xflow.service.api;

import org.xsnake.cloud.xflow.service.api.vo.DefinitionInstanceVo;

/**
 * 定义实例的状态有，未发布，已发布。
 * @author Jerry.Zhao
 *
 */
public interface IDefinitionInstanceService {
	
	/**
	 * 创建/升级流程定义实例
	 * @param code
	 * @param xml
	 */
	void create(String code,String xml,String remark);

	/**
	 * 更新一个未发布的定义实例，如果定义实例一旦被发布，则不允许再被编辑，只能升级
	 * @param code
	 * @param version
	 * @param xml
	 */
	void update(String code, Long version, String xml,String remark);
	/**
	 * 发布定义实例未该定义的正式版本，一次只能存在一个正式版本
	 * @param code
	 * @param version
	 */
	void release(String code,Long version);
	
	/**
	 * 获得指定版本的定义实例XML
	 * @param code
	 * @param version
	 * @return
	 */
	String getXML(String code,Long version);
	
	/**
	 * 查看定义下所有的定义实例
	 * @param code
	 * @param pageCondition
	 * @return
	 */
	Page<DefinitionInstanceVo> query(String code,PageCondition pageCondition);

}
