package org.xsnake.cloud.xflow.core.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.IContext;
import org.xsnake.cloud.xflow.core.context.OperateContext;
import org.xsnake.cloud.xflow.core.context.ProcessInstanceContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;

public final class StartActivity extends AutomaticActivity {

	public StartActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	private static final long serialVersionUID = 1L;

	public List<Transition> doWork(ProcessInstanceContext context){
		String sql = "INSERT INTO XFLOW_PROCESS_INSTANCE_HISTORY(RECORD_ID,OPERATOR_ID,OPERATOR_NAME,OPERATOR_TYPE,SUGGESTION,OPERATE_TIME,OPERATE_TYPE,CREATE_TIME) VALUES ( ?,?,?,?,?,?,?,?)";
		List<Object> args = new ArrayList<Object>();
		args.add(context.getAttribute(IContext.RECORD_ID));
		args.add(context.getProcessInstance().getCreatorId());
		args.add(context.getProcessInstance().getCreatorName());
		args.add(context.getProcessInstance().getCreatorType());
		args.add(null);
		args.add(new Date());
		args.add(OperateContext.OperateType.start.toString());
		args.add(new Date());
		context.getApplicationContext().getDaoTemplate().update(sql,args.toArray());
		return toTransitionList;
	}

	@Override
	public void definitionValidate(ApplicationContext context) {
		if(toTransitionList.size() != 1){
			throw new XflowDefinitionException("开始活动有且只能有一个出口路径");
		}
	}
}
