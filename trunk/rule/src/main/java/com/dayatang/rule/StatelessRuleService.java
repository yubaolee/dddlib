/**
 * 
 */
package com.dayatang.rule;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.rules.StatelessRuleSession;

/**
 * 
 * 规则组件对外提供的接口，封装了JSR94的实现
 * 
 * @author cchen <a href="mailto:chencao0524@gmail.com">陈操</a>
 * 
 * @version $LastChangedRevision$ $LastChangedBy$ $LastChangedDate$
 * 
 */
@SuppressWarnings("unchecked")
public interface StatelessRuleService extends Serializable {
	
	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	public List executeRules(InputStream ruleSource, Map ruleProperty,	Map map, List params);

	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	public List executeRules(Reader ruleSource, Map ruleProperty, Map map, List params);

	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	public List executeRules(Object ruleSource, Map ruleProperty, Map map, List params);

	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	public List executeRules(String ruleSource, Map ruleProperty, Map map, List params);

	/**
	 * 得到StatelessRuleSession
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @return StatelessRuleSession
	 * @throws RuleRuntimeException
	 */
	public StatelessRuleSession assembleRuleSession(InputStream ruleSource, Map ruleProperty, Map map);

	/**
	 * 得到StatelessRuleSession
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @return StatelessRuleSession
	 * @throws RuleRuntimeException
	 */
	public StatelessRuleSession assembleRuleSession(Reader ruleSource, Map ruleProperty, Map map);

	/**
	 * 得到StatelessRuleSession
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @return StatelessRuleSession
	 * @throws RuleRuntimeException
	 */
	public StatelessRuleSession assembleRuleSession(Object ruleSource, Map ruleProperty, Map map);

	/**
	 * 得到StatelessRuleSession
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @return StatelessRuleSession
	 * @throws RuleRuntimeException
	 */
	public StatelessRuleSession assembleRuleSession(String ruleSource, Map ruleProperty, Map map);

}
