
package org.webframe.easy.model.form;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 类功能描述：
 * 
 * @author <a href="mailto:guoqing.huang@foxmail.com>huangguoqing</a>
 * @version $Id: codetemplates.xml,v 1.3 2009/05/05 02:30:07 huangguoqing Exp $ Create: 2011-1-10
 *          下午05:14:20
 */
public class ViewElement {

	private String						name;

	private String						property;

	private String						type		= ViewElementType.未知类型.getValue();

	private String						css;

	private boolean					column	= true;

	private boolean					query		= true;

	private boolean					formed	= true;

	private QueryConditionType		queryConditionType;

	private Map<String, Object>	valueMap	= new LinkedHashMap<String, Object>();

	public ViewElement() {
		this("主键ID", "id", ViewElementType.HIDDEN, "", false, false, false, QueryConditionType.模糊类型);
	}

	/**
	 * @param name Entity属性名称，一般为中文
	 * @param property Entity属性，例如：id, name, username, password等。
	 * @param type 该Entity属性的页面form类型，为ViewElementType枚举
	 * @param css 该Entity属性在页面form中的样式
	 */
	public ViewElement(String name, String property, ViewElementType type, String css) {
		this(name, property, type, css, true, true, true, QueryConditionType.模糊类型);
	}

	/**
	 * @param name Entity属性名称，一般为中文
	 * @param property Entity属性，例如：id, name, username, password等。
	 * @param type 该Entity属性的页面form类型，为ViewElementType枚举
	 * @param css 该Entity属性在页面form中的样式
	 * @param formed 布尔值，true表示该Entity属性为新增页面中表单元素，默认值为true
	 * @param column 布尔值，true表示该Entity属性在列表页查询中显示该列，默认值为true
	 * @param query 布尔值，true表示该Entity属性在列表页查询中显示该查询条件，默认值为true
	 */
	public ViewElement(String name,
				String property,
				ViewElementType type,
				String css,
				boolean formed,
				boolean column,
				boolean query,
				QueryConditionType queryConditionType) {
		setName(name);
		setProperty(property);
		setType(type);
		setCss(css);
		setFormed(formed);
		setListPageConfig(column, query, queryConditionType);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getType() {
		return type;
	}

	public void setType(ViewElementType type) {
		this.type = type.getValue();
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public Map<String, Object> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, Object> valueMap) {
		this.valueMap = valueMap;
	}

	public boolean isColumn() {
		return column;
	}

	public void setColumn(boolean column) {
		this.column = column;
	}

	public boolean isQuery() {
		return query;
	}

	public void setQuery(boolean query) {
		this.query = query;
	}

	public boolean isFormed() {
		return formed;
	}

	public ViewElement setFormed(boolean formed) {
		this.formed = formed;
		return this;
	}

	public QueryConditionType getQueryConditionType() {
		return queryConditionType;
	}

	/**
	 * @param column 布尔值，true表示该Entity属性在列表页查询中显示该列，默认值为true
	 * @param query 布尔值，true表示该Entity属性在列表页查询中显示该查询条件，默认值为true
	 * @param queryConditionType 枚举类型
	 * @return
	 * @author: 黄国庆 2011-1-25 上午10:18:16
	 */
	public ViewElement setListPageConfig(boolean column, boolean query, QueryConditionType queryConditionType) {
		setColumn(column);
		setQuery(query);
		if (query && queryConditionType == null) {
			this.queryConditionType = QueryConditionType.模糊类型;
		} else {
			this.queryConditionType = queryConditionType;
		}
		return this;
	}

	public enum QueryConditionType {
		模糊类型("fuzzy"), 布尔类型("bool"), 区间类型("interval"), 等值类型("equal");

		private final String	value;

		private QueryConditionType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public String toString() {
			return this.getValue();
		}
	}
}
