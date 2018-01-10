package org.xsnake.cloud.xflow.core.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.ProcessInstanceContext;
import org.xsnake.cloud.xflow.exception.XflowDefinitionException;

import com.alibaba.fastjson.JSONObject;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
/**
 * 2018/1/15
 * 决策任务
 * @author Jerry.Zhao
 *
 */
public class DecisionActivity extends AutomaticActivity {

	private static final long serialVersionUID = 1L;

	String expression;
	
	public DecisionActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
		expression = activityElement.elementText("expression");
		if(StringUtils.isEmpty(expression)){
			throw new XflowDefinitionException("判断节点定义必须包含表达式");
		}
	}

	@Override
	public List<Transition> doWork(ProcessInstanceContext context) {
		String newExpression = null;
		try {
			newExpression = getString(expression, JSONObject.parseObject(context.getBusinessForm()));
			String to = String.valueOf(getEngine().eval("function test() {" + newExpression + "} test()"));
			if (StringUtils.isEmpty(to)) {
				throw new XflowDefinitionException("表达式错误，没有返回一个正确的流转名称");
			}
			List<Transition> resultList = new ArrayList<Transition>();
			for(Transition toTransition : toTransitionList){
				if(to.equals(toTransition.getId()) || to.equals(toTransition.getName())){
					resultList.add(toTransition);
				}
			}
			if(resultList.size() == 0){
				throw new XflowDefinitionException("表达式所返回了未知的流转");
			}
			return resultList;
		} catch (Exception e) {
			throw new XflowDefinitionException("表达式解析异常：" + e.getMessage());
		}
	}
	
	private static ScriptEngine getEngine(){
		ScriptEngineManager sfm = new ScriptEngineManager(); 
		ScriptEngine jsEngine = sfm.getEngineByName("JavaScript"); 
        if (jsEngine == null) { 
            throw new RuntimeException("找不到 JavaScript 引擎。"); 
        }
        return jsEngine;
	}

	private final static String DEFAULT_TEMPLATE_NAME = "template";
	
	private static String getString(String sql,Map<String,Object> map) throws Exception{
		FreeMarkerConfigurer config = new FreeMarkerConfigurer();
		StringTemplateLoader loader = new StringTemplateLoader();
		loader.putTemplate(DEFAULT_TEMPLATE_NAME, sql);
		Configuration configuration;
		try {
			configuration = config.createConfiguration();
			configuration.setTemplateLoader(loader);
			String result = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate(DEFAULT_TEMPLATE_NAME), map);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} 
	}
}
