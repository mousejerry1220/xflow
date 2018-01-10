package org.xsnake.cloud.xflow.core.activity;

import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.ParticipantActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.OperateContext;
/**
 * 2018/1/15
 * 多人任务/会签任务
 * @author Jerry.Zhao
 *
 */
public class MultiTaskActivity extends ParticipantActivity {

	public MultiTaskActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	private static final long serialVersionUID = 1L;
	
	
	@Override
	public List<Transition> doTask(OperateContext context) {
		//什么都不做，任务会在父类中被删除
		return toTransitionList;
	}
	
}
