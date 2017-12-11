package org.xsnake.cloud.xflow.activity;

import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.Endedable;
import org.xsnake.cloud.xflow.core.context.IXflowContext;

public final class EndActivity extends AutomaticActivity implements Endedable {

	private static final long serialVersionUID = 1L;

	public void doWork(IXflowContext context){
		//移除缓存的流程实例
	}

	@Override
	public void definitionValidate() {
		
	}

	@Override
	public boolean getResult() {
		return true;
	}
	
}
