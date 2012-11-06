/**
 * 
 */
package com.dayatang.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;

/**
 * 抽象实体类，可作为所有领域实体的基类，提供ID和版本属性。
 * 
 * @author yang
 * 
 */
@MappedSuperclass
public abstract class AbstractEntity implements Entity {

	private static final long serialVersionUID = 8882145540383345037L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Version
	private int version;
	
	@Transient
	private StringBuilder validationMessageBuilder = new StringBuilder();

	/**
	 * 获得实体的标识
	 * 
	 * @return 实体的标识
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置实体的标识
	 * 
	 * @param id
	 *            要设置的实体标识
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获得实体的版本号。持久化框架以此实现乐观锁。
	 * 
	 * @return 实体的版本号
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * 设置实体的版本号。持久化框架以此实现乐观锁。
	 * 
	 * @param version
	 *            要设置的版本号
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public boolean isNew() {
		return id == null || id.intValue() == 0;
	}

	
	
	/**
	 * 校验实体。如果子类决定覆盖该方法，在方法体的最后应调用super.validate()。
	 */
	protected void validate() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		if (validatorFactory == null) {
			return;
		}
		Validator validator = validatorFactory.getValidator();
		for (ConstraintViolation<AbstractEntity> violation : validator.validate(this)) {
			addValidationException(violation.getMessage());
		}
		String validationMessage = validationMessageBuilder.toString();
		if (StringUtils.isEmpty(validationMessage)) {
			return;
		}
		throw new javax.validation.ValidationException(validationMessage);
	}
	
	public void addValidationException(String message) {
		validationMessageBuilder.append(message + System.getProperty("line.separator"));
	}

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object other);

	@Override
	public abstract String toString();
}
