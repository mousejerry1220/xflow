package org.xsnake.cloud.xflow.activity.automatic;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.context.IXflowContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;

public final class StartActivity extends AutomaticActivity {

	public StartActivity(Element activityElement) {
		super(activityElement);
	}

	private static final long serialVersionUID = 1L;

	public void doWork(IXflowContext context){
		//创建并缓存流程实例
		
		//发送事件消息
	}

	@Override
	public void definitionValidate() {
		if(toTransitionList.size() != 1){
			throw new XflowDefinitionException("开始活动有且只能有一个出口路径");
		}
	}
}
