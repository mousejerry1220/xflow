package org.xsnake.cloud.xflow.activity.automatic;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.context.IXflowContext;

public class ForkActivity extends AutomaticActivity {

	public ForkActivity(Element activityElement) {
		super(activityElement);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void doWork(IXflowContext context){
		
	}

	@Override
	public void definitionValidate() {
		
	}
}
