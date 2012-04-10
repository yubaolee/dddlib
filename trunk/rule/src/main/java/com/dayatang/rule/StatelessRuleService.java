/**
 * 
 */
package com.dayatang.rule;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
public interface StatelessRuleService extends Serializable {

	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param executionSetProperties 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	List executeRules(String ruleSource, Map executionSetProperties, Map sessionProperties, List params);
	
	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param executionSetProperties 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	List executeRules(InputStream ruleSource, Map executionSetProperties, Map sessionProperties, List params);

	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param executionSetProperties 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	List executeRules(Reader ruleSource, Map executionSetProperties, Map sessionProperties, List params);

	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param executionSetProperties 规则的属性Map(source=drl/xml dsl=java.io.Reader)
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	List executeRules(Object ruleSource, Map executionSetProperties, Map sessionProperties, List params);

	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	List executeRules(String ruleSource, List params);

	
	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	List executeRules(InputStream ruleSource, List params);

	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	List executeRules(Reader ruleSource, List params);

	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param params 参数
	 * @return 结果（包括参数）
	 * @throws RuleRuntimeException
	 */
	List executeRules(Object ruleSource, List params);

}
