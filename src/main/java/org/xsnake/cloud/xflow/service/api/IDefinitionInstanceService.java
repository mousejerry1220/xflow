package org.xsnake.cloud.xflow.service.api;

public interface IDefinitionInstanceService {
	
	//创建/升级流程定义实例
	void create(String code,String xml);

	//更新一个未发布的定义实例，如果定义实例一旦被发布，则不允许再被编辑，只能升级
	void update(String code,int version,String xml);
	
	//发布定义实例未该定义的正式版本，一次只能存在一个正式版本
	void release(String code,int version);
	
	//如果一个定义实例未曾被任何业务使用过，则可以删除，且不能是正使用状态
	void delete(String code,int version);
	
	//获得当前被使用的定义实例XML
	String getCurrentDefinitionXML(String code);

	//获得指定版本的定义实例XML
	String getDefinitionXML(String code,int version);
	
}
