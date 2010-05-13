package com.dayatang.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 集合工具
 * @author yyang
 *
 */
public class CollectionUtils {
	
	/**
	 * 抽取集合中的一个字段，用指定的分隔符连接起来形成一个字符串。
	 * 如果集合是一个Set，则返回的字符串中包含的元素是无序且不重复的，
	 * 如果集合是一个List，则返回的字符串中包含的元素遵从集合元素的排
	 * 列顺序，且可能有重复。
	 * @param collection
	 * @param field
	 * @param separator
	 * @return
	 */
	public static String join(Collection<?> collection, String field, String separator) {
		Collection<String> results;
		if (collection instanceof Set<?>) {
			results = new HashSet<String>();
		} else {
			results = new ArrayList<String>();
		}
		try {
			for (Object item : collection) {
				results.add(BeanUtils.getProperty(item, field));
			}
			return StringUtils.join(results, separator);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
