/**
 * 
 */
package com.dayatang.koala.service;

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
public interface RuleManagement extends Serializable {
	
	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	public List executeStatelessRules(InputStream ruleSource, Map ruleProperty,	Map map, List params);

	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	public List executeStatelessRules(Reader ruleSource, Map ruleProperty, Map map, List params);

	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	public List executeStatelessRules(Object ruleSource, Map ruleProperty, Map map, List params);

	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	public List executeStatelessRules(String ruleSource, Map ruleProperty, Map map, List params);

	/**
	 * 得到StatelessRuleSession
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @return StatelessRuleSession
	 * @throws RuleRuntimeException
	 */
	public StatelessRuleSession assembleStatelessRuleSession(InputStream ruleSource, Map ruleProperty, Map map);

	/**
	 * 得到StatelessRuleSession
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @return StatelessRuleSession
	 * @throws RuleRuntimeException
	 */
	public StatelessRuleSession assembleStatelessRuleSession(Reader ruleSource, Map ruleProperty, Map map);

	/**
	 * 得到StatelessRuleSession
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @return StatelessRuleSession
	 * @throws RuleRuntimeException
	 */
	public StatelessRuleSession assembleStatelessRuleSession(Object ruleSource, Map ruleProperty, Map map);

	/**
	 * 得到StatelessRuleSession
	 * @param ruleSource 规则源
	 * @param ruleProperty 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param map 规则中的上下文（如全局变量等）
	 * @return StatelessRuleSession
	 * @throws RuleRuntimeException
	 */
	public StatelessRuleSession assembleStatelessRuleSession(String ruleSource, Map ruleProperty, Map map);

}
