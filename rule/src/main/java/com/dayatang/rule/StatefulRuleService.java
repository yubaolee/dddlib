/**
 * 
 */
package com.dayatang.rule;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.rules.RuleRuntime;
import javax.rules.StatefulRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;

/**
 * 
 * 规则组件对外提供的接口，封装了JSR94的实现
 * 
 * @author cchen <a href="mailto:chencao0524@gmail.com">陈操</a>
 * 
 * @version $LastChangedRevision$ $LastChangedBy$ $LastChangedDate$
 * 
 */
@SuppressWarnings("rawtypes")
public interface StatefulRuleService extends Serializable {

	/**
	 * 执行规则
	 * @param ruleSource 规则源
	 * @param executionSetProperties 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	void executeRules(String ruleSource, Map executionSetProperties, Map sessionProperties, List params);
	
	/**
	 * 执行规则
	 * @param ruleSource 规则源
	 * @param executionSetProperties 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	void executeRules(InputStream ruleSource, Map executionSetProperties, Map sessionProperties, List params);

	/**
	 * 执行规则
	 * @param ruleSource 规则源
	 * @param executionSetProperties 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	void executeRules(Reader ruleSource, Map executionSetProperties, Map sessionProperties, List params);

	/**
	 * 执行规则
	 * @param ruleSource 规则源
	 * @param executionSetProperties 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	void executeRules(Object ruleSource, Map executionSetProperties, Map sessionProperties, List params);

	/**
	 * 执行规则
	 * @param ruleSource 规则源
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	void executeRules(String ruleSource, List params);
	
	/**
	 * 执行规则
	 * @param ruleSource 规则源
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	void executeRules(InputStream ruleSource, List params);

	/**
	 * 执行规则
	 * @param ruleSource 规则源
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	void executeRules(Reader ruleSource, List params);

	/**
	 * 执行规则
	 * @param ruleSource 规则源
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	void executeRules(Object ruleSource, List params);

	/**
	 * 获得有状态规则会话。由客户代码负责释放该会话。
	 * @param ruleSource 规则源
	 * @param executionSetProperties 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 * @return
	 */
	StatefulRuleSession getRuleSession(Object ruleSource, Map executionSetProperties, Map sessionProperties);
	
	/**
	 * 获取本地规则执行集
	 * @return
	 */
	LocalRuleExecutionSetProvider getRuleExecutionSetProvider();
	
	/**
	 * 获取规则运行时
	 * @return
	 */
	RuleRuntime getRuleRuntime();
	
	/**
	 * 获取规则管理器
	 * @return
	 */
	RuleAdministrator getRuleAdministrator();
}
