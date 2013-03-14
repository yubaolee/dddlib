package com.dayatang.rule;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.rules.InvalidRuleSessionException;
import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 无状态规则服务模板类。负责创建StatelessRuleSession,执行规则和关闭StatelessRuleSession。
 * 建立这个类的目的，一是消除了客户代码自行创建StatelessRuleSession的必要性，二是为了防止客户代码执行规则之后忘记释放StatelessRuleSession。
 * @author yyang <a href="mailto:gdyangyu@gmail.com">杨宇</a>
 *
 */
@SuppressWarnings("rawtypes")
public class StatelessRuleTemplate {
	
	private static Logger LOGGER = LoggerFactory.getLogger(StatelessRuleTemplate.class);

	private RuleAdministrator ruleAdministrator;
	private LocalRuleExecutionSetProvider ruleExecutionSetProvider;
	private RuleRuntime ruleRuntime;

	private RuleExecutionSet ruleExecutionSet;
	private Map sessionProperties;

	public final StatelessRuleTemplate sessionProperties(Map sessionProperties) {
		this.sessionProperties = sessionProperties;
		return this;
	}

	public final StatelessRuleTemplate ruleSource(String ruleSource, Map executionSetProperties) {
		return ruleSource(new StringReader(ruleSource), executionSetProperties);
	}

	public final StatelessRuleTemplate ruleSource(String ruleSource) {
		return ruleSource(ruleSource, null);
	}

	public final StatelessRuleTemplate ruleSource(Reader ruleSource, Map executionSetProperties) {
		try {
			this.ruleExecutionSet = ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, executionSetProperties);
		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public final StatelessRuleTemplate ruleSource(Reader ruleSource) {
		return ruleSource(ruleSource, null);
	}

	public final StatelessRuleTemplate ruleSource(InputStream ruleSource, Map executionSetProperties) {
		try {
			this.ruleExecutionSet = ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, executionSetProperties);
		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public final StatelessRuleTemplate ruleSource(InputStream ruleSource) {
		return ruleSource(ruleSource, null);
	}

	public final StatelessRuleTemplate ruleSource(Object ruleSource, Map executionSetProperties) {
		try {
			this.ruleExecutionSet = ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, executionSetProperties);
		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		}
		return this;
	}

	public final StatelessRuleTemplate ruleSource(Object ruleSource) {
		return ruleSource(ruleSource, null);
	}
	
	/**
	 * 构造函数
	 * @param ruleServiceProvider 规则服务提供者实现类，如Drools等。
	 */
	public StatelessRuleTemplate(RuleServiceProvider ruleServiceProvider) {
		this(ruleServiceProvider, null);
	}

	/**
	 * 构造函数
	 * @param ruleServiceProvider 规则服务提供者实现类，如Drools等。
	 * @param serviceProviderProperties 具体规则服务提供者所需要的额外属性
	 */
	public StatelessRuleTemplate(RuleServiceProvider ruleServiceProvider, Map serviceProviderProperties) {
		try {
			ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
			ruleExecutionSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(serviceProviderProperties);
			ruleRuntime = ruleServiceProvider.getRuleRuntime();
			LOGGER.info("The rule service provider of JSR94 is " + ruleServiceProvider.getClass());
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}

	/**
	 * 构造函数
	 * @param ruleServiceProvider 规则服务提供者实现类，如Drools等。
	 * @param serviceProviderProperties 具体规则服务提供者所需要的额外属性
	 * @param ruleSource 规则源，包含规则定义的内容。可能是字符串，Reader, InputStream或其他服务提供者支持的类型。
	 * @param executionSetProperties 规则的属性Map(如：source=drl/xml dsl=java.io.Reader)
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 */
	@Deprecated
	public StatelessRuleTemplate(RuleServiceProvider ruleServiceProvider, Map serviceProviderProperties, 
			Object ruleSource, Map executionSetProperties, Map sessionProperties) {
		try {
			ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
			ruleExecutionSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(serviceProviderProperties);
			ruleRuntime = ruleServiceProvider.getRuleRuntime();
			this.ruleExecutionSet = createRuleExecutionSet(ruleSource, executionSetProperties);
			LOGGER.info("The rule service provider of JSR94 is " + ruleServiceProvider.getClass());
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
		this.sessionProperties = sessionProperties;
	}

	private RuleExecutionSet createRuleExecutionSet(Object ruleSource, Map executionSetProperties) {
		try {
			if (ruleSource instanceof String) {
				Reader reader = new StringReader((String) ruleSource);
				return ruleExecutionSetProvider.createRuleExecutionSet(reader, executionSetProperties);
			}
			if (ruleSource instanceof Reader) {
				Reader reader = (Reader) ruleSource;
				return ruleExecutionSetProvider.createRuleExecutionSet(reader, executionSetProperties);
			}
			if (ruleSource instanceof InputStream) {
				InputStream in = (InputStream) ruleSource;
				return ruleExecutionSetProvider.createRuleExecutionSet(in, executionSetProperties);
			}
			return ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, executionSetProperties);

		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 执行规则
	 * @param callback
	 * @return
	 * @throws Exception
	 */
	public List execute(StatelessRuleCallback callback) throws Exception {
		StatelessRuleSession session = createStatelessRuleSession();
		try {
			return callback.doInRuleSession(session);
		} finally {
			releaseStatelessRuleSession(session);
		}
	}

	/**
	 * 执行规则
	 * @param facts
	 * @return
	 * @throws Exception
	 */
	public List execute(List facts) {
		StatelessRuleSession session = createStatelessRuleSession();
		try {
			return session.executeRules(facts);
		} catch (InvalidRuleSessionException e) {
			 throw new RuleRuntimeException(e);
		} catch (RemoteException e) {
			 throw new RuleRuntimeException(e);
		} finally {
			releaseStatelessRuleSession(session);
		}
	}

	private StatelessRuleSession createStatelessRuleSession() {
		try{
			String packageName = ruleExecutionSet.getName();
			ruleAdministrator.registerRuleExecutionSet(packageName, ruleExecutionSet, null);
			return (StatelessRuleSession) ruleRuntime.createRuleSession(packageName, sessionProperties, RuleRuntime.STATELESS_SESSION_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuleRuntimeException("Cannot create Rule Session!!!", e);
		}
	}

	private void releaseStatelessRuleSession(StatelessRuleSession session) {
		try {
			session.release();
		} catch (Exception e) {
			throw new RuleRuntimeException("Cannot release rule session!!", e);
		}
	}
}
