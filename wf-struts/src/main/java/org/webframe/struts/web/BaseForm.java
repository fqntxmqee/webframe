
package org.webframe.struts.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts.validator.ValidatorActionForm;

public class BaseForm extends ValidatorActionForm {

	/**
	 *
	 */
	private static final long		serialVersionUID	= -345774068093223220L;

	/**
	 * 查询条件
	 */
	private Map<String, String>	queryMap				= new HashMap<String, String>();

	/**
	 * @param attributeKey
	 * @param attributeValue
	 */
	public void setAttribute(String attributeKey, Object attributeValue) {
		if (attributeValue != null) {
			getQueryMap().put(attributeKey, attributeValue.toString().trim());
		} else {
			getQueryMap().put(attributeKey, null);
		}
	}

	/**
	 * @param attributeKey
	 * @return
	 */
	public Object getAttribute(String attributeKey) {
		return getQueryMap().get(attributeKey);
	}

	/**
	 * @return 返回 conMap。
	 */
	public final Map<String, String> getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(Map<String, String> queryMap) {
		this.queryMap = queryMap;
	}
}
